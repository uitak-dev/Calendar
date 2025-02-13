package com.demo.calendar.controller;

import com.demo.calendar.domain.dto.MemberDto;
import com.demo.calendar.domain.dto.request.MemberRequest;
import com.demo.calendar.domain.dto.response.EventResponse;
import com.demo.calendar.domain.dto.response.MemberResponse;
import com.demo.calendar.domain.dto.search.EventSearch;
import com.demo.calendar.domain.dto.search.MemberSearch;
import com.demo.calendar.security.MemberContext;
import com.demo.calendar.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final PagedResourcesAssembler<MemberResponse> pagedResourcesAssembler;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/me")
    public ResponseEntity<MemberDto> getAuthenticatedUser(@AuthenticationPrincipal MemberContext memberContext) {
        if (memberContext == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(memberContext.getMemberDto());
    }

    /**
     * 회원 등록.
     */
    @PostMapping
    public ResponseEntity<MemberResponse> register(@RequestBody MemberRequest request) {
            return ResponseEntity.ok(memberService.createMember(request));
    }

    /**
     * 회원 목록.
     */
    @Operation(summary = "회원 목록 조회", description = "회원 목록을 검색조건에 맞게 조회합니다.")
    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<MemberResponse>>> getMembers(@ModelAttribute MemberSearch memberSearch,
                                                                              @AuthenticationPrincipal MemberContext memberContext) {

        Long loggedInId = memberContext.getMemberDto().getId();
        log.info("loggedInId - {}", loggedInId);
        log.info("loggedIn - {}", memberContext.getMemberDto());

        Page<MemberResponse> members = memberService.getMembers(memberSearch, loggedInId);

        if (members.isEmpty()) {
            return ResponseEntity.noContent().build();  // 204 No Content 응답
        }

        PagedModel<EntityModel<MemberResponse>> model = pagedResourcesAssembler.toModel(members);
        try {
            log.info("API 응답 데이터: {}", objectMapper.writeValueAsString(model)); // JSON 변환 후 로그 출력
        } catch (Exception e) {
            log.error("API 응답 데이터 변환 오류", e);
        }

        return ResponseEntity.ok(model);
    }

}
