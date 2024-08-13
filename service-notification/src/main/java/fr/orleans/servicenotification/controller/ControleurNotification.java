package fr.orleans.servicenotification.controller;

import fr.orleans.servicenotification.dtos.NotificationDTO;
import fr.orleans.servicenotification.dtos.PageDTO;
import fr.orleans.servicenotification.entities.Notification;
import fr.orleans.servicenotification.service.FacadeNotification;
import fr.orleans.servicenotification.service.exceptions.NotificationInexistanteException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/notifications")
public class ControleurNotification {
    private final FacadeNotification facadeNotification;

    public ControleurNotification(FacadeNotification facadeNotification) {
        this.facadeNotification = facadeNotification;
    }


    @PostMapping("")
    public ResponseEntity<String> addNotification(@RequestBody NotificationDTO notificationDTO){
        Notification notification = facadeNotification.addNotification(
                notificationDTO.idUtilisateur(),
                notificationDTO.description(),
                notificationDTO.typeNotification()
        );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{idNotification}")
                .buildAndExpand(notification.getIdNotification()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{idNotification}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable long idNotification) {
        Notification notification;

        try {
            notification = facadeNotification.getNotificationById(idNotification);
        } catch (NotificationInexistanteException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                new NotificationDTO(
                        notification.getIdNotification(),
                        notification.getIdUtilisateur(),
                        notification.getDescription(),
                        notification.getTypeNotification()
                )
        );
    }

    @GetMapping("/utilisateur/{idUtilisateur}")
    public ResponseEntity<PageDTO> getNotificationsByIdUtilisateur(@PathVariable long idUtilisateur,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "5") int size) {
        List<Notification> notificationList;
        List<NotificationDTO> notificationDTOList = new ArrayList<>();

        Pageable pageable = PageRequest.of(page, size);

        Page<Notification> pageUtil = facadeNotification.getNotificationsByIdUtilisateur(idUtilisateur, pageable);
        PageDTO<Notification> pageDTO = new PageDTO<>(pageUtil.getContent(),
                pageUtil.getNumber(),
                pageUtil.getTotalElements(),
                pageUtil.getTotalPages());

        return ResponseEntity.ok(pageDTO);
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
}
