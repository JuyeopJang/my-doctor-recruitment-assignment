package com.mydoctor.recruitmentassignment.diagnosis.entity;

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

    public Boolean isAcceptable(LocalDateTime currentTime) {
        if (currentTime.isAfter(expirationDateTime)) return false;
        return true;
    }

    public Diagnosis accept() {
        this.status = Status.ACCEPTED;
        return this;
    }

    private void setExpirationDateTime(LocalDateTime currDateTime) {
        System.out.println(currDateTime);
        if (doctor.isBeforeWorking(currDateTime)) {
            this.expirationDateTime =
                    LocalDateTime.of(currDateTime.toLocalDate(), doctor.getOperatingHour(currDateTime).getStartTime().plusMinutes(15));
            System.out.println(expirationDateTime);
        } else if (doctor.isAfterWorking(currDateTime)) {
            this.expirationDateTime =
                    LocalDateTime.of(currDateTime.toLocalDate().plusDays(1), doctor.getOperatingHour(currDateTime.plusDays(1)).getStartTime().plusMinutes(15));
            System.out.println(expirationDateTime);
        } else if (doctor.isLunchTime(currDateTime)) {
            this.expirationDateTime =
                    LocalDateTime.of(currDateTime.toLocalDate(), doctor.getOperatingHour(currDateTime).getLunchEndTime().plusMinutes(15));
            System.out.println(expirationDateTime);
        } else if (doctor.isHoliday(currDateTime)) {
            this.expirationDateTime = doctor.calculateNextWorkingDateTime(currDateTime);
            System.out.println(expirationDateTime);
        } else {
            this.expirationDateTime =
                    LocalDateTime.of(currDateTime.toLocalDate(), currDateTime.toLocalTime().plusMinutes(20));
            System.out.println(expirationDateTime);
        }
    }

    public static Diagnosis createDiagnosis(LocalDateTime desiredDateTime, Doctor doctor, Patient patient) {
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
