package com.example.outsourcing_project.domain.log;

import com.example.outsourcing_project.domain.log.controller.LogController;
import com.example.outsourcing_project.domain.log.controller.LogRequestDto;
import com.example.outsourcing_project.domain.log.controller.LogResponseDto;
import com.example.outsourcing_project.domain.log.service.LogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogControllerTest {

    @InjectMocks
    private LogController logController;

    @Mock
    private LogService logService;

    @Test
    void 로그_조회_API_요청시_정상적으로_응답을_반환한다() {
        // given
        LogResponseDto dto = new LogResponseDto(
                1L,
                "이름",
                101L,
                "TASK",
                "업무 생성",
                LocalDateTime.of(2024, 1, 1, 12, 0)
        );
        Page<LogResponseDto> logPage = new PageImpl<>(List.of(dto));

        when(logService.getLogs(any(), any())).thenReturn(logPage);

        // when
        ResponseEntity<?> response = logController.getLogs(
                1, 10,
                Optional.of("TASK"),
                Optional.of(101L),
                Optional.of(LocalDate.of(2024, 1, 1)),
                Optional.of(LocalDate.of(2024, 12, 31)),
                "createdAt", "DESC"
        );

        // then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        verify(logService).getLogs(any(PageRequest.class), any(LogRequestDto.class));
    }
}
