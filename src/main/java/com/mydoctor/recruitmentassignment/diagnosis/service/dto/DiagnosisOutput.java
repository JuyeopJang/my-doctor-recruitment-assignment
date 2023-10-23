package com.mydoctor.recruitmentassignment.diagnosis.service.dto;

import com.mydoctor.recruitmentassignment.diagnosis.entity.Diagnosis;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@ApiResponse
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosisOutput {

    @Schema(description = "진료 ID", example = "1")
    private Long diagnosisId;
    @Schema(description = "환자 이름", example = "홍길동")
    private String patientName;
    @Schema(description = "진료 희망 시간", example = "2023-10-10T17:00:00")
    private LocalDateTime desiredAppointmentDateTime;
    @Schema(description = "진료 요청 만료 시간", example = "2023-10-10T17:00:20")
    private LocalDateTime expiredAppointmentDateTime;

    public static DiagnosisOutput from(Diagnosis diagnosis) {
        DiagnosisOutput diagnosisOutput = DiagnosisOutput.builder()
                .diagnosisId(diagnosis.getId())
                .patientName(diagnosis.getPatient().getName())
                .desiredAppointmentDateTime(diagnosis.getDesiredDateTime())
                .expiredAppointmentDateTime(diagnosis.getExpirationDateTime())
                .build();

        return diagnosisOutput;
    }
}
