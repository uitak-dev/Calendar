package com.demo.calendar.controller;

import com.demo.calendar.domain.dto.response.CalendarResponse;
import com.demo.calendar.domain.dto.response.MemberResponse;
import com.demo.calendar.domain.entity.Permission;
import com.demo.calendar.security.MemberContext;
import com.demo.calendar.service.CalendarShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendar-share")
@RequiredArgsConstructor
public class CalendarShareController {

    private final CalendarShareService calendarShareService;

    /**
     * 캘린더 공유 (여러 사용자에게 공유)
     */
    @PostMapping("/{calendarId}/share")
    public ResponseEntity<Void> shareCalendar(@PathVariable Long calendarId,
                                              @RequestParam List<Long> memberIds,
                                              @RequestParam Permission permission) {

        calendarShareService.shareCalendar(calendarId, memberIds, permission);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 사용자가 공유받은 캘린더 목록 조회
     */
    @GetMapping("/user/{memberId}")
    public ResponseEntity<List<CalendarResponse>> getSharedCalendars(@PathVariable Long memberId) {
        return ResponseEntity.ok(calendarShareService.getSharedCalendars(memberId));
    }

    /**
     * 특정 캘린더를 공유받은 사용자 목록 조회
     */
    @GetMapping("/{calendarId}/users")
    public ResponseEntity<List<MemberResponse>> getSharedUsers(@PathVariable Long calendarId) {
        return ResponseEntity.ok(calendarShareService.getSharedUsers(calendarId));
    }

    /**
     * 특정 사용자에 대한 캘린더 공유 해제
     */
    @DeleteMapping("/{calendarId}/unshare/{memberId}")
    public ResponseEntity<Void> unshareCalendar(@PathVariable Long calendarId,
                                                @PathVariable Long memberId,
                                                @AuthenticationPrincipal MemberContext memberContext) {
        calendarShareService.unshareCalendar(calendarId, memberContext.getMemberDto().getId(), memberId);
        return ResponseEntity.noContent().build();
    }
}
