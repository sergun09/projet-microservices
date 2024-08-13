package fr.orleans.serviceevenement.controller;

import fr.orleans.serviceevenement.dao.EvenementSportifDAO;
import fr.orleans.serviceevenement.dtos.*;
import fr.orleans.serviceevenement.entities.EvenementSportif;
import fr.orleans.serviceevenement.service.FacadeEvenementSportifImpl;
import fr.orleans.serviceevenement.service.RabbitMqService;
import fr.orleans.serviceevenement.service.exceptions.EvenementSportifNonExistantException;
import org.hibernate.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/evenement")

public class EvenementSportifController {

    private final FacadeEvenementSportifImpl facadeEvenementSportif;

    private final RabbitMqService rabbitMqService;


    public EvenementSportifController(FacadeEvenementSportifImpl facadeEvenementSportif, RabbitMqService rabbitMqService) {
        this.facadeEvenementSportif = facadeEvenementSportif;
        this.rabbitMqService = rabbitMqService;
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

        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        Map<String, Object> claims = jwtToken.getClaims();
        Long userId = (long) claims.get("idUtilisateur");
        String nom = (String) claims.get("nom");
        String prenom = (String) claims.get("prenom");
        return ResponseEntity.ok(Map.of("idUtilisateur",String.valueOf(userId),
                "nom",nom,
                "prenom",prenom));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "")
    public ResponseEntity<EvenementSportifDTO> creerEvenementSportif(@RequestBody EvenementSportifDTO e1)
    {
        EvenementSportif e2= facadeEvenementSportif.creerEvenementSportif(e1.nomTypeEvenement(),e1.equipe1(),e1.equipe2(),e1.dateEvenement(),e1.ville(),e1.coteEquipe1(), e1.coteEquipe2(), e1.coteNul());
        EvenementSportifDTO e3= new EvenementSportifDTO(e2.getIdEvenement(),e2.getTypeEvenement().getNomTypeEvenement(),e2.getEquipe1(),e2.getEquipe2(),e2.getDateEvenement(),e2.getVille(),e2.getCoteEquipe1(), e2.getCoteEquipe2(), e2.getCoteNul(), e2.getTypeResultat());
        URI location = ServletUriComponentsBuilder
                 .fromCurrentRequest().path("/{id}")
            .buildAndExpand(e3.idEvenement()).toUri();
        EvenementCreationDTO evenementCreationDTO = new EvenementCreationDTO(e2.getIdEvenement(),e2.getDateEvenement());
        rabbitMqService.envoyerEvenementCreation(evenementCreationDTO);
        return ResponseEntity.created(location).body(e3);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping (value = "/{id}")
    public ResponseEntity<EvenementSportifDTO> getEvenementSportif(@PathVariable long id) throws EvenementSportifNonExistantException
    {
        EvenementSportif e1 = facadeEvenementSportif.getEvenementSportifById(id);
        EvenementSportifDTO e2= new EvenementSportifDTO(e1.getIdEvenement(),e1.getTypeEvenement().getNomTypeEvenement(),e1.getEquipe1(),e1.getEquipe2(),e1.getDateEvenement(),e1.getVille(),e1.getCoteEquipe1(), e1.getCoteEquipe2(), e1.getCoteNul(), e1.getTypeResultat());
        return ResponseEntity.ok(e2);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping (value = "/{id}")
    public ResponseEntity<String> supprimerEvenementSportif(@PathVariable long id) throws EvenementSportifNonExistantException
    {
        facadeEvenementSportif.supprimerEvenementSportif(id);
        EvenementSupprimerDTO evenementSupprimerDTO = new EvenementSupprimerDTO(id);
        rabbitMqService.envoyerEvenementSuppression(evenementSupprimerDTO);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping (value = "/{id}")
    public ResponseEntity<EvenementSportifDTO> modifierEvenementSportif(@PathVariable long id, @RequestBody EvenementSportifDTO e1) throws EvenementSportifNonExistantException
    {
        EvenementSportif e2 = facadeEvenementSportif.modifierEvenementSportif(id, e1.nomTypeEvenement(),e1.equipe1(),e1.equipe2(),e1.dateEvenement(),e1.ville(),e1.coteEquipe1(), e1.coteEquipe2(), e1.coteNul());
        EvenementSportifDTO e3= new EvenementSportifDTO(e2.getIdEvenement(),e2.getTypeEvenement().getNomTypeEvenement(),e2.getEquipe1(),e2.getEquipe2(),e2.getDateEvenement(),e2.getVille(),e2.getCoteEquipe1(), e2.getCoteEquipe2(), e2.getCoteNul(), e2.getTypeResultat());
        return ResponseEntity.ok(e3);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping (value = "/{id}/resultat")
    public ResponseEntity<EvenementSportifDTO> modifierResultat(@PathVariable long id, @RequestBody EvenementSportifDTO e1) throws EvenementSportifNonExistantException
    {
        EvenementSportif e2 = facadeEvenementSportif.modifierResultat(id, e1.typeResultat());
        EvenementSportifDTO e3= new EvenementSportifDTO(e2.getIdEvenement(),e2.getTypeEvenement().getNomTypeEvenement(),e2.getEquipe1(),e2.getEquipe2(),e2.getDateEvenement(),e2.getVille(),e2.getCoteEquipe1(), e2.getCoteEquipe2(), e2.getCoteNul(), e2.getTypeResultat());
        Double coteResulat = facadeEvenementSportif.getCoteResultat(e2.getIdEvenement());
        System.out.println(coteResulat);
        EvenementResultatDTO evenementResultatDTO = new EvenementResultatDTO(e2.getIdEvenement(), e2.getTypeResultat(), coteResulat);
        rabbitMqService.envoyerEvenementResultat(evenementResultatDTO);
        return ResponseEntity.ok(e3);
    }


    @GetMapping(value = "")
    public ResponseEntity<PageDTO> get(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<EvenementSportif> pageUtil = facadeEvenementSportif.getEvenementsSportifs(pageable);
        PageDTO<EvenementSportif> pageDTO = new PageDTO<>(pageUtil.getContent(),
                pageUtil.getNumber(),
                pageUtil.getTotalElements(),
                pageUtil.getTotalPages());
        return ResponseEntity.ok(pageDTO);
    }

}
