package fr.orleans.servicenotification.entities;

public enum TypeTransaction {
    CB_COMPTE_PARIEUR,
    COMPTE_PARIEUR_CB,
    COMPTE_PARIEUR_PARI,

    //credit du compte parieur pour un pari gagné
    PARI_COMPTE_PARIEUR,

    //credit du compte parieur pour un pari annulé
    PARI_ANNULE_COMPTE_PARIEUR

}
