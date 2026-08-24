package com.medical.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "visiter")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Visiter {
    @EmbeddedId
    private VisiterId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("codeMed")
    @JoinColumn(name = "codemed", nullable = false)
    private Medecin medecin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("codePat")
    @JoinColumn(name = "codepat", nullable = false)
    private Patient patient;
}
