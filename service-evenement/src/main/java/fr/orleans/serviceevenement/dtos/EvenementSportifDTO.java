package fr.orleans.serviceevenement.dtos;

import java.time.LocalDateTime;

public record EvenementSportifDTO (

        long idEvenement,

        String nomTypeEvenement,

        String equipe1,

        String equipe2,

        LocalDateTime dateEvenement,

        String ville,

        Double coteEquipe1,

        Double coteEquipe2,

        Double coteNul,

        String typeResultat // peut etre null, nul, equipe1, equipe2

) {}
