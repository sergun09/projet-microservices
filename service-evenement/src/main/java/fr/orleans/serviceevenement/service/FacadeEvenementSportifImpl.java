package fr.orleans.serviceevenement.service;

import fr.orleans.serviceevenement.dao.EvenementSportifDAO;
import fr.orleans.serviceevenement.dao.TypeEvenementSportifDAO;
import fr.orleans.serviceevenement.entities.EvenementSportif;
import fr.orleans.serviceevenement.entities.TypeEvenementSportif;
import fr.orleans.serviceevenement.service.exceptions.EvenementSportifNonExistantException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class FacadeEvenementSportifImpl implements FacadeEvenementSportif{

    private EvenementSportifDAO evenementSportifDAO;

    private TypeEvenementSportifDAO typeEvenementSportifDAO;

    public FacadeEvenementSportifImpl(EvenementSportifDAO evenementSportifDAO, TypeEvenementSportifDAO typeEvenementSportifDAO) { this.evenementSportifDAO = evenementSportifDAO ;
    this.typeEvenementSportifDAO = typeEvenementSportifDAO;
    }





    public EvenementSportif creerEvenementSportif(String nomTypeEvenementSportif, String equipe1, String equipe2, LocalDateTime dateEvenement, String ville, Double coteEquipe1, Double coteEquipe2, Double coteNul) {

        EvenementSportif evenementSportif = null;
        List<TypeEvenementSportif> listeTypeEvenementSportif = typeEvenementSportifDAO.findTypeEvenementByNomTypeEvenement(nomTypeEvenementSportif);
        if (listeTypeEvenementSportif.size() == 0) {
            TypeEvenementSportif newTypeEvenementSportif = typeEvenementSportifDAO.save(new TypeEvenementSportif(nomTypeEvenementSportif));
            evenementSportif = new EvenementSportif(newTypeEvenementSportif, equipe1, equipe2, dateEvenement, ville, coteEquipe1, coteEquipe2, coteNul);
        }
        else {
            evenementSportif = new EvenementSportif(listeTypeEvenementSportif.get(0), equipe1, equipe2, dateEvenement, ville, coteEquipe1, coteEquipe2, coteNul);
        }
        evenementSportifDAO.save(evenementSportif);
        return evenementSportif;
    }

    @Override
    public void supprimerEvenementSportif(long idEvenementSportif) throws EvenementSportifNonExistantException {
        if (evenementSportifDAO.existsById(idEvenementSportif)) {
            evenementSportifDAO.deleteById(idEvenementSportif);
        }
        else {
            throw new EvenementSportifNonExistantException();
        }
    }

    @Override
    public Page<EvenementSportif> getEvenementsSportifs(Pageable pageable) {
        return evenementSportifDAO.findAll(pageable);
    }

    @Override
    public Double getCoteResultat(long id) throws EvenementSportifNonExistantException {
        EvenementSportif evenementSportif = getEvenementSportifById(id);
        String resultat = evenementSportif.getTypeResultat();

        Double coteResultat= null;
        if (resultat.equals("Equipe1") || resultat.equals("equipe1")) {
            coteResultat = evenementSportif.getCoteEquipe1();
        }
        else if (resultat.equals("Equipe2") || resultat.equals("equipe2")) {
            coteResultat = evenementSportif.getCoteEquipe2();
        }
        else if (resultat.equals("Nul")) {
            coteResultat = evenementSportif.getCoteNul();
        }
        return coteResultat;

    }


    @Override
    public EvenementSportif modifierEvenementSportif(long idEvenementSportif,String nomTypeEvenementSportif, String equipe1, String equipe2, LocalDateTime dateEvenement, String ville, Double coteEquipe1, Double coteEquipe2, Double coteNul) throws EvenementSportifNonExistantException {
        Optional<EvenementSportif> evenementSportif = null;
        EvenementSportif evenementSportif2=null;
        if (evenementSportifDAO.existsById(idEvenementSportif)) {
            TypeEvenementSportif typeEvenementSportif = typeEvenementSportifDAO.findTypeEvenementByNomTypeEvenement(nomTypeEvenementSportif).get(0);
            evenementSportif = evenementSportifDAO.findById(idEvenementSportif);
            evenementSportif.get().setTypeEvenement(typeEvenementSportif);
            evenementSportif.get().setEquipe1(equipe1);
            evenementSportif.get().setEquipe2(equipe2);
            evenementSportif.get().setDateEvenement(dateEvenement);
            evenementSportif.get().setVille(ville);
            evenementSportif.get().setCoteEquipe1(coteEquipe1);
            evenementSportif.get().setCoteEquipe2(coteEquipe2);
            evenementSportif.get().setCoteNul(coteNul);

            evenementSportif2 = evenementSportif.get();
            evenementSportifDAO.save(evenementSportif2);
            return evenementSportif2;

        }
        else {
            throw new EvenementSportifNonExistantException();
        }
    }

    @Override
    public EvenementSportif getEvenementSportifById(long idEvenementSportif) throws EvenementSportifNonExistantException {
        Optional<EvenementSportif> evenementSportif = null;
        EvenementSportif evenementSportif2=null;
        if (evenementSportifDAO.existsById(idEvenementSportif)) {
            evenementSportif = evenementSportifDAO.findById(idEvenementSportif);
            evenementSportif2 = evenementSportif.get();
            return evenementSportif2;
        }
        else {
            throw new EvenementSportifNonExistantException();
        }

    }

    @Override
    public EvenementSportif modifierResultat(long idEvenementSportif, String typeResultat) throws EvenementSportifNonExistantException {
        Optional<EvenementSportif> evenementSportif = null;
        EvenementSportif evenementSportif2=null;
        if (evenementSportifDAO.existsById(idEvenementSportif)) {
            evenementSportif = evenementSportifDAO.findById(idEvenementSportif);
            evenementSportif.get().setTypeResultat(typeResultat);
            evenementSportif2 = evenementSportif.get();
            evenementSportifDAO.save(evenementSportif2);
            return evenementSportif2;
        }
        else {
            throw new EvenementSportifNonExistantException();
        }

    }




}