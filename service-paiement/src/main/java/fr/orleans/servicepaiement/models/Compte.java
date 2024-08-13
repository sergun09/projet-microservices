package fr.orleans.servicepaiement.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Compte {

    @Id
    @GeneratedValue
    private Long idCompte;
    private Long idUtilisateur;
    private double solde;

    public Compte(Long idUtilisateur, double solde) {
        this.idUtilisateur = idUtilisateur;
        this.solde = solde;
    }

    public Compte() {
    }

    public Long getIdCompte() {
        return idCompte;
    }


    public Long getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Long idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

}
