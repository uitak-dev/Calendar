package com.demo.calendar.controller;

import com.demo.calendar.domain.dto.request.CalendarShareRequest;
import com.demo.calendar.domain.dto.response.CalendarResponse;
import com.demo.calendar.domain.dto.response.MemberResponse;
import com.demo.calendar.domain.entity.Permission;
import com.demo.calendar.security.MemberContext;
import com.demo.calendar.service.CalendarShareService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "캘린더 공유", description = "특정 캘린더를 여러 사용자에게 공유합니다.")
    @PostMapping("/{calendarId}/share")
    public ResponseEntity<Void> shareCalendar(@PathVariable Long calendarId,
                                              @RequestBody CalendarShareRequest calendarShareRequest,
                                              @AuthenticationPrincipal MemberContext memberContext) {

        Long loggedInMemberId = memberContext.getMemberDto().getId();

        calendarShareService.shareCalendar(calendarId, loggedInMemberId,
                calendarShareRequest.getMemberIds(), calendarShareRequest.getPermission());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "사용자가 공유받은 캘린더 목록 조회", description = "특정 사용자가 공유받은 캘린더 목록을 조회합니다.")
    @GetMapping("/user/{memberId}")
    public ResponseEntity<List<CalendarResponse>> getSharedCalendars(@PathVariable Long memberId) {
        return ResponseEntity.ok(calendarShareService.getSharedCalendars(memberId));
    }

    @Operation(summary = "캘린더 공유 사용자 목록 조회", description = "특정 캘린더를 공유받은 사용자 목록을 조회합니다.")
    @GetMapping("/{calendarId}/users")
    public ResponseEntity<List<MemberResponse>> getSharedUsers(@PathVariable Long calendarId) {
        return ResponseEntity.ok(calendarShareService.getSharedUsers(calendarId));
    }

    @Operation(summary = "캘린더 공유 해제", description = "여러 사용자의 캘린더 공유를 해제합니다.")
    @DeleteMapping("/{calendarId}/unshare")
    public ResponseEntity<Void> unshareCalendar(@PathVariable Long calendarId,
                                                @RequestBody List<Long> memberIds,
                                                @AuthenticationPrincipal MemberContext memberContext) {
        calendarShareService.unshareCalendar(calendarId, memberContext.getMemberDto().getId(), memberIds);
        return ResponseEntity.noContent().build();
    }
}
