package com.demo.calendar.service;

import com.demo.calendar.domain.dto.request.MemberRequest;
import com.demo.calendar.domain.dto.response.MemberResponse;
import com.demo.calendar.domain.entity.Calendar;
import com.demo.calendar.domain.entity.Event;
import com.demo.calendar.domain.entity.Member;
import com.demo.calendar.repository.CalendarRepository;
import com.demo.calendar.repository.EventRepository;
import com.demo.calendar.repository.MemberRepository;
import com.demo.calendar.utility.mapper.MemberMapper;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CalendarRepository calendarRepository;
    private final EventRepository eventRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입.
     */
    public void createMember(MemberRequest request) {

        if (memberRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new EntityExistsException("이미 존재하는 아이디입니다.");
        }

        Member member = Member.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .build();

        memberRepository.save(member);
        initializeRegistration(member);
    }

    /**
     * 신규 사용자 등록 초기화.
     */
    private void initializeRegistration(Member member) {

        Calendar calendar = Calendar.builder()
                .name("Welcome")
                .description("welcome to calendar management and sharing service.")
                .owner(member)
                .build();

        calendarRepository.save(calendar);

        Event event = Event.builder()
                .title("write your event title")
                .content("write your event content")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(3))
                .owner(member)
                .calendar(calendar)
                .build();

        eventRepository.save(event);
    }

    /**
     * - 회원 정보 조회(memberId).
     * - 회원 정보 수정(memberId).
     */

}
