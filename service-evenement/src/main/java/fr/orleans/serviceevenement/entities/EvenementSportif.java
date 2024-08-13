package fr.orleans.serviceevenement.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
public class EvenementSportif {

    @Id @GeneratedValue
    private long idEvenement;

    @ManyToOne
    private TypeEvenementSportif typeEvenementSportif;

    private String equipe1;

    private String equipe2;

    private LocalDateTime dateEvenement;

    private String ville;

    private Double coteEquipe1;

    private Double coteEquipe2;

    private Double coteNul;

    public String getTypeResultat() {
        return typeResultat;
    }

    public void setTypeResultat(String typeResultat) {
        this.typeResultat = typeResultat;
    }

    private String typeResultat; // peut etre null, nul, equipe1, equipe2

    public EvenementSportif() {

    }

    public EvenementSportif(TypeEvenementSportif typeEvenementSportif, String equipe1, String equipe2, LocalDateTime dateEvenement, String ville, Double coteEquipe1, Double coteEquipe2, Double coteNul) {
        this.typeEvenementSportif = typeEvenementSportif;
        this.equipe1 = equipe1;
        this.equipe2 = equipe2;
        this.dateEvenement = dateEvenement;
        this.ville = ville;
        this.coteEquipe1 = coteEquipe1;
        this.coteEquipe2 = coteEquipe2;
        this.coteNul = coteNul;
    }

    public long getIdEvenement() {
        return idEvenement;
    }

    public void setIdEvenement(long idEvenement) {
        this.idEvenement = idEvenement;
    }

    public TypeEvenementSportif getTypeEvenement() {
        return typeEvenementSportif;
    }

    public void setTypeEvenement(TypeEvenementSportif typeEvenementSportif) {
        this.typeEvenementSportif = typeEvenementSportif;
    }

    public String getEquipe1() {
        return equipe1;
    }

    public void setEquipe1(String equipe1) {
        this.equipe1 = equipe1;
    }

    public String getEquipe2() {
        return equipe2;
    }

    public void setEquipe2(String equipe2) {
        this.equipe2 = equipe2;
    }


    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Double getCoteEquipe1() {
        return coteEquipe1;
    }

    public void setCoteEquipe1(Double coteEquipe1) {
        this.coteEquipe1 = coteEquipe1;
    }

    public Double getCoteEquipe2() {
        return coteEquipe2;
    }

    public void setCoteEquipe2(Double coteEquipe2) {
        this.coteEquipe2 = coteEquipe2;
    }

    public Double getCoteNul() {
        return coteNul;
    }

    public void setCoteNul(Double coteNul) {
        this.coteNul = coteNul;
    }


    public LocalDateTime getDateEvenement() {
        return dateEvenement;
    }

    public void setDateEvenement(LocalDateTime dateEvenement) {
        this.dateEvenement = dateEvenement;
    }
}
