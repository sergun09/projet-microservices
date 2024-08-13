package fr.orleans.serviceevenement.dtos;

import java.time.LocalDateTime;

public record EvenementCreationDTO (
    long idEvenement,
    LocalDateTime dateEvenement
) {}
