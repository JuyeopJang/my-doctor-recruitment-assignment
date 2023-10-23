package com.mydoctor.recruitmentassignment.diagnosis.controller;

import com.mydoctor.recruitmentassignment.common.exception.BusinessException;
import com.mydoctor.recruitmentassignment.common.response.CommonResponse;
import com.mydoctor.recruitmentassignment.diagnosis.controller.dto.RequestDiagnosisInput;
import com.mydoctor.recruitmentassignment.diagnosis.service.dto.DiagnosisOutput;
import com.mydoctor.recruitmentassignment.diagnosis.service.port.DiagnosisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Diagnosis", description = "진료 API")
@RequiredArgsConstructor
@RequestMapping("api/v1/diagnoses")
@RestController
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    @Operation(summary = "진료요청 검색 API", description = "의사에게 요청된 진료 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DiagnosisOutput.class))
                    )
            })
    })
    @GetMapping()
    ResponseEntity<CommonResponse<List<DiagnosisOutput>>> findDiagnosisRequests(@RequestParam @NotNull Long doctorId) {
        List<DiagnosisOutput> diagnosisOutputs = diagnosisService.getDiagnosisRequestsToDoctor(doctorId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success(diagnosisOutputs));
    }

    @Operation(summary = "진료 수락 API", description = "진료 요청을 수락합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DiagnosisOutput.class)
                    )
            }),
            @ApiResponse(responseCode = "403", description = "Fail - 진료 요청 만료 시간이 지난 경우", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BusinessException.class)
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Fail - 진료가 DB에 존재하지 않는 경우", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BusinessException.class)
                    )
            })
    })
    @PatchMapping("/{diagnosisId}")
    ResponseEntity<CommonResponse<DiagnosisOutput>> acceptDiagnosis(@PathVariable @NotNull Long diagnosisId) {
        DiagnosisOutput diagnosisOutput = diagnosisService.updateDiagnosisStatus(diagnosisId, LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success(diagnosisOutput));
    }

    @Operation(summary = "진료 요청 API", description = "환자가 의사에게 진료를 요청합니다. desiredDateTime의 경우 보여지는 예시가 아닌 yyyy-MM-dd HH:mm 다음과 같은 포맷으로 보내주세요.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success - 진료 요청 성공", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DiagnosisOutput.class)
                    )
            }),
            @ApiResponse(responseCode = "403", description = "Fail - 희망 진료 시간이 의사의 영업시간이 아닐 경우", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BusinessException.class)
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Fail - 환자, 의사가 DB에 존재하지 않는 경우", content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BusinessException.class)
                    )
            })
    })
    @PostMapping()
    ResponseEntity<CommonResponse<DiagnosisOutput>> requestDiagnosis(@RequestBody @Valid RequestDiagnosisInput requestDiagnosisInput) {
        DiagnosisOutput diagnosisOutput = diagnosisService.createDiagnosis(requestDiagnosisInput);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.success(diagnosisOutput));
    }
}
