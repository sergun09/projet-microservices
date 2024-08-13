package fr.orleans.servicepaiement.dao;

import fr.orleans.servicepaiement.models.Compte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompteDAO extends JpaRepository<Compte,Long> {

    Optional<Compte> findById(Long idCompte);

    Optional<Compte> findByIdUtilisateur(Long idUtilisateur);

}
