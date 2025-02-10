package com.demo.calendar.controller;

import com.demo.calendar.domain.dto.request.CalendarRequest;
import com.demo.calendar.domain.dto.response.CalendarResponse;
import com.demo.calendar.security.MemberContext;
import com.demo.calendar.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendars")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    /**
     * 캘린더 생성
     */
    @PostMapping
    public ResponseEntity<CalendarResponse> createCalendar(@RequestBody CalendarRequest request,
                                                           @AuthenticationPrincipal MemberContext memberContext) {
        Long memberId = memberContext.getMemberDto().getId();
        return ResponseEntity.ok(calendarService.createCalendar(memberId, request));
    }

    /**
     * 특정 캘린더 조회
     */
    @GetMapping("/{calendarId}")
    public ResponseEntity<CalendarResponse> getCalendar(@PathVariable Long calendarId,
                                                        @AuthenticationPrincipal MemberContext memberContext) {
        Long memberId = memberContext.getMemberDto().getId();
        return ResponseEntity.ok(calendarService.getCalendar(calendarId, memberId));
    }

    /**
     * 사용자가 소유한 캘린더 목록 조회
     */
    @GetMapping("/user/{memberId}")
    public ResponseEntity<List<CalendarResponse>> getUserCalendars(@PathVariable Long memberId,
                                                                   @AuthenticationPrincipal MemberContext memberContext) {

        if (memberId != memberContext.getMemberDto().getId()) {
            throw new AccessDeniedException("해당 캘린더에 대한 접근 권한이 없습니다.");
        }

        return ResponseEntity.ok(calendarService.getUserCalendars(memberId));
    }

    /**
     * 캘린더 수정
     */
    @PutMapping("/{calendarId}")
    public ResponseEntity<CalendarResponse> updateCalendar(@PathVariable Long calendarId,
                                                           @RequestBody CalendarRequest calendarRequest,
                                                           @AuthenticationPrincipal MemberContext memberContext) {
        Long memberId = memberContext.getMemberDto().getId();
        return ResponseEntity.ok(calendarService.updateCalendar(calendarId, memberId, calendarRequest));
    }

    /**
     * 캘린더 삭제
     */
    @DeleteMapping("/{calendarId}")
    public ResponseEntity<Void> deleteCalendar(@PathVariable Long calendarId,
                                               @AuthenticationPrincipal MemberContext memberContext) {

        Long memberId = memberContext.getMemberDto().getId();

        calendarService.deleteCalendar(calendarId, memberId);
        return ResponseEntity.noContent().build();
    }
}
