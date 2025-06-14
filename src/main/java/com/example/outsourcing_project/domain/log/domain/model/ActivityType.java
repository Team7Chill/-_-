package com.example.outsourcing_project.domain.log.domain.model;

public enum ActivityType {

    TASK_CREATED("TASK_CREATED"),
    TASK_UPDATED("TASK_UPDATED"),
    TASK_DELETED("TASK_DELETED"),
    TASK_STATUS_CHANGED("TASK_STATUS_CHANGED"),
    COMMENT_CREATED("COMMENT_CREATED"),
    COMMENT_UPDATED("COMMENT_UPDATED"),
    COMMENT_DELETED("COMMENT_DELETED"),
    USER_LOGGED_IN("USER_LOGGED_IN"),
    USER_LOGGED_OUT("USER_LOGGED_OUT");

    private final String value;

    // 생성자
    ActivityType(String value) {
        this.value = value;
    }
    // value 값을 반환하는 메서드
    public String getValue() {
        return value;
    }

}
