package com.example.outsourcing_project.domain.log.domain.repository;

import com.example.outsourcing_project.domain.log.controller.LogRequestDto;
import com.example.outsourcing_project.domain.log.domain.model.Log;
import com.example.outsourcing_project.domain.log.domain.model.QLog;
import com.example.outsourcing_project.domain.user.domain.model.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LogRepositoryImpl implements LogSearchRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Log> findAllFromQueryDsl(LogRequestDto dto, Pageable pageable) {

        QLog log = QLog.log;
        //유저 Entity 추가 시 사용
        QUser user = QUser.user;

        //where 절을 동적으로 구현
        BooleanBuilder where = new BooleanBuilder();

        //activityType 이 null 이 아니면 조건 추가 
        if (StringUtils.hasText(dto.getActivityType())) {
            where.and(log.activityType.eq(dto.getActivityType()));

            // activityId 가 null 이면 조건 생략 (service 에서 예외처리)
            if (dto.getActivityId() != null) {
                where.and(log.activityId.eq(dto.getActivityId()));
            }
        }

        // 날짜 조건 처리
        LocalDateTime start = dto.getStartDateTime();
        LocalDateTime end = dto.getEndDateTime();

        if (start != null && end != null) { //시작일 종료일 모두 있을경우
            where.and(log.createdAt.goe(start));
            where.and(log.createdAt.lt(end)); 
        } else if (start != null) {
            where.and(log.createdAt.goe(start));          // 시작일만 있을 경우 (이상 비교)
        } else if (end != null) {
            where.and(log.createdAt.lt(end));             // 종료일만 있을 경우 (미만 비교)
        }

        // 쿼리문 실행
        List<Log> findLogList = queryFactory
                .selectFrom(log)
                .leftJoin(log.user, user).fetchJoin()
                .where(where)
                .orderBy(log.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch(); //조건 없을 시 Null 반환

        // 쿼리 전체 개수
        long total = Optional.ofNullable(
                queryFactory
                        .select(log.count())
                        .from(log)
                        .leftJoin(log.user, user)
                        .where(where)
                        .fetchOne()
        ).orElse(0L);

        //page 반환
        return new PageImpl<>(findLogList, pageable, total);
       }
}
