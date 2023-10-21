package com.mydoctor.recruitmentassignment.diagnosis.service;

import com.mydoctor.recruitmentassignment.common.exception.BusinessException;
import com.mydoctor.recruitmentassignment.common.exception.ErrorCode;
import com.mydoctor.recruitmentassignment.diagnosis.controller.dto.RequestDiagnosisInput;
import com.mydoctor.recruitmentassignment.diagnosis.entity.Diagnosis;
import com.mydoctor.recruitmentassignment.diagnosis.repository.DiagnosisRepository;
import com.mydoctor.recruitmentassignment.diagnosis.service.dto.DiagnosisOutput;
import com.mydoctor.recruitmentassignment.diagnosis.service.port.DiagnosisService;
import com.mydoctor.recruitmentassignment.doctor.entity.Doctor;
import com.mydoctor.recruitmentassignment.doctor.repository.DoctorRepository;
import com.mydoctor.recruitmentassignment.patient.entity.Patient;
import com.mydoctor.recruitmentassignment.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DiagnosisServiceImpl implements DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Override
    public List<DiagnosisOutput> getDiagnosisRequestsToDoctor(Long doctorId) {
        Optional<List<Diagnosis>> optDiagnoses = diagnosisRepository.findAllByStatusIsApplied(doctorId);

        if (optDiagnoses.isPresent()) {
            List<Diagnosis> diagnoses = optDiagnoses.get();
            return diagnoses.stream().map(diagnosis -> DiagnosisOutput.from(diagnosis)).toList();
        }

        return null;
    }

    @Transactional
    @Override
    public DiagnosisOutput updateDiagnosisStatus(Long diagnosisId, LocalDateTime currentDateTime) {
        Diagnosis diagnosis = diagnosisRepository.findDiagnosisById(diagnosisId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND))
                .isAcceptable(currentDateTime)
                .accept();

        return DiagnosisOutput.from(diagnosisRepository.save(diagnosis));
    }

    @Transactional
    @Override
    public DiagnosisOutput createDiagnosis(RequestDiagnosisInput requestDiagnosisInput) {
        Patient patient = patientRepository.findPatientById(requestDiagnosisInput.getPatientId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        Doctor doctor = doctorRepository.findDoctorById(requestDiagnosisInput.getDoctorId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        if (doctor.isBreakTime(requestDiagnosisInput.getDesiredDateTime()))
            throw new BusinessException(ErrorCode.FORBIDDEN);

        Diagnosis diagnosis = Diagnosis.createDiagnosis(requestDiagnosisInput.getDesiredDateTime(), doctor, patient);

        Diagnosis createdDiagnosis = diagnosisRepository.save(diagnosis);

        return DiagnosisOutput.from(createdDiagnosis);
    }
}
