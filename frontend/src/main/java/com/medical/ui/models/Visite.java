package com.medical.ui.models;
import java.time.LocalDate;
public record Visite(String codeMed, String codePat, LocalDate date, String medecinNom, String patientNom) {}
