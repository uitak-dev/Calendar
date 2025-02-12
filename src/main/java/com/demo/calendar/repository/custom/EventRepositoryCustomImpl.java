package com.demo.calendar.repository.custom;

import com.demo.calendar.domain.dto.response.EventResponse;
import com.demo.calendar.domain.dto.search.EventSearch;
import com.demo.calendar.domain.entity.Calendar;
import com.demo.calendar.domain.entity.QEvent;
import com.demo.calendar.repository.CalendarRepository;
import com.demo.calendar.utility.mapper.EventMapper;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class EventRepositoryCustomImpl implements EventRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final CalendarRepository calendarRepository;

    public EventRepositoryCustomImpl(EntityManager em, CalendarRepository calendarRepository) {
        this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
        this.calendarRepository = calendarRepository;
    }

    private static final QEvent event = QEvent.event;

    @Override
    public Page<EventResponse> searchEventList(Long calendarId, EventSearch eventSearch, Pageable pageable) {

        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new EntityNotFoundException("Calendar not found with id: " + calendarId));

        List<EventResponse> content = queryFactory
                .selectFrom(event)
                .where(
                        event.calendar.eq(calendar),
                        titleEq(eventSearch.getTitle()),
                        betweenStartAndEnd(eventSearch.getStartTime(), eventSearch.getEndTime())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch()
                .stream()
                .map(EventMapper::convertToResponse)
                .collect(Collectors.toList());

        // 전체 개수 조회 (페이징 처리)
        JPAQuery<Long> countQuery = queryFactory
                .select(event.count())
                .from(event)
                .where(
                        event.calendar.id.eq(calendarId),
                        titleEq(eventSearch.getTitle()),
                        betweenStartAndEnd(eventSearch.getStartTime(), eventSearch.getEndTime())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression titleEq(String title) {
        // null, isEmpty() 검사
        return StringUtils.hasLength(title) ? event.title.containsIgnoreCase(title) : null;
    }

    private BooleanExpression betweenStartAndEnd(LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime != null && endTime != null) {
            return event.startTime.lt(endTime).and(event.endTime.gt(startTime));
        }
        else if(startTime != null) {
            return event.endTime.gt(startTime);
        }
        else if(endTime != null) {
            return event.startTime.lt(endTime);
        }

        return null;
    }
}
