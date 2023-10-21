package com.mydoctor.recruitmentassignment.doctor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OperatingHour {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private LocalTime startTime;

    @Column
    private LocalTime endTime;

    @Column
    private LocalTime lunchStartTime;

    @Column
    private LocalTime lunchEndTime;

    @Column
    private DayOfWeek dayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    public Boolean isBeforeWorking(LocalTime currTime) {
        if (currTime.isBefore(startTime)) return true;
        return false;
    }

    public Boolean isAfterWorking(LocalTime currTime) {
        if (currTime.isAfter(endTime)) return true;
        return false;
    }

    public Boolean isLunch(LocalTime currTime) {
        if (currTime.isBefore(lunchStartTime)) return false;
        if (currTime.isAfter(lunchEndTime)) return false;
        return true;
    }

    public Boolean isWorking(LocalTime currTime) {
        if (isBeforeWorking(currTime)) return false;
        if (isAfterWorking(currTime)) return false;
        if (isLunch(currTime)) return false;
        return true;
    }
}
