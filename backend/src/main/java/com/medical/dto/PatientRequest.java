package com.medical.dto;

import jakarta.validation.constraints.NotBlank;

public record PatientRequest(
        @NotBlank String codePat,
        @NotBlank String nom,
        @NotBlank String prenom,
        @NotBlank String sexe,
        @NotBlank String adresse) {}
