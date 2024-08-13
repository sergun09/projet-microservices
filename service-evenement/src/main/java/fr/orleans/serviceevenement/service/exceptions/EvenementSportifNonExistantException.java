package fr.orleans.serviceevenement.service.exceptions;

public class EvenementSportifNonExistantException extends Exception {


    public EvenementSportifNonExistantException(String message){
        super(message);
    }
    public EvenementSportifNonExistantException(){
        super("Evenement Sportif non existant");
    }
}
