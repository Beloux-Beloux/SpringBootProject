package com.medical.dto;

import jakarta.validation.constraints.NotBlank;

public record MedecinRequest(
        @NotBlank String codeMed,
        @NotBlank String nom,
        @NotBlank String prenom,
        @NotBlank String grade) {}
