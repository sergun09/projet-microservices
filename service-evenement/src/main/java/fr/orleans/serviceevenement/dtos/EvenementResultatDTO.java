package fr.orleans.serviceevenement.dtos;

public record EvenementResultatDTO (
    long idEvenement,
    String typeResultat,// peut etre null, nul, equipe1, equipe2
    Double coteResultat
) {}
