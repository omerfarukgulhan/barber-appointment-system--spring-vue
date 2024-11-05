package com.ofg.hairdresser.repository;

import com.ofg.hairdresser.model.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {
}
