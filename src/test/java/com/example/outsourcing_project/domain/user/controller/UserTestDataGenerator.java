package com.example.outsourcing_project.domain.user.controller;

import com.example.outsourcing_project.domain.user.domain.model.User;
import com.example.outsourcing_project.global.enums.UserRoleEnum;

public class UserTestDataGenerator {
    public static User getUser() {
        User user = User.builder()
                .name("홍길동")
                .username("동에번쩍")
                .email("hong-gd@gmail.com")
                .role(UserRoleEnum.USER)
                .password("password")
                .build();
        return user;
    }
}
