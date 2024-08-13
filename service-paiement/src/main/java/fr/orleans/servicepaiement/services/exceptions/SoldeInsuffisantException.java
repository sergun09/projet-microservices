package fr.orleans.servicepaiement.services.exceptions;

public class SoldeInsuffisantException extends Exception {

    public SoldeInsuffisantException(String message){
        super(message);
    }

    public SoldeInsuffisantException(){super("Solde Insuffisant");}

}
