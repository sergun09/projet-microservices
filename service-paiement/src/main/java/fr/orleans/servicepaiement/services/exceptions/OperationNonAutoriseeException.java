package fr.orleans.servicepaiement.services.exceptions;

public class OperationNonAutoriseeException extends Exception {

    public OperationNonAutoriseeException(String message){
        super(message);
    }

    public OperationNonAutoriseeException(){super("Operation non autorisee");}

}
