package com.mydoctor.recruitmentassignment.patient.entity;

import com.mydoctor.recruitmentassignment.diagnosis.entity.Diagnosis;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
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

    @Builder.Default
    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<Diagnosis> diagnoses = new ArrayList<>();
}
