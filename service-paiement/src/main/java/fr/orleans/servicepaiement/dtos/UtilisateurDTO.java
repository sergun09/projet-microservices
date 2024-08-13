package fr.orleans.servicepaiement.dtos;


import java.io.Serializable;

public record UtilisateurDTO(
        long idUtilisateur,
        String email,
        String nom,
        String prenom,
        String mdpUtilisateur) implements Serializable
{
}
