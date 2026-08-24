package com.medical.service;

import com.medical.dto.PatientRequest;
import com.medical.entity.Patient;
import com.medical.exception.*;
import com.medical.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository repository;
    private final VisiterRepository visiterRepository;

    public List<Patient> findAll() { return repository.findAll(); }

    public List<Patient> search(String type, String value) {
        if (value == null || value.isBlank()) return findAll();
        if ("code".equalsIgnoreCase(type)) {
            return repository.findById(value.trim()).map(List::of).orElse(List.of());
        }
        if ("nom".equalsIgnoreCase(type))
            return repository.findByNomContainingIgnoreCaseOrderByNomAscPrenomAsc(value.trim());
        throw new BusinessException("Type de recherche invalide. Utilisez code ou nom.");
    }

    public Patient findById(String id) {
        return repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Patient introuvable: " + id));
    }

    public Patient create(PatientRequest r) {
        if (repository.existsById(r.codePat()))
            throw new BusinessException("Le code patient existe déjà: " + r.codePat());
        return repository.save(Patient.builder().codePat(r.codePat().trim()).nom(r.nom().trim())
                .prenom(r.prenom().trim()).sexe(r.sexe().trim()).adresse(r.adresse().trim()).build());
    }

    public Patient update(String id, PatientRequest r) {
        Patient p = findById(id);
        if (!id.equals(r.codePat()) && repository.existsById(r.codePat()))
            throw new BusinessException("Le nouveau code patient existe déjà.");
        p.setCodePat(r.codePat().trim()); p.setNom(r.nom().trim());
        p.setPrenom(r.prenom().trim()); p.setSexe(r.sexe().trim()); p.setAdresse(r.adresse().trim());
        return repository.save(p);
    }

    public void delete(String id) {
        findById(id);
        if (visiterRepository.existsByPatient_CodePat(id))
            throw new BusinessException("Suppression bloquée: ce patient possède des visites.");
        repository.deleteById(id);
    }
}
