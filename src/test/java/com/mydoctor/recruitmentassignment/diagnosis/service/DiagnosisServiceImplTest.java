package com.mydoctor.recruitmentassignment.diagnosis.service;

import com.mydoctor.recruitmentassignment.common.exception.BusinessException;
import com.mydoctor.recruitmentassignment.common.exception.ErrorCode;
import com.mydoctor.recruitmentassignment.diagnosis.controller.dto.RequestDiagnosisInput;
import com.mydoctor.recruitmentassignment.diagnosis.repository.DiagnosisRepository;
import com.mydoctor.recruitmentassignment.doctor.entity.Doctor;
import com.mydoctor.recruitmentassignment.doctor.entity.OperatingHour;
import com.mydoctor.recruitmentassignment.doctor.repository.DoctorRepository;
import com.mydoctor.recruitmentassignment.patient.entity.Patient;
import com.mydoctor.recruitmentassignment.patient.repository.PatientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DiagnosisServiceImplTest {

    @InjectMocks
    private DiagnosisServiceImpl diagnosisService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DiagnosisRepository diagnosisRepository;

    @Nested
    @DisplayName("진료 요청")
    class CreateDiagnosis {

        @DisplayName("DB에 환자가 없는 경우 NotFoundException 예외를 Throw 한다.")
        @Test
        void createDiagnosisPatientNotFound() {
            // given
            RequestDiagnosisInput requestDiagnosisInput = RequestDiagnosisInput.builder()
                    .patientId(1L)
                    .doctorId(1L)
                    .desiredDateTime(LocalDateTime.parse("2023-10-23T14:00:00"))
                    .build();

            // when
            Mockito.lenient().doReturn(Optional.empty())
                    .when(patientRepository)
                    .findPatientById(requestDiagnosisInput.getPatientId());

            // then
            try {
                diagnosisService.createDiagnosis(requestDiagnosisInput);
            } catch (BusinessException e) {
                assertEquals(e.getErrorCode(), ErrorCode.NOT_FOUND);
                assertEquals(e.getMessage(), "존재하지 않는 환자입니다.");
            }
        }

        @DisplayName("DB에 의사가 없는 경우 BusinessException 예외를 Throw 한다.")
        @Test
        void createDiagnosisDoctorNotFound() {
            // given
            RequestDiagnosisInput requestDiagnosisInput = RequestDiagnosisInput.builder()
                    .patientId(1L)
                    .doctorId(1L)
                    .desiredDateTime(LocalDateTime.parse("2023-10-23T14:00:00"))
                    .build();

            Patient patient = Patient.builder()
                    .id(requestDiagnosisInput.getPatientId())
                    .name("홍길동")
                    .build();

            // when
            Mockito.lenient().doReturn(Optional.of(patient))
                    .when(patientRepository)
                    .findPatientById(requestDiagnosisInput.getPatientId());

            Mockito.lenient().doReturn(Optional.empty())
                    .when(doctorRepository)
                    .findDoctorById(requestDiagnosisInput.getDoctorId());

            try {
                diagnosisService.createDiagnosis(requestDiagnosisInput);
            } catch (BusinessException e) {
                assertEquals(e.getErrorCode(), ErrorCode.NOT_FOUND);
                assertEquals(e.getMessage(), "존재하지 않는 의사입니다.");
            }
        }

        @DisplayName("환자의 진료 희망 시간이 의사의 영업시간이 아닌 경우 BusinessException 예외를 Throw 한다.")
        @Test
        void createDiagnosisDoctorIsInBreakTime() {
            // given
            RequestDiagnosisInput requestDiagnosisInput = RequestDiagnosisInput.builder()
                    .patientId(1L)
                    .doctorId(1L)
                    .desiredDateTime(LocalDateTime.parse("2023-10-23T11:30:00"))
                    .build();

            List<OperatingHour> operatingHours = new ArrayList<>();

            OperatingHour operatingHour = OperatingHour.builder()
                    .startTime(LocalDateTime.parse("2023-10-23T09:00:00").toLocalTime())
                    .endTime(LocalDateTime.parse("2023-10-23T17:00:00").toLocalTime())
                    .lunchStartTime(LocalDateTime.parse("2023-10-23T11:00:00").toLocalTime())
                    .lunchEndTime(LocalDateTime.parse("2023-10-23T12:00:00").toLocalTime())
                    .dayOfWeek(DayOfWeek.MONDAY.toString())
                    .build();

            operatingHours.add(operatingHour);

            Patient patient = Patient.builder()
                    .id(requestDiagnosisInput.getPatientId())
                    .name("홍길동")
                    .build();

            Doctor doctor = Doctor.builder()
                    .operatingHours(operatingHours)
                    .build();

            // when
            Mockito.lenient().doReturn(Optional.of(patient))
                    .when(patientRepository)
                    .findPatientById(requestDiagnosisInput.getPatientId());

            Mockito.lenient().doReturn(Optional.of(doctor))
                    .when(doctorRepository)
                    .findDoctorById(requestDiagnosisInput.getDoctorId());

            // then
            try {
                diagnosisService.createDiagnosis(requestDiagnosisInput);
            } catch (BusinessException e) {
                assertEquals(e.getErrorCode(), ErrorCode.FORBIDDEN);
                assertEquals(e.getMessage(), "진료 희망시간이 의사의 영업시간이 아닙니다.");
            }
        }
    }
}