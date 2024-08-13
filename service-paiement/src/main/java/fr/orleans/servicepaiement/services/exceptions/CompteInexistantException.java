package fr.orleans.servicepaiement.services.exceptions;

public class CompteInexistantException extends Exception {

    public CompteInexistantException(String message) {
        super(message);
    }

    public CompteInexistantException() {
        super("Compte inexistant");
    }
}

