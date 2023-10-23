package com.mydoctor.recruitmentassignment.diagnosis.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestDiagnosisInput {

    @Schema(description = "환자 ID", example = "1")
    @NotNull
    private Long patientId;
    @Schema(description = "의사 ID", example = "1")
    @NotNull
    private Long doctorId;
    @NotNull
    @Schema(description = "진료 희망 시간 - 보여지는 예시가 아닌 yyyy-MM-dd HH:mm 다음과 같은 포맷으로 보내주세요.", example = "2023-10-10 17:00", format = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime desiredDateTime;
}
