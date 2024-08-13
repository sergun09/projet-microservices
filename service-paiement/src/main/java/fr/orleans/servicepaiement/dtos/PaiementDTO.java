package fr.orleans.servicepaiement.dtos;

public record PaiementDTO(
        double gain,
        long idPari,
        long idUtilisateur
) {
}
