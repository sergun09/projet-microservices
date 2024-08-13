package fr.orleans.serviceauthentification.service;

import fr.orleans.serviceauthentification.entities.Utilisateur;
import fr.orleans.serviceauthentification.service.exceptions.EmailExistantException;
import fr.orleans.serviceauthentification.service.exceptions.UtilisateurInexistantException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FacadeAuthentification {
    Utilisateur inscription(String email,String nom, String prenom, String mdpUtilisateur) throws EmailExistantException;
    Utilisateur getInscriptionById(long idUtilisateur) throws UtilisateurInexistantException;
    Page<Utilisateur> getInscriptions(Pageable pageable);
     Utilisateur getInscriptionByEmail(String email) throws UtilisateurInexistantException;
     Utilisateur majInscription(long idUtilisateur,String email,String nom,
                                String prenom, String mdpUtilisateur) throws EmailExistantException, UtilisateurInexistantException;
}
