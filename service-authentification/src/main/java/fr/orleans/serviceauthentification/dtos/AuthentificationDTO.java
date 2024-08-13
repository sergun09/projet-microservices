package fr.orleans.serviceauthentification.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record AuthentificationDTO (
        @Email(message = "format email invalide")
        @NotNull
        String email,
        String mdpUtilisateur
        )

{
}
