package com.medical.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class VisiterId implements Serializable {
    private String codeMed;
    private String codePat;
    private LocalDate date;
}
