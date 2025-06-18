package com.example.outsourcing_project.domain.log.service;

import com.example.outsourcing_project.domain.log.domain.model.Log;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogSaveDto {

    //private User user;
    private Long activityId;
    private String activityType;
    private String method;
    private String uri;
    private String contents;

    public Log toEntity() {
        Log log = new Log();
        //log.setUser(this.user);
        log.setActivityId(this.activityId);
        log.setActivityType(this.activityType);
        log.setMethod(this.method);
        log.setUrl(this.uri);
        log.setContents(this.contents);
        return log;
    }
}
