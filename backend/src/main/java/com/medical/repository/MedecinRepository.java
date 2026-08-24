package com.medical.repository;

import com.medical.entity.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MedecinRepository extends JpaRepository<Medecin, String> {

    List<Medecin> findByNomContainingIgnoreCaseOrderByNomAscPrenomAsc(String nom);

    List<Medecin> findByPrenomContainingIgnoreCaseOrderByNomAscPrenomAsc(String prenom);

    List<Medecin> findByGradeContainingIgnoreCaseOrderByNomAscPrenomAsc(String grade);

    @Query("SELECT m FROM Medecin m WHERE m.codeMed LIKE %:keyword% OR m.nom LIKE %:keyword% OR m.prenom LIKE %:keyword% OR m.grade LIKE %:keyword% ORDER BY m.nom")
    List<Medecin> searchAllContaining(String keyword);

    @Query("SELECT COUNT(DISTINCT m.grade) FROM Medecin m")
    long countDistinctGrades();

    @Query("SELECT COUNT(DISTINCT m) FROM Medecin m WHERE EXISTS (SELECT 1 FROM Visiter v WHERE v.id.codeMed = m.codeMed)")
    long countMedecinsWithVisits();
}