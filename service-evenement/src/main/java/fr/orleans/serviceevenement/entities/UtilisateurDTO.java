package fr.orleans.serviceevenement.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record UtilisateurDTO(
        long idUtilisateur,
        String email,
        String nom,
        String prenom,
        String mdpUtilisateur) implements Serializable
{
}
