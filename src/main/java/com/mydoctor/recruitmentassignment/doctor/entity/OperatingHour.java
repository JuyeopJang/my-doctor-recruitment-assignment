package com.mydoctor.recruitmentassignment.doctor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Builder
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
    private String dayOfWeek;

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
        if (lunchStartTime == null || lunchEndTime == null) return false;
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

    public Integer getPriority() {
        if (dayOfWeek.equals(DayOfWeek.MONDAY.toString())) return Integer.valueOf(1);
        if (dayOfWeek.equals(DayOfWeek.TUESDAY.toString())) return Integer.valueOf(2);
        if (dayOfWeek.equals(DayOfWeek.WEDNESDAY.toString())) return Integer.valueOf(3);
        if (dayOfWeek.equals(DayOfWeek.THURSDAY.toString())) return Integer.valueOf(4);
        if (dayOfWeek.equals(DayOfWeek.FRIDAY.toString())) return Integer.valueOf(5);
        if (dayOfWeek.equals(DayOfWeek.SATURDAY.toString())) return Integer.valueOf(6);
        return Integer.valueOf(7);
    }
}
