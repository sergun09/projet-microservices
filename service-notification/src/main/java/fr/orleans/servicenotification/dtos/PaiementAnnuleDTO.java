package fr.orleans.servicenotification.dtos;

public record PaiementAnnuleDTO(
        double montant,
        long idPari,
        long idUtilisateur
) {
}
