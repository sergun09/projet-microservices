package fr.orleans.servicepaiement.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Transaction {

    @Id @GeneratedValue
    private Long idTransaction;
    private Long idCompte;
    private LocalDateTime date;
    private double montant;
    private TypeTransaction typeTransaction;

    public Transaction(Long idCompte, LocalDateTime date, double montant, TypeTransaction typeTransaction) {
        this.idCompte = idCompte;
        this.date = date;
        this.montant = montant;
        this.typeTransaction = typeTransaction;
    }

    public Transaction() {
    }

    public Long getIdTransaction() {
        return idTransaction;
    }

    public Long getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(Long idCompte) {
        this.idCompte = idCompte;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public TypeTransaction getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(TypeTransaction typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

}
