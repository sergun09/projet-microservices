package fr.orleans.servicepaiement.dtos;

import fr.orleans.servicepaiement.models.TypeTransaction;

public record VersementDTO(
        Long idCompte,
        Long numCarte,
        String dateExpiration,
        int cvv,
        Double montant,
        String typeTransaction
) {
}
