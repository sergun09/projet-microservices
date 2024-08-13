package fr.orleans.servicepaiement.services;

import fr.orleans.servicepaiement.models.Compte;
import fr.orleans.servicepaiement.models.Transaction;
import fr.orleans.servicepaiement.services.exceptions.CompteInexistantException;
import fr.orleans.servicepaiement.services.exceptions.OperationNonAutoriseeException;
import fr.orleans.servicepaiement.services.exceptions.SoldeInsuffisantException;

public interface PaiementService {

    /**
     * @param idUtilisateur
     * @param montant
     * @return
     * @throws CompteInexistantException
     */
    Boolean verifierSoldeSuffisant(Long idUtilisateur, Double montant)throws CompteInexistantException;

    Transaction creerTransactionCBVersCompte(Long idUtilisateur,Long idCompte, Double montant) throws CompteInexistantException, OperationNonAutoriseeException;
    Transaction creerTransactionCompteVersCB(Long idUtilisateur,Long idCompte, Double montant) throws CompteInexistantException, SoldeInsuffisantException,OperationNonAutoriseeException;


    Transaction creerTransactionPayerPari(Long idParis, Long idUtilisateur, Double montant) throws SoldeInsuffisantException, CompteInexistantException;
    Transaction creerTransactionAnnulerPari(Long idParis, Long idUtilisateur, Double montant)throws CompteInexistantException;
    Transaction creerTransactionCrediterCompteGagnant(Long idParis, Long idUtilisateur, Double montant) throws CompteInexistantException;


    /**
     * Ajout du montant sur le compte d'un parieur
     * @param idCompte
     * @param montant
     * @return
     */
    void ajouterArgentCompte(Long idCompte, Double montant) throws CompteInexistantException;


    /**
     * @param idCompte
     * @param montant
     * @throws CompteInexistantException
     * @throws SoldeInsuffisantException
     */
    void retirerArgent(Long idCompte, Double montant) throws CompteInexistantException, SoldeInsuffisantException;

    /**
     * Renvoie un compte à partir de son idCompte
     * @param idCompte
     * @return
     * @throws CompteInexistantException
     */
    Compte getCompteById(Long idCompte)throws CompteInexistantException;

    /**
     * * Renvoie un compte à partir de son idUtilisateur
     * @param idUtilisateur
     * @return
     * @throws CompteInexistantException
     */
    Compte getCompteByIdUtilisateur(Long idUtilisateur)throws CompteInexistantException;

    Compte creeCompte(Long idUtilisateur);

}
