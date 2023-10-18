package com.mydoctor.recruitmentassignment.diagnosis.service.dto;

import com.mydoctor.recruitmentassignment.diagnosis.entity.Diagnosis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosisOutput {

    private Long diagnosisId;
    private String patientName;
    private LocalDateTime desiredAppointmentDateTime;
    private LocalDateTime expiredAppointmentDateTime;

    public static DiagnosisOutput from(Diagnosis diagnosis) {
        DiagnosisOutput diagnosisOutput = DiagnosisOutput.builder()
                .diagnosisId(diagnosis.getId())
                .patientName(diagnosis.getPatient().getName())
                .desiredAppointmentDateTime(diagnosis.getRequestedDateTime())
                .expiredAppointmentDateTime(diagnosis.getExpirationDateTime())
                .build();

        return diagnosisOutput;
    }
}
