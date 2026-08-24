package com.medical.repository;

import com.medical.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, String> {
    List<Patient> findByNomContainingIgnoreCaseOrderByNomAscPrenomAsc(String nom);
}
