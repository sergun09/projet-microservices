package fr.orleans.servicepaiement.dtos;

import fr.orleans.servicepaiement.models.TypeTransaction;

public record ReglementDTO(
        Long idPari,
        Double montant
) {
}
