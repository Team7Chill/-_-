package com.example.outsourcing_project.domain.user.controller;

import com.example.outsourcing_project.domain.user.domain.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Entity:User")
public class UserTest {
    @Test
    @DisplayName("회원 탈퇴")
    void delete() {
        // Given
        User user = UserTestDataGenerator.getUser();

        // When
        user.delete();

        // Then
        Assertions.assertThat(user.isDeleted()).isTrue();
    }
}
