package fr.orleans.serviceevenement;

import fr.orleans.serviceevenement.dao.EvenementSportifDAO;
import fr.orleans.serviceevenement.dao.TypeEvenementSportifDAO;
import fr.orleans.serviceevenement.entities.EvenementSportif;
import fr.orleans.serviceevenement.entities.TypeEvenementSportif;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class ServiceEvenementApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ServiceEvenementApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(EvenementSportifDAO evenementSportifDAO, TypeEvenementSportifDAO typeEvenementSportifDAO) {
        return args -> {
            typeEvenementSportifDAO.saveAll(
                    Arrays.asList(new TypeEvenementSportif("Foot"),
                            new TypeEvenementSportif("Rugby"),
                            new TypeEvenementSportif("Basket"))
            );
            EvenementSportif evenementSportif = new EvenementSportif();
            LocalDateTime date = LocalDateTime.of(2024, 04, 02, 14, 33, 48, 640000);
            evenementSportif.setTypeEvenement(typeEvenementSportifDAO.findTypeEvenementByNomTypeEvenement("Foot").get(0));
            evenementSportif.setEquipe1("Real Madrid");
            evenementSportif.setEquipe2("Barcelona");
            evenementSportif.setDateEvenement(date);
            evenementSportif.setVille("Madrid");
            evenementSportif.setCoteEquipe1(3.0);
            evenementSportif.setCoteEquipe2(1.5);
            evenementSportif.setCoteNul(1.1);
            evenementSportifDAO.save(evenementSportif);

            EvenementSportif evenementSportif2 = new EvenementSportif();
            LocalDateTime date2 = LocalDateTime.of(2024, 04, 01, 13, 37, 48, 640000);
            evenementSportif2.setTypeEvenement(typeEvenementSportifDAO.findTypeEvenementByNomTypeEvenement("Foot").get(0));
            evenementSportif2.setEquipe1("France");
            evenementSportif2.setEquipe2("Brazil");
            evenementSportif2.setDateEvenement(date2);
            evenementSportif2.setVille("Paris");
            evenementSportif2.setCoteEquipe1(2.8);
            evenementSportif2.setCoteEquipe2(1.6);
            evenementSportif2.setCoteNul(1.3);
            evenementSportif2.setTypeResultat("Equipe1");
            evenementSportifDAO.save(evenementSportif2);

            EvenementSportif evenementSportif3 = new EvenementSportif();
            LocalDateTime date3 = LocalDateTime.of(2024, 04, 03, 15, 30, 48, 640000);
            evenementSportif3.setTypeEvenement(typeEvenementSportifDAO.findTypeEvenementByNomTypeEvenement("Foot").get(0));
            evenementSportif3.setEquipe1("Chelsea");
            evenementSportif3.setEquipe2("Arsenal");
            evenementSportif3.setDateEvenement(date3);
            evenementSportif3.setVille("Londres");
            evenementSportif3.setCoteEquipe1(2.5);
            evenementSportif3.setCoteEquipe2(1.9);
            evenementSportif3.setCoteNul(1.2);
            evenementSportif3.setTypeResultat("Equipe2");
            evenementSportifDAO.save(evenementSportif3);


        };
    }


}
