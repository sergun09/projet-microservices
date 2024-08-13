package fr.orleans.servicepaiement.dtos;

public record PaiementAnnuleDTO(
        double montant,
        long idPari,
        long idUtilisateur
) {
}
