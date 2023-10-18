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
import org.springframework.cglib.core.Local;

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
    private LocalDateTime requestedDateTime;

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
}
