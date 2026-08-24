package com.medical.repository;

import com.medical.entity.Visiter;
import com.medical.entity.VisiterId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisiterRepository extends JpaRepository<Visiter, VisiterId> {
    boolean existsByMedecin_CodeMed(String codeMed);
    boolean existsByPatient_CodePat(String codePat);
}
