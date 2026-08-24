package com.medical.controller;

import com.medical.dto.PatientRequest;
import com.medical.entity.Patient;
import com.medical.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService service;

    @GetMapping public List<Patient> all() { return service.findAll(); }
    @GetMapping("/search") public List<Patient> search(@RequestParam String type, @RequestParam String value) {
        return service.search(type, value);
    }
    @GetMapping("/{id}") public Patient one(@PathVariable String id) { return service.findById(id); }
    @PostMapping public ResponseEntity<Patient> create(@Valid @RequestBody PatientRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }
    @PutMapping("/{id}") public Patient update(@PathVariable String id, @Valid @RequestBody PatientRequest r) {
        return service.update(id, r);
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id); return ResponseEntity.noContent().build();
    }

    // ── Stats ──
    @GetMapping("/count") public long count() { return service.count(); }
    @GetMapping("/count/by-sexe") public long countBySexe(@RequestParam String sexe) { return service.countBySexe(sexe); }
}