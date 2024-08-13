package fr.orleans.serviceauthentification.dao;

import fr.orleans.serviceauthentification.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UtilisateurDAO extends JpaRepository<Utilisateur,Long> {
    List<Utilisateur> findUtilisateurByEmail(String email);

}
