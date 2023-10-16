package com.mydoctor.recruitmentassignment.doctor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.DayOfWeek;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OperatingHour {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Time startTime;

    @Column
    private Time endTime;

    @Column
    private Time lunchStartTime;

    @Column
    private Time lunchEndTime;

    @Column
    private DayOfWeek dayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}
