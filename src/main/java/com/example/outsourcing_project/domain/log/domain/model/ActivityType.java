package com.example.outsourcing_project.domain.log.domain.model;

public enum ActivityType {

    TASK_CREATED("TASK_CREATED", "새로운 작업이 생성되었습니다."),
    TASK_UPDATED("TASK_UPDATED", "작업이 변경되었습니다."),
    TASK_DELETED("TASK_DELETED", "작업이 삭제되었습니다."),
    TASK_STATUS_CHANGED("TASK_STATUS_CHANGED", "작업 상태가 변경되었습니다."),
    COMMENT_CREATED("COMMENT_CREATED", "새로운 댓글이 작성되었습니다."),
    COMMENT_UPDATED("COMMENT_UPDATED", "댓글이 수정되었습니다."),
    COMMENT_DELETED("COMMENT_DELETED", "댓글이 삭제되었습니다."),
    USER_LOGGED_IN("USER_LOGGED_IN", "로그인 되었습니다."),
    USER_LOGGED_OUT("USER_LOGGED_OUT", "로그아웃 되었습니다.");

    private final String type;
    private final String contents;

    // 생성자
    ActivityType(String type, String contents) {
        this.type = type;
        this.contents = contents;
    }
    public String getType() {
        return type;
    }

    public String getContents() {
        return contents;
    }

}
