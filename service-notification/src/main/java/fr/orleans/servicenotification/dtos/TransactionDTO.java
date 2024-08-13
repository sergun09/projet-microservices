package fr.orleans.servicenotification.dtos;

import java.time.LocalDateTime;

public record TransactionDTO(
        long idUtilisateur,
        LocalDateTime date,
        double montant,
        String typeTransaction
) {
}
