package fr.orleans.servicepaiement.controllers;

import fr.orleans.servicepaiement.dtos.ReglementDTO;
import fr.orleans.servicepaiement.dtos.TransactionDTO;
import fr.orleans.servicepaiement.dtos.VersementDTO;
import fr.orleans.servicepaiement.models.Compte;
import fr.orleans.servicepaiement.models.Transaction;
import fr.orleans.servicepaiement.models.TypeTransaction;
import fr.orleans.servicepaiement.services.PaiementService;
import fr.orleans.servicepaiement.services.RabbitMqEnvoie;
import fr.orleans.servicepaiement.services.exceptions.CompteInexistantException;
import fr.orleans.servicepaiement.services.exceptions.OperationNonAutoriseeException;
import fr.orleans.servicepaiement.services.exceptions.SoldeInsuffisantException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/paiement")

public class ControleurPaiement {

    @Autowired
    PaiementService paiementService;

    @Autowired
    RabbitMqEnvoie rabbitMqEnvoie;

    @GetMapping("/comptes/utilisateurs/{idUtilisateur}")
    public ResponseEntity<Compte> getCompteByIdUtilisateur(@PathVariable Long idUtilisateur,Authentication authentication) throws CompteInexistantException {
        if(idUtilisateur!=getIdUtilisateur(authentication) &&
                authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Compte compte = paiementService.getCompteByIdUtilisateur(idUtilisateur);
        return ResponseEntity.ok(compte);
    }

    @PostMapping("/reglements")
    public ResponseEntity<Transaction> reglementPari(@RequestBody ReglementDTO dto,Authentication authentication) throws CompteInexistantException, SoldeInsuffisantException {
        Long idUtilisateur = getIdUtilisateur(authentication);
        Transaction transaction = paiementService.creerTransactionPayerPari(dto.idPari(), idUtilisateur, dto.montant());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{idTransaction}")
                .buildAndExpand(transaction.getIdTransaction()).toUri();
        rabbitMqEnvoie.envoyer(new TransactionDTO(idUtilisateur,transaction.getDate(),transaction.getMontant(),transaction.getTypeTransaction().name()));
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/versements")
    public ResponseEntity<Transaction> versement(@RequestBody VersementDTO dto,Authentication authentication) throws CompteInexistantException, SoldeInsuffisantException, OperationNonAutoriseeException {
        TypeTransaction typeTransaction = TypeTransaction.valueOf(dto.typeTransaction());
        Long idUtilisateur = getIdUtilisateur(authentication);
        Transaction transaction = null;

        switch (typeTransaction) {
            case CB_COMPTE_PARIEUR:
                transaction = paiementService.creerTransactionCBVersCompte(idUtilisateur, dto.idCompte(), dto.montant());
                break;
            case COMPTE_PARIEUR_CB:
                transaction = paiementService.creerTransactionCompteVersCB(idUtilisateur, dto.idCompte(), dto.montant());
                break;
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{idTransaction}")
                .buildAndExpand(transaction.getIdTransaction()).toUri();
        rabbitMqEnvoie.envoyer(new TransactionDTO(idUtilisateur,transaction.getDate(),transaction.getMontant(),transaction.getTypeTransaction().name()));
        return ResponseEntity.created(location).build();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping ("/test")
    public ResponseEntity<String> test(Authentication authentication){
        return ResponseEntity.ok(authentication.getName());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping ("/test/admin")
    public ResponseEntity<String> testAdmin(Authentication authentication){
        return ResponseEntity.ok(authentication.getName());
    }
    @GetMapping ("/test/infos")
    public ResponseEntity<Map<String,String>> infos(Authentication authentication)
    {

        // Accédez au champ claims pour récupérez la valeur associée à la clé "user_id"
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        Map<String, Object> claims = jwtToken.getClaims();
        Long userId = (long) claims.get("idUtilisateur");
        String nom = (String) claims.get("nom");
        String prenom = (String) claims.get("prenom");
        return ResponseEntity.ok(Map.of("idUtilisateur",String.valueOf(userId),
                "nom",nom,
                "prenom",prenom));
    }

    public Long getIdUtilisateur(Authentication authentication){
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        Map<String, Object> claims = jwtToken.getClaims();
        Long userId = (long) claims.get("idUtilisateur");
        return userId;
    }
}



