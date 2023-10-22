package com.mydoctor.recruitmentassignment.diagnosis.repository;

import com.mydoctor.recruitmentassignment.diagnosis.entity.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    Optional<Diagnosis> findDiagnosisById(Long diagnosisId);

    @Query("select ds from Diagnosis ds "
        + "join fetch ds.doctor "
        + "join fetch ds.patient where ds.doctor.id = :doctorId and ds.status = 'APPLIED'"
    )
    Optional<List<Diagnosis>> findAllByStatusIsApplied(@Param("doctorId") Long doctorId);
}
