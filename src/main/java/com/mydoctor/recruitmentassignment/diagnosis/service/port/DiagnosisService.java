package com.mydoctor.recruitmentassignment.diagnosis.service.port;

import com.mydoctor.recruitmentassignment.diagnosis.service.dto.DiagnosisOutput;

import java.time.LocalDateTime;
import java.util.List;

public interface DiagnosisService {

    List<DiagnosisOutput> getDiagnosisRequestsToDoctor(Long doctorId);

    DiagnosisOutput updateDiagnosisStatus(Long diagnosisId, LocalDateTime currentDateTime);
}
