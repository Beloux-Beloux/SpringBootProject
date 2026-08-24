package com.medical.repository;

import com.medical.entity.Visiter;
import com.medical.entity.VisiterId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface VisiterRepository extends JpaRepository<Visiter, VisiterId> {

    boolean existsByMedecin_CodeMed(String codeMed);
    boolean existsByPatient_CodePat(String codePat);

    @Query("SELECT v FROM Visiter v JOIN FETCH v.medecin m JOIN FETCH v.patient p WHERE v.id.codeMed LIKE %:keyword%")
    List<Visiter> findByCodeMedContaining(String keyword);

    @Query("SELECT v FROM Visiter v JOIN FETCH v.medecin m JOIN FETCH v.patient p WHERE v.id.codePat LIKE %:keyword%")
    List<Visiter> findByCodePatContaining(String keyword);

    @Query("SELECT v FROM Visiter v JOIN FETCH v.medecin m JOIN FETCH v.patient p WHERE CAST(v.id.date AS string) LIKE %:keyword%")
    List<Visiter> findByDateContaining(String keyword);

    @Query("SELECT v FROM Visiter v JOIN FETCH v.medecin m JOIN FETCH v.patient p WHERE LOWER(m.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(m.prenom) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Visiter> findByMedecinNomContaining(String keyword);

    @Query("SELECT v FROM Visiter v JOIN FETCH v.medecin m JOIN FETCH v.patient p WHERE LOWER(p.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.prenom) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Visiter> findByPatientNomContaining(String keyword);

    @Query(value = "SELECT COUNT(*) FROM visiter WHERE MONTH(date) = MONTH(CURDATE()) AND YEAR(date) = YEAR(CURDATE())", nativeQuery = true)
    long countCurrentMonth();
}