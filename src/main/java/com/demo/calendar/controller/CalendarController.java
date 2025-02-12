package com.demo.calendar.controller;

import com.demo.calendar.domain.dto.request.CalendarRequest;
import com.demo.calendar.domain.dto.response.CalendarResponse;
import com.demo.calendar.security.MemberContext;
import com.demo.calendar.service.CalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "캘린더 생성", description = "인증된 사용자가 새로운 캘린더를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "캘린더 생성 성공"),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음")
    })
    @PostMapping
    public ResponseEntity<CalendarResponse> createCalendar(@RequestBody CalendarRequest request,
                                                           @AuthenticationPrincipal MemberContext memberContext) {
        Long memberId = memberContext.getMemberDto().getId();
        return ResponseEntity.ok(calendarService.createCalendar(memberId, request));
    }

    @Operation(summary = "캘린더 상세 조회", description = "캘린더 ID를 기준으로, 캘린더 정보를 조회합니다.")
    @GetMapping("/{calendarId}")
    public ResponseEntity<CalendarResponse> getCalendar(@PathVariable Long calendarId,
                                                        @AuthenticationPrincipal MemberContext memberContext) {
        Long memberId = memberContext.getMemberDto().getId();
        return ResponseEntity.ok(calendarService.getCalendar(calendarId, memberId));
    }

    @Operation(summary = "사용자가 소유한 캘린더 목록 조회", description = "사용자가 소유한 캘린더 목록을 조회합니다.")
    @GetMapping("/user/{memberId}")
    public ResponseEntity<List<CalendarResponse>> getUserCalendars(@PathVariable Long memberId,
                                                                   @AuthenticationPrincipal MemberContext memberContext) {

        if (memberId != memberContext.getMemberDto().getId()) {
            throw new AccessDeniedException("해당 캘린더에 대한 접근 권한이 없습니다.");
        }

        return ResponseEntity.ok(calendarService.getUserCalendars(memberId));
    }

    @Operation(summary = "캘린더 수정", description = "캘린더 ID를 기준으로 캘린더 정보를 수정합니다.")
    @PutMapping("/{calendarId}")
    public ResponseEntity<CalendarResponse> updateCalendar(@PathVariable Long calendarId,
                                                           @RequestBody CalendarRequest calendarRequest,
                                                           @AuthenticationPrincipal MemberContext memberContext) {
        Long memberId = memberContext.getMemberDto().getId();
        return ResponseEntity.ok(calendarService.updateCalendar(calendarId, memberId, calendarRequest));
    }

    @Operation(summary = "캘린더 삭제", description = "캘린더 ID를 기준으로 특정 캘린더를 삭제합니다.")
    @DeleteMapping("/{calendarId}")
    public ResponseEntity<Void> deleteCalendar(@PathVariable Long calendarId,
                                               @AuthenticationPrincipal MemberContext memberContext) {

        Long memberId = memberContext.getMemberDto().getId();

        calendarService.deleteCalendar(calendarId, memberId);
        return ResponseEntity.noContent().build();
    }
}
