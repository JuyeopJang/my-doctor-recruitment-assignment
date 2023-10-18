package com.mydoctor.recruitmentassignment.diagnosis.repository;

import com.mydoctor.recruitmentassignment.diagnosis.entity.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    Optional<Diagnosis> findDiagnosisById(Long diagnosisId);

    @Query("select d from Diagnosis d "
        + "join fetch d.doctor "
        + "join fetch d.patient where d.id = :doctorId and d.status = 'APPLIED'"
    )
    Optional<List<Diagnosis>> findAllByStatusIsApplied(@Param("doctorId") Long doctorId);
}
