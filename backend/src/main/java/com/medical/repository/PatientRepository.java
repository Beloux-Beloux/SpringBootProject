package com.medical.repository;

import com.medical.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, String> {

    List<Patient> findByNomContainingIgnoreCaseOrderByNomAscPrenomAsc(String nom);

    List<Patient> findByPrenomContainingIgnoreCaseOrderByNomAscPrenomAsc(String prenom);

    List<Patient> findBySexeContainingIgnoreCaseOrderByNomAscPrenomAsc(String sexe);

    List<Patient> findByAdresseContainingIgnoreCaseOrderByNomAscPrenomAsc(String adresse);

    @Query("SELECT p FROM Patient p WHERE p.codePat LIKE %:keyword% OR p.nom LIKE %:keyword% OR p.prenom LIKE %:keyword% OR p.sexe LIKE %:keyword% OR p.adresse LIKE %:keyword% ORDER BY p.nom")
    List<Patient> searchAllContaining(String keyword);

    long countBySexe(String sexe);
}