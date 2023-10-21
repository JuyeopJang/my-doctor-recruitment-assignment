package com.mydoctor.recruitmentassignment.patient.repository;

import com.mydoctor.recruitmentassignment.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findPatientById(Long patientId);
}
