package com.demo.calendar.utility.mapper;

import com.demo.calendar.domain.dto.request.MemberRequest;
import com.demo.calendar.domain.dto.response.MemberResponse;
import com.demo.calendar.domain.entity.Member;

public class MemberMapper {

    public static Member convertToEntity(MemberRequest memberRequest) {
        return Member.builder()
                .id(memberRequest.getId())
                .username(memberRequest.getUsername())
                .password(memberRequest.getPassword())
                .email(memberRequest.getEmail())
                .build();
    }

    public static MemberResponse convertToResponse(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .username(member.getUsername())
                .email(member.getEmail())
                .build();
    }
}
