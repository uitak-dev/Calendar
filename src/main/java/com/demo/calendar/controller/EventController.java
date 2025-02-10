package com.demo.calendar.controller;

import com.demo.calendar.domain.dto.request.EventRequest;
import com.demo.calendar.domain.dto.response.EventResponse;
import com.demo.calendar.security.MemberContext;
import com.demo.calendar.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    /**
     * 이벤트 생성
     */
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest request,
                                                     @AuthenticationPrincipal MemberContext memberContext) {
        return ResponseEntity.ok(eventService.createEvent(request, memberContext.getMemberDto().getId()));
    }

    /**
     * 특정 캘린더의 이벤트 목록 조회
     */
    @GetMapping("/calendar/{calendarId}")
    public ResponseEntity<List<EventResponse>> getEventsByCalendar(@PathVariable Long calendarId) {
        return ResponseEntity.ok(eventService.getEventsByCalendar(calendarId));
    }

    /**
     * 이벤트 상세 조회
     */
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEvent(eventId));
    }

    /**
     * 이벤트 수정
     */
    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long eventId,
                                                     @RequestBody EventRequest request,
                                                     @AuthenticationPrincipal MemberContext memberContext) {
        EventResponse eventResponse = eventService.updateEvent(eventId, request, memberContext.getMemberDto().getId());
        return ResponseEntity.ok(eventResponse);
    }

    /**
     * 이벤트 삭제
     */
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId,
                                            @AuthenticationPrincipal MemberContext memberContext) {
        eventService.deleteEvent(eventId, memberContext.getMemberDto().getId());
        return ResponseEntity.noContent().build();
    }
}
