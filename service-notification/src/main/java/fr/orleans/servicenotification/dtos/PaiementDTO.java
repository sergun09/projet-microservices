package fr.orleans.servicenotification.dtos;

public record PaiementDTO(
        double gain,
        long idPari,
        long idUtilisateur
) {
}
