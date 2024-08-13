package fr.orleans.serviceevenement.dao;

import fr.orleans.serviceevenement.entities.TypeEvenementSportif;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface TypeEvenementSportifDAO extends JpaRepository<TypeEvenementSportif,Long> {

    List<TypeEvenementSportif> findTypeEvenementByNomTypeEvenement(String nomTypeEvenement);
}
