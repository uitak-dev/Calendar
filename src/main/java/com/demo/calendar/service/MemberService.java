package com.demo.calendar.service;

import com.demo.calendar.domain.dto.request.MemberRequest;
import com.demo.calendar.domain.dto.response.MemberResponse;
import com.demo.calendar.domain.entity.Member;
import com.demo.calendar.repository.MemberRepository;
import com.demo.calendar.utility.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입.
     */
    public void createMember(MemberRequest memberRequest) {
        Member member = MemberMapper.convertToEntity(memberRequest);
        memberRepository.save(member);
    }

    /**
     * 회원 목록 조회.
     * 페이징, 아이디 필터링
     */
}
