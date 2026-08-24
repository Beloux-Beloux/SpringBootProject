package com.medical.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "patient")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Patient {
    @Id
    @Column(name = "codepat", length = 30, nullable = false)
    private String codePat;

    @Column(name = "nom", length = 100, nullable = false)
    private String nom;

    @Column(name = "prenom", length = 100, nullable = false)
    private String prenom;

    @Column(name = "sexe", length = 20, nullable = false)
    private String sexe;

    @Column(name = "adresse", length = 255, nullable = false)
    private String adresse;
}
