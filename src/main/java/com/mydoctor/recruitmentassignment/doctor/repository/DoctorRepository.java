package com.mydoctor.recruitmentassignment.doctor.repository;

import com.mydoctor.recruitmentassignment.doctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findDoctorById(Long doctorId);
}
