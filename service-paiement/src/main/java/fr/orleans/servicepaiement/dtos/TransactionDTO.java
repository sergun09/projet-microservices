package fr.orleans.servicepaiement.dtos;

import fr.orleans.servicepaiement.models.TypeTransaction;

import java.time.LocalDateTime;

public record TransactionDTO(
        long idUtilisateur,
        LocalDateTime date,
        double montant,
        String typeTransaction
) {
}
