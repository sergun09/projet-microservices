package fr.orleans.serviceauthentification.service.exceptions;

public class UtilisateurInexistantException extends Exception {
    public UtilisateurInexistantException(String message){
        super(message);

    }
    public UtilisateurInexistantException(){
        super("Utilisateur inexistant");
    }
}
