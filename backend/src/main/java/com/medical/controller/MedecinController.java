package com.medical.controller;

import com.medical.dto.MedecinRequest;
import com.medical.entity.Medecin;
import com.medical.service.MedecinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/medecins")
@RequiredArgsConstructor
public class MedecinController {
    private final MedecinService service;

    @GetMapping public List<Medecin> all() { return service.findAll(); }

    @GetMapping("/search")
    public List<Medecin> search(@RequestParam String type, @RequestParam String value) {
        return service.search(type, value);
    }

    @GetMapping("/{id}") public Medecin one(@PathVariable String id) { return service.findById(id); }

    @PostMapping public ResponseEntity<Medecin> create(@Valid @RequestBody MedecinRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }

    @PutMapping("/{id}") public Medecin update(@PathVariable String id, @Valid @RequestBody MedecinRequest r) {
        return service.update(id, r);
    }

    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id); return ResponseEntity.noContent().build();
    }

    // ── Stats ──
    @GetMapping("/count") public long count() { return service.count(); }
    @GetMapping("/count/distinct-grades") public long countDistinctGrades() { return service.countDistinctGrades(); }
    @GetMapping("/count/with-visits") public long countMedecinsWithVisits() { return service.countMedecinsWithVisits(); }
}