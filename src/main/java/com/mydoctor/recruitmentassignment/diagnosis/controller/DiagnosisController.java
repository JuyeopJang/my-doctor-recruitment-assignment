package com.mydoctor.recruitmentassignment.diagnosis.controller;

import com.mydoctor.recruitmentassignment.common.response.CommonResponse;
import com.mydoctor.recruitmentassignment.diagnosis.controller.dto.RequestDiagnosisInput;
import com.mydoctor.recruitmentassignment.diagnosis.service.dto.DiagnosisOutput;
import com.mydoctor.recruitmentassignment.diagnosis.service.port.DiagnosisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/v1/diagnoses")
@RestController
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    @GetMapping()
    ResponseEntity<CommonResponse<List<DiagnosisOutput>>> findDiagnosisRequests(@RequestParam Long doctorId) {
        List<DiagnosisOutput> diagnosisOutputs = diagnosisService.getDiagnosisRequestsToDoctor(doctorId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success(diagnosisOutputs));
    }

    @PatchMapping("/{diagnosisId}")
    ResponseEntity<CommonResponse<DiagnosisOutput>> acceptDiagnosis(@PathVariable Long diagnosisId) {
        DiagnosisOutput diagnosisOutput = diagnosisService.updateDiagnosisStatus(diagnosisId, LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success(diagnosisOutput));
    }

    @PostMapping()
    ResponseEntity<CommonResponse<DiagnosisOutput>> requestDiagnosis(@RequestBody RequestDiagnosisInput requestDiagnosisInput) {
        DiagnosisOutput diagnosisOutput = diagnosisService.createDiagnosis(requestDiagnosisInput);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success(diagnosisOutput));
    }
}
