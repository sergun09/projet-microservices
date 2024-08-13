package fr.orleans.servicenotification.service.exceptions;

public class NotificationInexistanteException extends Exception {
    public NotificationInexistanteException(String message){
        super(message);

    }
    public NotificationInexistanteException(){
        super("Notification inexistante");
    }
}
