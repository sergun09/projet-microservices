package fr.orleans.serviceauthentification.service;

import fr.orleans.serviceauthentification.dao.UtilisateurDAO;
import fr.orleans.serviceauthentification.entities.Role;
import fr.orleans.serviceauthentification.entities.Utilisateur;
import fr.orleans.serviceauthentification.service.exceptions.EmailExistantException;
import fr.orleans.serviceauthentification.service.exceptions.UtilisateurInexistantException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FacadeAuthentificationImpl implements FacadeAuthentification{
    private UtilisateurDAO utilisateurDAO;

    public FacadeAuthentificationImpl(UtilisateurDAO utilisateurDAO) {
        this.utilisateurDAO = utilisateurDAO;
    }

    @Override
    public Utilisateur inscription(String email, String nom, String prenom, String mdpUtilisateur) throws EmailExistantException {
        List<Utilisateur> utilisateurs = utilisateurDAO.findUtilisateurByEmail(email);
        if (utilisateurs.size() != 0)
            throw new EmailExistantException();
        Utilisateur u = utilisateurDAO.save(Utilisateur.builder()
                .email(email)
                .nom(nom)
                .prenom(prenom)
                .mdpUtilisateur(mdpUtilisateur)
                .roles(Set.of(Role.ROLE_USER))
                .build());
        return u;
    }

    @Override
    public Utilisateur getInscriptionById(long idUtilisateur) throws UtilisateurInexistantException{
        Optional<Utilisateur> utilisateur = utilisateurDAO.findById(idUtilisateur);
        if (utilisateur.isEmpty())
            throw new UtilisateurInexistantException();
        return utilisateur.get();
    }

    @Override
    public Utilisateur getInscriptionByEmail(String email) throws UtilisateurInexistantException{
        List<Utilisateur> utilisateurs = utilisateurDAO.findUtilisateurByEmail(email);
        if (utilisateurs.size()==0)
            throw new UtilisateurInexistantException();
        return utilisateurs.get(0);
    }

    @Override
    public Page<Utilisateur> getInscriptions(Pageable pageable) {
        return utilisateurDAO.findAll(pageable);
    }

    @Override
    public Utilisateur majInscription(long idUtilisateur, String email, String nom, String prenom, String mdpUtilisateur)
            throws EmailExistantException, UtilisateurInexistantException
    {

        List<Utilisateur> utilisateurs = utilisateurDAO.findUtilisateurByEmail(email);
        if (utilisateurs.size()!=0)
            throw new EmailExistantException();

        Optional<Utilisateur> optionalUtilisateur = utilisateurDAO.findById(idUtilisateur);
        if (optionalUtilisateur.isEmpty())
            throw new UtilisateurInexistantException();
        Utilisateur utilisateur = optionalUtilisateur.get();
        utilisateur.setEmail(email);
        utilisateur.setMdpUtilisateur(mdpUtilisateur);
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        return utilisateurDAO.save(utilisateur);
    }
}
