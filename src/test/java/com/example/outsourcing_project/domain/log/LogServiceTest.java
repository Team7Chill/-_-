package com.example.outsourcing_project.domain.log;

import com.example.outsourcing_project.domain.log.controller.LogRequestDto;
import com.example.outsourcing_project.domain.log.controller.LogResponseDto;
import com.example.outsourcing_project.domain.log.domain.model.Log;
import com.example.outsourcing_project.domain.log.domain.repository.LogRepository;
import com.example.outsourcing_project.domain.log.service.LogService;
import com.example.outsourcing_project.domain.user.domain.model.User;
import com.example.outsourcing_project.global.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogServiceTest {

    @InjectMocks
    private LogService logService;

    @Mock
    private LogRepository logRepository;

    @Test
    void 작업_ID가_존재하는데_활동_유형이_없는_경우_예외가_발생한다() {
        // given
        LogRequestDto dto = new LogRequestDto(
                null,                       // activityType 없음
                100L,                       // activityId 존재
                null, null                  // 날짜 필터 없음
        );

        // when & then
        assertThatThrownBy(() ->
                logService.getLogs(Pageable.unpaged(), dto)
        ).isInstanceOf(BadRequestException.class)
                .hasMessage("활동 유형을 선택해야 합니다.");
    }

    @Test
    void 종료일이_시작일보다_빠른_경우_예외가_발생한다() {
        // given
        LocalDate startDate = LocalDate.of(2024, 6, 10);
        LocalDate endDate = LocalDate.of(2024, 6, 1); // 종료일이 더 과거

        LogRequestDto dto = new LogRequestDto(
                "TASK",        // activityType
                100L,          // activityId
                startDate,
                endDate
        );

        // when & then
        assertThatThrownBy(() ->
                logService.getLogs(Pageable.unpaged(), dto)
        ).isInstanceOf(BadRequestException.class)
                .hasMessage("시작일은 종료일보다 빠르거나 같아야 합니다.");
    }

    @Test
    void 유효한_요청일_경우_정상적으로_로그를_반환한다() {
        // given
        LocalDate startDate = LocalDate.of(2024, 6, 1);
        LocalDate endDate = LocalDate.of(2024, 6, 30);

        LogRequestDto dto = new LogRequestDto(
                "TASK",
                100L,
                startDate,
                endDate
        );

        User mockUser = mock(User.class);
        when(mockUser.getUsername()).thenReturn("testuser");

        Log log = new Log();
        log.setId(1L);
        log.setUser(mockUser);
        log.setActivityId(100L);
        log.setActivityType("TASK");
        log.setMethod("POST");
        log.setUrl("/api/tasks");
        log.setContents("업무 생성");
        log.setCreatedAt(LocalDateTime.of(2024, 6, 1, 12, 0)); // BaseEntity의 필드


        Page<Log> fakePage = new PageImpl<>(List.of(log));
        when(logRepository.findAllFromQueryDsl(any(LogRequestDto.class), any(Pageable.class)))
                .thenReturn(fakePage);

        // when
        Page<LogResponseDto> result = logService.getLogs(Pageable.unpaged(), dto);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1L);
        assertThat(result.getContent().get(0).getActivityId()).isEqualTo(100L);
        assertThat(result.getContent().get(0).getActivityType()).isEqualTo("TASK");
        assertThat(result.getContent().get(0).getContents()).isEqualTo("업무 생성");

        verify(logRepository).findAllFromQueryDsl(any(LogRequestDto.class), any(Pageable.class));
    }

}
