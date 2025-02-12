package com.demo.calendar.controller;

import com.demo.calendar.domain.dto.request.EventRequest;
import com.demo.calendar.domain.dto.response.EventResponse;
import com.demo.calendar.domain.dto.search.EventSearch;
import com.demo.calendar.security.MemberContext;
import com.demo.calendar.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @Operation(summary = "이벤트 생성", description = "인증된 사용자가 캘린더에 새로운 이벤트를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이벤트 생성 성공"),
            @ApiResponse(responseCode = "403", description = "이벤트 생성 권한 없음"),
            @ApiResponse(responseCode = "404", description = "캘린더를 찾을 수 없음")
    })
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest request,
                                                     @AuthenticationPrincipal MemberContext memberContext) {
        return ResponseEntity.ok(eventService.createEvent(request, memberContext.getMemberDto().getId()));
    }

    @Operation(summary = "캘린더 이벤트 목록 조회", description = "주어진 캘린더 ID에 속한 이벤트 목록을 검색조건에 맞게 조회합니다.")
    @GetMapping("/calendar/{calendarId}")
    public ResponseEntity<Page<EventResponse>> getEventsByCalendar(@PathVariable Long calendarId,
                                                                   @RequestBody EventSearch eventSearch) {
        return ResponseEntity.ok(eventService.getEventsByCalendar(calendarId, eventSearch));
    }

    @Operation(summary = "이벤트 상세 조회", description = "이벤트 ID를 기반으로 특정 이벤트의 상세 정보를 조회합니다.")
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEvent(eventId));
    }

    @Operation(summary = "이벤트 수정", description = "권한 있는 사용자가 이벤트 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이벤트 수정 성공"),
            @ApiResponse(responseCode = "403", description = "이벤트 수정 권한 없음"),
            @ApiResponse(responseCode = "404", description = "이벤트를 찾을 수 없음")
    })
    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long eventId,
                                                     @RequestBody EventRequest request,
                                                     @AuthenticationPrincipal MemberContext memberContext) {
        EventResponse eventResponse = eventService.updateEvent(eventId, request, memberContext.getMemberDto().getId());
        return ResponseEntity.ok(eventResponse);
    }

    @Operation(summary = "이벤트 삭제", description = "권한 있는 사용자가 이벤트를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "이벤트 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "이벤트 삭제 권한 없음"),
            @ApiResponse(responseCode = "404", description = "이벤트를 찾을 수 없음")
    })
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId,
                                            @AuthenticationPrincipal MemberContext memberContext) {
        eventService.deleteEvent(eventId, memberContext.getMemberDto().getId());
        return ResponseEntity.noContent().build();
    }
}
