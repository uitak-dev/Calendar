package com.demo.calendar.service;

import com.demo.calendar.domain.dto.request.CalendarRequest;
import com.demo.calendar.domain.dto.request.EventRequest;
import com.demo.calendar.domain.dto.response.EventResponse;
import com.demo.calendar.domain.dto.search.EventSearch;
import com.demo.calendar.domain.entity.Member;
import com.demo.calendar.repository.CalendarRepository;
import com.demo.calendar.repository.MemberRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Member를 클래스 레벨에서 한 번만 저장.
class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    private Member savedMember;

    @BeforeAll
    void setUp() {
        // 테스트 시작 전에 한 번만 Member 저장
        Member member = Member.builder()
                .username("testUser2")
                .password("rawPassword")
                .email("test@example.com")
                .build();

        savedMember = memberRepository.save(member);
    }

    @Test
    @WithUserDetails(value = "testUser2", userDetailsServiceBeanName = "formUserDetailsService")
    @Transactional
    void 이벤트_생성_테스트() {
        // Given
        CalendarRequest calendarRequest = new CalendarRequest("Events Calendar", "Event Testing");
        Long calendarId = calendarService.createCalendar(savedMember.getId(), calendarRequest).getId();

        EventRequest eventRequest = EventRequest.builder()
                .title("Meeting")
                .content("Weekly Sync")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .calendarId(calendarId)
                .build();

        // When
        EventResponse eventResponse = eventService.createEvent(eventRequest, savedMember.getId());

        // Then
        assertThat(eventResponse).isNotNull();
        assertThat(eventResponse.getTitle()).isEqualTo("Meeting");
    }

    @Test
    @WithUserDetails(value = "testUser2", userDetailsServiceBeanName = "formUserDetailsService")
    @Transactional
    void 특정_캘린더의_이벤트_조회_테스트() {
        // Given
        CalendarRequest calendarRequest = new CalendarRequest("My Calendar", "Event Testing");
        Long calendarId = calendarService.createCalendar(savedMember.getId(), calendarRequest).getId();
        EventSearch eventSearch = new EventSearch();

        EventRequest eventRequest1 = EventRequest.builder()
                .title("Event-1")
                .content("Weekly Sync-1")
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().plusHours(1))
                .calendarId(calendarId)
                .build();

        EventRequest eventRequest2 = EventRequest.builder()
                .title("Meeting-2")
                .content("Weekly Sync-2")
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(2))
                .calendarId(calendarId)
                .build();

        eventService.createEvent(eventRequest1, savedMember.getId());
        eventService.createEvent(eventRequest2, savedMember.getId());

        // When
        Page<EventResponse> events = eventService.getEventsByCalendar(calendarId, eventSearch);

        // Then
        assertThat(events).hasSize(2);
    }
}
