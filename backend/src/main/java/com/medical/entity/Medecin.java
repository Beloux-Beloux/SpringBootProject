package com.medical.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medecin")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Medecin {
    @Id
    @Column(name = "codemed", length = 30, nullable = false)
    private String codeMed;

    @Column(name = "nom", length = 100, nullable = false)
    private String nom;

    @Column(name = "prenom", length = 100, nullable = false)
    private String prenom;

    @Column(name = "grade", length = 100, nullable = false)
    private String grade;
}
