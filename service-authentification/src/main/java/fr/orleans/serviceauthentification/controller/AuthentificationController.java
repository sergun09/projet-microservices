package fr.orleans.serviceauthentification.controller;

import fr.orleans.serviceauthentification.dao.UtilisateurDAO;
import fr.orleans.serviceauthentification.dtos.AuthentificationDTO;
import fr.orleans.serviceauthentification.dtos.PageDTO;
import fr.orleans.serviceauthentification.dtos.UtilisateurDTO;
import fr.orleans.serviceauthentification.entities.Role;
import fr.orleans.serviceauthentification.entities.Utilisateur;
import fr.orleans.serviceauthentification.service.FacadeAuthentificationImpl;
import fr.orleans.serviceauthentification.service.RabbitMqService;
import fr.orleans.serviceauthentification.service.exceptions.UtilisateurInexistantException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.function.Function;

@RestController
@RequestMapping("/authentification")
//@EnableWebSecurity
public class AuthentificationController {
    private static final Object TOKEN_PREFIX = "Bearer ";
    private final PasswordEncoder passwordEncoder;
    private final FacadeAuthentificationImpl facadeAuthentification;
    private final RabbitMqService rabbitMqService;
    Function<Utilisateur,String> genereToken;

    public AuthentificationController(UtilisateurDAO utilisateurDAO,
                                      PasswordEncoder passwordEncoder,
                                      FacadeAuthentificationImpl facadeAuthentification,
                                      RabbitMqService rabbitMqService,
                                      Function<Utilisateur, String> genereToken) {
        this.passwordEncoder = passwordEncoder;
        this.facadeAuthentification = facadeAuthentification;
        this.genereToken = genereToken;
        this.rabbitMqService = rabbitMqService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/inscriptions")
    public ResponseEntity<PageDTO> get(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "5") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<Utilisateur> pageUtil = facadeAuthentification.getInscriptions(pageable);
        PageDTO<Utilisateur> pageDTO = new PageDTO<>(pageUtil.getContent(),
                pageUtil.getNumber(),
                pageUtil.getTotalElements(),
                pageUtil.getTotalPages());
        return ResponseEntity.ok(pageDTO);
    }

    @PostMapping(value = "/inscriptions")
    public ResponseEntity nscription(@Valid @RequestBody UtilisateurDTO utilisateurDTO) throws Exception{
        String encodedPassword = passwordEncoder.encode(utilisateurDTO.mdpUtilisateur());
        Utilisateur u = facadeAuthentification.inscription(
                utilisateurDTO.email(),
                utilisateurDTO.nom(),
                utilisateurDTO.prenom(),
                encodedPassword);
        //envoie de l'utilisateur sur le message broker RabbitMQ

        UtilisateurDTO dto= new UtilisateurDTO(u.getIdUtilisateur(),u.getEmail(),u.getNom(),
                u.getPrenom(),u.getMdpUtilisateur());
        rabbitMqService.envoyer(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{idUtilisateur}")
                .buildAndExpand(u.getIdUtilisateur()).toUri();
        return ResponseEntity.created(location).header(HttpHeaders.AUTHORIZATION,
                TOKEN_PREFIX+genereToken.apply(u)).build();
    }


    @GetMapping(value = "/inscriptions/{idUtilisateur}")
    public ResponseEntity<UtilisateurDTO> inscription(@PathVariable long idUtilisateur, Authentication  authentication)
            throws UtilisateurInexistantException
    {
        Long idUtil = getIdUtilisateurFromToken(authentication);

        if(idUtil!=idUtilisateur &&
                authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Utilisateur u = facadeAuthentification.getInscriptionById(idUtilisateur);
        return ResponseEntity.ok(
                new UtilisateurDTO(u.getIdUtilisateur(),u.getEmail(),u.getNom(),
                        u.getPrenom(),u.getMdpUtilisateur())
        );
    }


    @PostMapping("/login")
    public ResponseEntity login( @Valid @RequestBody AuthentificationDTO authDTO) throws UtilisateurInexistantException{
        Utilisateur u = facadeAuthentification.getInscriptionByEmail(authDTO.email());

        if (passwordEncoder.matches(authDTO.mdpUtilisateur(), u.getMdpUtilisateur())) {
            String token = genereToken.apply(u);
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION,"Bearer "+token).build();
        };
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping (value = "/inscriptions/{idUtilisateur}")
    public ResponseEntity<UtilisateurDTO> inscription(@PathVariable long idUtilisateur,@Valid @RequestBody UtilisateurDTO utilisateurDTO,
                                                      Authentication authentication)
            throws Exception
    {
        Utilisateur u = facadeAuthentification.getInscriptionById(idUtilisateur);
        Long idUtil = getIdUtilisateurFromToken(authentication);

        if(idUtil!=u.getIdUtilisateur() &&
                authentication.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        String encodedPassword = passwordEncoder.encode(utilisateurDTO.mdpUtilisateur());
        Utilisateur nouvUtilisateur = facadeAuthentification.majInscription(
                idUtilisateur,
                utilisateurDTO.email(),
                utilisateurDTO.nom(),
                utilisateurDTO.prenom(),
                encodedPassword);

        return ResponseEntity.ok(
                new UtilisateurDTO(nouvUtilisateur.getIdUtilisateur(),nouvUtilisateur.getEmail(),
                        nouvUtilisateur.getNom(),
                        nouvUtilisateur.getPrenom(),nouvUtilisateur.getMdpUtilisateur())
        );
    }

    public Long getIdUtilisateurFromToken(Authentication authentication){
        Jwt jwtToken = (Jwt) authentication.getPrincipal();
        Map<String, Object> claims = jwtToken.getClaims();
        Long idUtil = (long) claims.get("idUtilisateur");
        return  idUtil;
    }
}
