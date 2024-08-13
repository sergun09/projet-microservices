package fr.orleans.servicepaiement;

import fr.orleans.servicepaiement.dao.CompteDAO;
import fr.orleans.servicepaiement.models.Compte;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Set;

@SpringBootApplication
public class ServicePaiementApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ServicePaiementApplication.class, args);
    }
    @Bean
    public CommandLineRunner commandLineRunner(CompteDAO compteDAO){
        return args -> {
            compteDAO.save(new Compte(1L,10));

            compteDAO.save(new Compte(2L,20));
        };
    }

}
