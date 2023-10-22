package com.mydoctor.recruitmentassignment.doctor.entity;

import com.mydoctor.recruitmentassignment.diagnosis.entity.Diagnosis;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
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

    @Builder.Default()
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<Diagnosis> diagnoses = new ArrayList<>();

    @Builder.Default()
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<ClinicService> clinicServices = new ArrayList<>();

    @Builder.Default()
    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<OperatingHour> operatingHours = new ArrayList<>();

    public OperatingHour getOperatingHour(LocalDateTime currDateTime) {
        String currDay = currDateTime.getDayOfWeek().toString();

        // filter doctor's operating hours from patient's desiredAppointmentDateTime
        List<OperatingHour> doctorOperatingHours = operatingHours
                .stream()
                .filter(operatingHour -> operatingHour.getDayOfWeek().equals(currDay))
                .toList();

        if (doctorOperatingHours.isEmpty()) return null;
        return doctorOperatingHours.get(0);
    }

    public Boolean isBeforeWorking(LocalDateTime currDateTime) {
        OperatingHour operatingHour = getOperatingHour(currDateTime);
        if (operatingHour != null && operatingHour.isBeforeWorking(currDateTime.toLocalTime())) return true;
        return false;
    }

    public Boolean isAfterWorking(LocalDateTime currDateTime) {
        OperatingHour operatingHour = getOperatingHour(currDateTime);
        OperatingHour lastOperatingHour = getLastOperatingHour();

        if (operatingHour != null && !operatingHour.getDayOfWeek().equals(lastOperatingHour.getDayOfWeek()) && operatingHour.isAfterWorking(currDateTime.toLocalTime())) return true;
        return false;
    }

    public Boolean isLunchTime(LocalDateTime currDateTime) {
        OperatingHour operatingHour = getOperatingHour(currDateTime);
        if (operatingHour != null && operatingHour.isLunch(currDateTime.toLocalTime())) return true;
        return false;
    }

    public Boolean isHoliday(LocalDateTime currDateTime) {
        OperatingHour operatingHour = getOperatingHour(currDateTime);
        OperatingHour lastOperatingHour = getLastOperatingHour();

        if (operatingHour == null || (operatingHour.getDayOfWeek().equals(lastOperatingHour.getDayOfWeek()) && operatingHour.isAfterWorking(currDateTime.toLocalTime()))) return true;
        return false;
    }

    public Boolean isBreakTime(LocalDateTime currDateTime) {
        if (isBeforeWorking(currDateTime) || isAfterWorking(currDateTime) || isLunchTime(currDateTime) || isHoliday(currDateTime)) return true;
        return false;
    }

    public OperatingHour getLastOperatingHour() {
        Comparator<OperatingHour> operatingHourComparator = Comparator.comparing(OperatingHour::getPriority);
        operatingHours.sort(operatingHourComparator);
        return operatingHours.get(operatingHours.size() - 1);
    }

    public LocalDateTime calculateNextWorkingDateTime(LocalDateTime currDateTime) {
        Comparator<OperatingHour> operatingHourComparator = Comparator.comparing(OperatingHour::getPriority);
        operatingHours.sort(operatingHourComparator);
        OperatingHour operatingHour = operatingHours.get(0);

        DayOfWeek nextWorkingDay = DayOfWeek.valueOf(operatingHour.getDayOfWeek());

        LocalDate nextWorkingDate = currDateTime.plusDays((7 - currDateTime.getDayOfWeek().getValue() + nextWorkingDay.getValue()) % 7).toLocalDate();
        LocalTime nextWorkingTime = operatingHour.getStartTime().plusMinutes(15);

        return LocalDateTime.of(nextWorkingDate, nextWorkingTime);
    }
}
