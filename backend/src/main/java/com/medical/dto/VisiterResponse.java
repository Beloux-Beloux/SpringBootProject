package com.medical.dto;

import java.time.LocalDate;

public record VisiterResponse(String codeMed, String codePat, LocalDate date,
                               String medecinNom, String patientNom) {}
