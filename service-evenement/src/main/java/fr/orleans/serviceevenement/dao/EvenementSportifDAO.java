package fr.orleans.serviceevenement.dao;

import fr.orleans.serviceevenement.entities.EvenementSportif;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvenementSportifDAO extends JpaRepository<EvenementSportif,Long> {
}
