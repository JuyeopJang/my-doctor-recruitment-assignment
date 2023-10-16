package com.mydoctor.recruitmentassignment.diagnosis;

public enum Status {
    APPLIED("진료 신청"),
    ACCEPTED("진료 수락"),
    IN_PROGRESS("진료 중"),
    COMPLETED("진료 완료");

    private String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
