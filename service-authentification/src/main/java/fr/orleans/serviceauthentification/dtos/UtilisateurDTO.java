package fr.orleans.serviceauthentification.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record UtilisateurDTO  (
        long idUtilisateur,
        @Email(message = "format email invalide")
        @NotNull
        String email,
        String nom,
        String prenom,
        String mdpUtilisateur) implements Serializable
{
}
