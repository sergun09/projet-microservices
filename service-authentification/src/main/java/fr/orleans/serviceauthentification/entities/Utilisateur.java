package fr.orleans.serviceauthentification.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Entity @NoArgsConstructor @AllArgsConstructor @Builder
public class Utilisateur {
    @Id @GeneratedValue
    private long idUtilisateur;
    @Column(unique=true)
    private String email;
    private String nom;
    private String prenom;
    private String mdpUtilisateur;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "idUtilisateur"))
    @Column(name = "role")
    private Set<Role> roles;

    public long getIdUtilisateur() {
        return idUtilisateur;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMdpUtilisateur() {
        return mdpUtilisateur;
    }

    public void setMdpUtilisateur(String mdpUtilisateur) {
        this.mdpUtilisateur = mdpUtilisateur;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
