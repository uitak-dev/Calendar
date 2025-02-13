package com.demo.calendar.service;

import com.demo.calendar.domain.dto.request.EventRequest;
import com.demo.calendar.domain.dto.response.EventResponse;
import com.demo.calendar.domain.dto.search.EventSearch;
import com.demo.calendar.domain.entity.*;
import com.demo.calendar.repository.CalendarRepository;
import com.demo.calendar.repository.CalendarShareRepository;
import com.demo.calendar.repository.EventRepository;
import com.demo.calendar.repository.MemberRepository;
import com.demo.calendar.utility.mapper.EventMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final MemberRepository memberRepository;
    private final EventRepository eventRepository;
    private final CalendarRepository calendarRepository;
    private final CalendarShareRepository calendarShareRepository;

    /**
     * 이벤트 생성.
     */
    public EventResponse createEvent(EventRequest request, Long memberId) {

        Member owner = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberId));

        Long calendarId = request.getCalendarId();
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new EntityNotFoundException("Calendar not found with id: " + calendarId));

        if(!isPermission(calendar, null, memberId, Permission.WRITE)) {
            throw new AccessDeniedException("해당 캘린더에 이벤트 생성 권한이 없습니다.");
        }

        Event event = Event.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .calendar(calendar)
                .owner(owner)
                .build();

        eventRepository.save(event);

        return EventMapper.convertToResponse(event);
    }

    /**
     * 특정 캘린더의 이벤트 목록 조회.
     * 검색 조건 추가: 기간, 제목, 페이징
     */
    public Page<EventResponse> getEventsByCalendar(Long calendarId, EventSearch eventSearch) {
        Page<EventResponse> events = eventRepository.searchEventList(calendarId, eventSearch, PageRequest.of(0, 5));

        if (events.isEmpty()) {
            log.warn("캘린더 ID {}에 대한 이벤트가 없습니다.", calendarId);
        }

        return events;
    }

    /**
     * 이벤트 상세 조회
     */
    public EventResponse getEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));

        return EventMapper.convertToResponse(event);
    }

    /**
     * 이벤트 수정
     */
    public EventResponse updateEvent(Long eventId, EventRequest request, Long memberId) {

        Long calendarId = request.getCalendarId();
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new EntityNotFoundException("Calendar not found with id: " + calendarId));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));

        if(!isPermission(calendar, event, memberId, Permission.EDIT)) {
            throw new AccessDeniedException("해당 캘린더에 이벤트 수정 권한이 없습니다.");
        }

        event.updateInfo(request.getTitle(), request.getContent(),
                request.getStartTime(), request.getEndTime());

        return EventMapper.convertToResponse(event);
    }

    /**
     * 이벤트 삭제.
     */
    public void deleteEvent(Long eventId, Long memberId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));


        if (!isPermission(event.getCalendar(), event, memberId, Permission.EDIT)) {
            throw new AccessDeniedException("해당 캘린더에 이벤트 삭제 권한이 없습니다.");
        }

        eventRepository.delete(event);
    }

    /**
     * 이벤트 생성/수정 권한 확인.
     */
    private boolean isPermission(Calendar calendar, @Nullable Event event, Long memberId, Permission requiredPermission) {

        // 캘린더의 소유주인 경우.
        if (calendar.getOwner().getId().equals(memberId)) return true;

        // 해당 이벤트의 소유주인 경우, 수정/삭제 가능.
        if (event != null && event.getOwner().getId() == memberId) return true;

        return calendarShareRepository.findById(new CalendarShare.CalendarShareId(calendar.getId(), memberId))
                .map(CalendarShare::getAccessControls)
                .map(controls -> controls.stream()
                        .map(CalendarAccessControl::getPermission)
                        .anyMatch(permission -> permission == requiredPermission))
                .orElse(false);
    }
}
