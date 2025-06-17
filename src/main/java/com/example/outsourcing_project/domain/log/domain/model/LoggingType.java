package com.example.outsourcing_project.domain.log.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LoggingType {

    TASK_CREATED("/api/tasks", "POST", "TASK_CREATED", "새로운 작업이 생성되었습니다.",-1),
    TASK_UPDATED("/api/tasks/\\d+", "PATCH", "TASK_UPDATED", "작업이 변경되었습니다.",3),
    TASK_DELETED("/api/tasks/\\d+", "DELETE", "TASK_DELETED", "작업이 삭제되었습니다.",3),
    TASK_STATUS_CHANGED("/api/tasks/\\d+/status/", "PATCH", "TASK_STATUS_CHANGED", "작업 상태가 변경되었습니다.",3),

    COMMENT_CREATED("/api/tasks/\\d+/comments", "POST", "COMMENT_CREATED", "새로운 댓글이 작성되었습니다.",-1),
    COMMENT_UPDATED("/api/tasks/\\d+/comments/\\d+", "PATCH", "COMMENT_UPDATED", "댓글이 수정되었습니다.",5),
    COMMENT_DELETED("/api/tasks/\\d+/comments/\\d+", "DELETE", "COMMENT_DELETED", "댓글이 삭제되었습니다.",5),

    LOGIN("/api/login", "POST", "LOGIN", "로그인 되었습니다.",-1),
    LOGOUT("/api/logout", "POST", "LOGOUT", "로그아웃 되었습니다.",-1);

    private final String uriPattern;
    private final String method;
    private final String type;
    private final String contents;
    private final int idIndex;
}
