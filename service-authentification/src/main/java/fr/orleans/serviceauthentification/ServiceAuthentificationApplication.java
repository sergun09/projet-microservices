package fr.orleans.serviceauthentification;

import fr.orleans.serviceauthentification.dao.UtilisateurDAO;
import fr.orleans.serviceauthentification.entities.Role;
import fr.orleans.serviceauthentification.entities.Utilisateur;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class ServiceAuthentificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceAuthentificationApplication.class, args);
    }
    @Bean
    public CommandLineRunner commandLineRunner(UtilisateurDAO utilisateurDAO, PasswordEncoder passwordEncoder){
        return args -> {
            utilisateurDAO.save(Utilisateur.builder()
                            .email("u1@test.com")
                            .nom("user1")
                            .prenom("prenom user1")
                            .mdpUtilisateur(passwordEncoder.encode("user1"))
                            .roles(Set.of(Role.ROLE_USER))
                    .build());

            utilisateurDAO.save(Utilisateur.builder()
                    .email("admin@test.com")
                    .nom("admin")
                    .prenom("prenom admin")
                    .mdpUtilisateur(passwordEncoder.encode("admin"))
                    .roles(Set.of(Role.ROLE_USER, Role.ROLE_ADMIN))
                    .build());
        };
    }

}
