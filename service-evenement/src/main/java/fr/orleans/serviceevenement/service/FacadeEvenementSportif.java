package fr.orleans.serviceevenement.service;

import fr.orleans.serviceevenement.dtos.EvenementSportifDTO;
import fr.orleans.serviceevenement.entities.EvenementSportif;
import fr.orleans.serviceevenement.service.exceptions.EvenementSportifNonExistantException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface FacadeEvenementSportif {

    EvenementSportif creerEvenementSportif(String typeEvenementSportif, String equipe1, String equipe2, LocalDateTime dateEvenement, String ville, Double coteEquipe1, Double coteEquipe2, Double coteNul);

    EvenementSportif modifierEvenementSportif(long idEvenementSportif,String nomTypeEvenementSportif, String equipe1, String equipe2, LocalDateTime dateEvenement, String ville, Double coteEquipe1, Double coteEquipe2, Double coteNul) throws EvenementSportifNonExistantException;

    EvenementSportif getEvenementSportifById(long id) throws EvenementSportifNonExistantException;

    EvenementSportif modifierResultat(long idEvenementSportif, String typeResultat) throws EvenementSportifNonExistantException;

    void supprimerEvenementSportif(long idEvenementSportif) throws EvenementSportifNonExistantException;


    Page<EvenementSportif> getEvenementsSportifs(Pageable pageable);

    Double getCoteResultat(long id) throws EvenementSportifNonExistantException;



}
