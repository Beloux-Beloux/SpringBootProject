package com.medical.service;

import com.medical.dto.*;
import com.medical.entity.*;
import com.medical.exception.*;
import com.medical.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisiterService {
    private final VisiterRepository repository;
    private final MedecinRepository medecinRepository;
    private final PatientRepository patientRepository;

    public List<VisiterResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public VisiterResponse findById(VisiterId id) {
        return toResponse(repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Visite introuvable.")));
    }

    public VisiterResponse create(VisiterRequest r) {
        VisiterId id = new VisiterId(r.codeMed().trim(), r.codePat().trim(), r.date());
        if (repository.existsById(id)) throw new BusinessException("Cette visite existe déjà.");
        Medecin m = medecinRepository.findById(id.getCodeMed()).orElseThrow(() ->
                new ResourceNotFoundException("Médecin introuvable: " + id.getCodeMed()));
        Patient p = patientRepository.findById(id.getCodePat()).orElseThrow(() ->
                new ResourceNotFoundException("Patient introuvable: " + id.getCodePat()));
        return toResponse(repository.save(Visiter.builder().id(id).medecin(m).patient(p).build()));
    }

    public VisiterResponse update(VisiterId oldId, VisiterRequest r) {
        repository.findById(oldId).orElseThrow(() -> new ResourceNotFoundException("Visite introuvable."));
        VisiterId newId = new VisiterId(r.codeMed().trim(), r.codePat().trim(), r.date());
        if (!oldId.equals(newId) && repository.existsById(newId))
            throw new BusinessException("La nouvelle visite existe déjà.");
        Medecin m = medecinRepository.findById(newId.getCodeMed()).orElseThrow(() ->
                new ResourceNotFoundException("Médecin introuvable: " + newId.getCodeMed()));
        Patient p = patientRepository.findById(newId.getCodePat()).orElseThrow(() ->
                new ResourceNotFoundException("Patient introuvable: " + newId.getCodePat()));
        if (!oldId.equals(newId)) repository.deleteById(oldId);
        return toResponse(repository.save(Visiter.builder().id(newId).medecin(m).patient(p).build()));
    }

    public void delete(VisiterId id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Visite introuvable.");
        repository.deleteById(id);
    }

    private VisiterResponse toResponse(Visiter v) {
        return new VisiterResponse(v.getId().getCodeMed(), v.getId().getCodePat(), v.getId().getDate(),
                v.getMedecin().getNom() + " " + v.getMedecin().getPrenom(),
                v.getPatient().getNom() + " " + v.getPatient().getPrenom());
    }
}
