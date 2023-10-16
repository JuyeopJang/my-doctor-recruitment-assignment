package com.mydoctor.recruitmentassignment.doctor.entity;

import com.mydoctor.recruitmentassignment.diagnosis.Diagnosis;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private String hospital;

    @OneToMany(mappedBy = "doctor")
    private List<Diagnosis> diagnoses = new ArrayList<>();

    @OneToMany(mappedBy = "doctor")
    private List<ClinicService> clinicServices = new ArrayList<>();

    @OneToMany(mappedBy = "doctor")
    private List<OperatingHour> operatingHours = new ArrayList<>();
}