package com.mydoctor.recruitmentassignment.patient.entity;

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
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "patient")
    private List<Diagnosis> diagnoses = new ArrayList<>();
}
