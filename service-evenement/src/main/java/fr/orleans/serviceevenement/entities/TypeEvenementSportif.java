package fr.orleans.serviceevenement.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class TypeEvenementSportif {

    @Id @GeneratedValue
    private long idTypeEvenement;
    private String nomTypeEvenement;

    @OneToMany
    private List<EvenementSportif> listeEvenementSportif;

    public long getIdTypeEvenement() {
        return idTypeEvenement;
    }

    public String getNomTypeEvenement() {
        return nomTypeEvenement;
    }

    public void setNomTypeEvenement(String nomTypeEvenement) {
        this.nomTypeEvenement = nomTypeEvenement;
    }

    public TypeEvenementSportif(String nomTypeEvenement) {
        this.nomTypeEvenement = nomTypeEvenement;
    }

    public TypeEvenementSportif() {
    }
}
