package com.medical.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record VisiterRequest(
        @NotBlank String codeMed,
        @NotBlank String codePat,
        @NotNull LocalDate date) {}
