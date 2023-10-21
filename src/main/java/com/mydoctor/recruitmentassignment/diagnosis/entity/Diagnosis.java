package com.mydoctor.recruitmentassignment.diagnosis.entity;

import com.mydoctor.recruitmentassignment.common.exception.BusinessException;
import com.mydoctor.recruitmentassignment.common.exception.ErrorCode;
import com.mydoctor.recruitmentassignment.doctor.entity.Doctor;
import com.mydoctor.recruitmentassignment.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private LocalDateTime desiredDateTime;

    @Column
    private LocalDateTime expirationDateTime;

    @Enumerated(EnumType.STRING)
    @Column
    private Status status;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public Diagnosis isAcceptable(LocalDateTime currentTime) {
        if (currentTime.isAfter(expirationDateTime)) throw new BusinessException(ErrorCode.UNAUTHORIZED);
        return this;
    }

    public Diagnosis accept() {
        this.status = Status.ACCEPTED;
        return this;
    }

    private void setExpirationDateTime(LocalDateTime currDateTime) {
        // 월 ~ 목 출근전
        if (doctor.isBeforeWorking(currDateTime))
            this.expirationDateTime =
                    LocalDateTime.of(currDateTime.toLocalDate(), doctor.getOperatingHour(currDateTime).getStartTime().plusMinutes(15));

        // 월 ~ 목 출근후
        if (doctor.isAfterWorking(currDateTime))
            this.expirationDateTime =
                    LocalDateTime.of(currDateTime.toLocalDate().plusDays(1), doctor.getOperatingHour(currDateTime.plusDays(1)).getStartTime().plusMinutes(15));

        // 영업일 당일 점심
        if (doctor.isLunchTime(currDateTime))
            this.expirationDateTime =
                    LocalDateTime.of(currDateTime.toLocalDate(), doctor.getOperatingHour(currDateTime).getLunchEndTime().plusMinutes(15));

        // 마지막 출근일 퇴근이후 ~ 영업일에 포함되지 않는 휴일
        if (doctor.isHoliday(currDateTime))
            this.expirationDateTime = doctor.calculateNextWorkingDateTime(currDateTime);
    }

    public static Diagnosis createDiagnosis(LocalDateTime desiredDateTime, Doctor doctor, Patient patient) {
        // default +20 minute, else

        Diagnosis diagnosis = Diagnosis.builder()
                .desiredDateTime(desiredDateTime)
                .status(Status.APPLIED)
                .doctor(doctor)
                .patient(patient)
                .build();

        diagnosis.setExpirationDateTime(LocalDateTime.now());

        return diagnosis;
    }
}
