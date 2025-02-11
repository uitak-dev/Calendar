package com.demo.calendar.listener;

import com.demo.calendar.domain.entity.Calendar;
import com.demo.calendar.domain.entity.Event;
import com.demo.calendar.domain.entity.Member;
import com.demo.calendar.repository.CalendarRepository;
import com.demo.calendar.repository.EventRepository;
import com.demo.calendar.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final MemberRepository memberRepository;
    private final CalendarRepository calendarRepository;
    private final EventRepository eventRepository;

    private final PasswordEncoder passwordEncoder;

    // 스레드 안전한 중복 실행 방지 플래그
    private final AtomicBoolean alreadySetup = new AtomicBoolean(false);


    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup.get()) {
            return;
        }
        setupData();
        alreadySetup.set(true);
    }

    private void setupData() {

        Member testMember01 = createMemberIfNotFound("testMember01", "password01", "testMember01@example.com");
        Member testMember02 = createMemberIfNotFound("testMember02", "password02", "testMember02@example.com");
        Member testMember03 = createMemberIfNotFound("testMember03", "password03", "testMember03@example.com");
        Member testMember04 = createMemberIfNotFound("testMember04", "password04", "testMember04@example.com");

        Calendar calendar01 = createCalendar("Welcome", "How to use the Calendar", testMember01);
        Calendar calendar02 = createCalendar("Welcome", "How to use the Calendar", testMember02);
        Calendar calendar03 = createCalendar("Welcome", "How to use the Calendar", testMember03);
        Calendar calendar04 = createCalendar("Welcome", "How to use the Calendar", testMember04);

        createEvent("How to use the Event", "Blah blah blah", LocalDateTime.now(), LocalDateTime.now().plusHours(3), testMember01, calendar01);
        createEvent("How to use the Event", "Blah blah blah", LocalDateTime.now(), LocalDateTime.now().plusHours(3), testMember02, calendar02);
        createEvent("How to use the Event", "Blah blah blah", LocalDateTime.now(), LocalDateTime.now().plusHours(3), testMember03, calendar03);
        createEvent("How to use the Event", "Blah blah blah", LocalDateTime.now(), LocalDateTime.now().plusHours(3), testMember04, calendar04);
    }

    private Member createMemberIfNotFound(String username, String password, String email) {

        Optional<Member> existingMember = memberRepository.findByUsername(username);

        if (existingMember.isPresent()) {
            return existingMember.get();
        }

        Member newMember = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();

        return memberRepository.save(newMember);
    }

    private Calendar createCalendar(String name, String description, Member owner) {
        if (owner == null) {
            throw new IllegalArgumentException("캘린더 생성 실패: 'owner'가 null 입니다.");
        }

        Calendar calendar = Calendar.builder()
                .name(name)
                .description(description)
                .owner(owner)
                .build();

        return calendarRepository.save(calendar);
    }

    private Event createEvent(String title, String content, LocalDateTime startTime, LocalDateTime endTime,
                              Member owner, Calendar calendar) {
        if (owner == null || calendar == null) {
            throw new IllegalArgumentException("이벤트 생성 실패: 'owner' 또는 'calendar'가 null 입니다.");
        }

        Event event = Event.builder()
                .title(title)
                .content(content)
                .startTime(startTime)
                .endTime(endTime)
                .owner(owner)
                .calendar(calendar)
                .build();

        return eventRepository.save(event);
    }
}
