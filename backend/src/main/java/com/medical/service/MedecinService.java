package com.medical.service;

import com.medical.dto.MedecinRequest;
import com.medical.entity.Medecin;
import com.medical.exception.*;
import com.medical.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedecinService {
    private final MedecinRepository repository;
    private final VisiterRepository visiterRepository;

    public List<Medecin> findAll() { return repository.findAll(); }

    public Medecin findById(String id) {
        return repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Médecin introuvable: " + id));
    }

    public List<Medecin> search(String type, String value) {
        if (value == null || value.isBlank()) return findAll();
        String v = value.trim();
        return switch (type.toLowerCase()) {
            case "tous"    -> repository.searchAllContaining(v);
            case "nom"     -> repository.findByNomContainingIgnoreCaseOrderByNomAscPrenomAsc(v);
            case "prenom"  -> repository.findByPrenomContainingIgnoreCaseOrderByNomAscPrenomAsc(v);
            case "grade"   -> repository.findByGradeContainingIgnoreCaseOrderByNomAscPrenomAsc(v);
            default        -> throw new BusinessException("Type de recherche invalide: " + type);
        };
    }

    public Medecin create(MedecinRequest r) {
        if (repository.existsById(r.codeMed()))
            throw new BusinessException("Le code médecin existe déjà: " + r.codeMed());
        return repository.save(Medecin.builder().codeMed(r.codeMed().trim()).nom(r.nom().trim())
                .prenom(r.prenom().trim()).grade(r.grade().trim()).build());
    }

    public Medecin update(String id, MedecinRequest r) {
        Medecin m = findById(id);
        if (!id.equals(r.codeMed()) && repository.existsById(r.codeMed()))
            throw new BusinessException("Le nouveau code médecin existe déjà.");
        m.setCodeMed(r.codeMed().trim()); m.setNom(r.nom().trim());
        m.setPrenom(r.prenom().trim()); m.setGrade(r.grade().trim());
        return repository.save(m);
    }

    public void delete(String id) {
        findById(id);
        if (visiterRepository.existsByMedecin_CodeMed(id))
            throw new BusinessException("Suppression bloquée: ce médecin possède des visites.");
        repository.deleteById(id);
    }

    // ── Stats ──
    public long count() { return repository.count(); }
    public long countDistinctGrades() { return repository.countDistinctGrades(); }
    public long countMedecinsWithVisits() { return repository.countMedecinsWithVisits(); }
}