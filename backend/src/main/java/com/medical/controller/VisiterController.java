package com.medical.controller;

import com.medical.dto.*;
import com.medical.entity.VisiterId;
import com.medical.service.VisiterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/visites")
@RequiredArgsConstructor
public class VisiterController {
    private final VisiterService service;

    @GetMapping public List<VisiterResponse> all() { return service.findAll(); }

    @GetMapping("/{codemed}/{codepat}/{date}")
    public VisiterResponse one(@PathVariable String codemed, @PathVariable String codepat,
                               @PathVariable LocalDate date) {
        return service.findById(new VisiterId(codemed, codepat, date));
    }

    @PostMapping public ResponseEntity<VisiterResponse> create(@Valid @RequestBody VisiterRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }

    @PutMapping("/{codemed}/{codepat}/{date}")
    public VisiterResponse update(@PathVariable String codemed, @PathVariable String codepat,
                                  @PathVariable LocalDate date, @Valid @RequestBody VisiterRequest r) {
        return service.update(new VisiterId(codemed, codepat, date), r);
    }

    @DeleteMapping("/{codemed}/{codepat}/{date}")
    public ResponseEntity<Void> delete(@PathVariable String codemed, @PathVariable String codepat,
                                       @PathVariable LocalDate date) {
        service.delete(new VisiterId(codemed, codepat, date));
        return ResponseEntity.noContent().build();
    }
}
