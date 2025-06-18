package com.example.outsourcing_project.domain.log.service;

import com.example.outsourcing_project.domain.log.controller.LogRequestDto;
import com.example.outsourcing_project.domain.log.controller.LogResponseDto;
import com.example.outsourcing_project.domain.log.domain.model.Log;
import com.example.outsourcing_project.domain.log.domain.repository.LogRepository;
import com.example.outsourcing_project.domain.user.domain.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.example.outsourcing_project.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LogService {

    @PersistenceContext
    private final EntityManager entityManager;

    private LogRepository logRepository;

    public Page<LogResponseDto> getLogs(Pageable pageable, LogRequestDto logRequestDto) {

        //작업 ID가 존재하는데, 활동 유형이 없는경우 예외처리
        validateActivityIdWithType(logRequestDto);

        //종료일이 시작일보다 빠를 경우 예외 처리
        validateEndDateEarlierThanStartDate(logRequestDto);
        
        //DB 에서 페이징 및 검색 처리 된 Log 리스트 가져옴
        Page<Log> findLog = logRepository.findAllFromQueryDsl(logRequestDto,pageable);
        
        //Log 리스트를 Dto 로 변환
        List<LogResponseDto> logResponseDto = findLog.getContent().stream()
                .map(log -> new LogResponseDto().toDto(log)) 
                .collect(Collectors.toList());

        // PageImpl 을 사용하여 반환
        return new PageImpl<>(logResponseDto, pageable, findLog.getTotalElements());

    }

    private void validateActivityIdWithType(LogRequestDto logRequestDto){
        //타입이 있으면 리턴
        String activityType = logRequestDto.getActivityType();
        if(activityType != null && !activityType.isBlank()) return;

        //타입이 없는데 활동 ID 만 존재할경우 예외처리
        if(logRequestDto.getActivityId() != null){
            throw new BadRequestException("활동 유형을 선택해야 합니다.");
        }
    }

    private void validateEndDateEarlierThanStartDate(LogRequestDto logRequestDto){
        LocalDate startDate = logRequestDto.getStartDate();
        LocalDate endDate = logRequestDto.getEndDate();

        if (startDate == null || endDate == null) return;

        if(startDate.isAfter(endDate)){
            throw new BadRequestException("시작일은 종료일보다 빠르거나 같아야 합니다.");
        }
    }

    @Transactional
    public void addLog(LogSaveDto logSaveDto){

        //UserId 정보만 생성
        User userRef = entityManager.getReference(User.class, logSaveDto.getUserId());

        Log log = logSaveDto.toEntity(userRef);

        logRepository.save(log);
    }
}
