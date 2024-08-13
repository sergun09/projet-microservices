package fr.orleans.serviceauthentification.service.exceptions;

public class EmailExistantException extends Exception {
    public EmailExistantException(String message){
        super(message);

    }
    public EmailExistantException(){
        super("Email déjà existant");
    }

}
