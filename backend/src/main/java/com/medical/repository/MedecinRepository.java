package com.medical.repository;

import com.medical.entity.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedecinRepository extends JpaRepository<Medecin, String> {}
