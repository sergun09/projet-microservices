package fr.orleans.servicepaiement.services;

import fr.orleans.servicepaiement.dao.CompteDAO;
import fr.orleans.servicepaiement.models.Compte;
import fr.orleans.servicepaiement.models.Transaction;
import fr.orleans.servicepaiement.models.TypeTransaction;
import fr.orleans.servicepaiement.services.exceptions.CompteInexistantException;
import fr.orleans.servicepaiement.services.exceptions.OperationNonAutoriseeException;
import fr.orleans.servicepaiement.services.exceptions.SoldeInsuffisantException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaiementServiceImpl implements PaiementService{

    private CompteDAO compteDAO;

    public PaiementServiceImpl(CompteDAO compteDAO) {
        this.compteDAO = compteDAO;
    }

    @Override
    public Compte getCompteById(Long idCompte) throws CompteInexistantException {
        Optional<Compte> compte = compteDAO.findById(idCompte);
        if (compte.isEmpty())
            throw new CompteInexistantException();
        return compte.get();
    }

    @Override
    public Compte getCompteByIdUtilisateur(Long idUtilisateur) throws CompteInexistantException {
        Optional<Compte> compte = compteDAO.findByIdUtilisateur(idUtilisateur);
        if (compte.isEmpty())
            //le compte que l'on cherche via l'id utilisateur n'existe pas
            throw new CompteInexistantException();

        return compte.get();
    }

    @Override
    public Boolean verifierSoldeSuffisant(Long idUtilisateur, Double montant) throws CompteInexistantException {
        Compte compte = getCompteByIdUtilisateur(idUtilisateur);
        return (compte != null) ? (compte.getSolde() >= montant) : false;

    }

    @Override
    public Transaction creerTransactionCBVersCompte(Long idUtilisateur, Long idCompte, Double montant) throws CompteInexistantException, OperationNonAutoriseeException {

        Compte c = getCompteById(idCompte);
        if (c.getIdUtilisateur()!=idUtilisateur){
            throw new OperationNonAutoriseeException();
        }

        Transaction transaction = new Transaction(c.getIdCompte(),LocalDateTime.now(), montant, TypeTransaction.CB_COMPTE_PARIEUR);
        this.ajouterArgentCompte(c.getIdCompte(),montant);
        return transaction;
    }

    @Override
    public Transaction creerTransactionCompteVersCB(Long idUtilisateur, Long idCompte, Double montant) throws CompteInexistantException, SoldeInsuffisantException,OperationNonAutoriseeException{
        if (verifierSoldeSuffisant(idUtilisateur,montant)){

        Compte c = getCompteByIdUtilisateur(idUtilisateur);

        if (c.getIdUtilisateur()!=idUtilisateur){
            throw new OperationNonAutoriseeException();
        }
        Transaction transaction = new Transaction(c.getIdCompte(),LocalDateTime.now(), montant, TypeTransaction.COMPTE_PARIEUR_CB);
        this.retirerArgent(c.getIdCompte(),montant);
        return transaction;
        }else{
            throw new SoldeInsuffisantException("Solde Insuffisant");
        }
    }

    @Override
    public Transaction creerTransactionCrediterCompteGagnant(Long idParis, Long idUtilisateur, Double montant) throws CompteInexistantException {

        Compte c = getCompteByIdUtilisateur(idUtilisateur);
        Transaction transaction = new Transaction(c.getIdCompte(),LocalDateTime.now(), montant, TypeTransaction.PARI_COMPTE_PARIEUR);
        this.ajouterArgentCompte(c.getIdCompte(),montant);
        return transaction;
    }

    @Override
    public Transaction creerTransactionAnnulerPari(Long idParis, Long idUtilisateur, Double montant) throws CompteInexistantException {
        Compte c = getCompteByIdUtilisateur(idUtilisateur);
        Transaction transaction = new Transaction(c.getIdCompte(),LocalDateTime.now(), montant, TypeTransaction.PARI_ANNULE_COMPTE_PARIEUR);
        this.ajouterArgentCompte(c.getIdCompte(),montant);
        return transaction;
    }

    @Override
    public Transaction creerTransactionPayerPari(Long idParis, Long idUtilisateur, Double montant)throws SoldeInsuffisantException, CompteInexistantException{
        if (verifierSoldeSuffisant(idUtilisateur,montant)){

        Compte c = getCompteByIdUtilisateur(idUtilisateur);
        Transaction transaction = new Transaction(c.getIdCompte(),LocalDateTime.now(), montant, TypeTransaction.COMPTE_PARIEUR_PARI);
        this.retirerArgent(c.getIdCompte(),montant);
        return transaction;
        }else{
            throw new SoldeInsuffisantException("Solde Insuffisant");
        }
    }

    @Override
    public void ajouterArgentCompte(Long idCompte, Double montant) throws CompteInexistantException {
        Compte compte = getCompteById(idCompte);
        compte.setSolde(compte.getSolde() + montant);
        compteDAO.save(compte);

    }


    @Override
    public void retirerArgent(Long idCompte, Double montant) throws CompteInexistantException, SoldeInsuffisantException{
        Compte compte = getCompteById(idCompte);
        if (montant > compte.getSolde()) {
            throw new SoldeInsuffisantException() ;
        }
        compte.setSolde(compte.getSolde() - montant);
    compteDAO.save(compte);
    }

    @Override
    public Compte creeCompte(Long idUtilisateur) {
        Compte c = new Compte(idUtilisateur,0);
        return compteDAO.save(c) ;
    }

}
