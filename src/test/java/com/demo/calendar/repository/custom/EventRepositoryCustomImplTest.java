package com.demo.calendar.repository.custom;

import com.demo.calendar.domain.dto.response.EventResponse;
import com.demo.calendar.domain.dto.search.EventSearch;
import com.demo.calendar.domain.entity.Calendar;
import com.demo.calendar.domain.entity.Event;
import com.demo.calendar.domain.entity.Member;
import com.demo.calendar.repository.CalendarRepository;
import com.demo.calendar.repository.EventRepository;
import com.demo.calendar.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EventRepositoryCustomImplTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager em;

    private Calendar testCalendar;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .username("testMember")
                .password("rawPassword")
                .email("test@example.com")
                .build();
        memberRepository.save(member);

        testCalendar = Calendar.builder()
                .name("Test Calendar")
                .description("Test Description")
                .owner(member)
                .build();
        calendarRepository.save(testCalendar);

        for (int i = 1; i <= 5; i++) {
            Event event = Event.builder()
                    .title("Event " + i)
                    .content("Content " + i)
                    .startTime(LocalDateTime.now().plusDays(i))
                    .endTime(LocalDateTime.now().plusDays(i).plusHours(2))
                    .calendar(testCalendar)
                    .owner(member)
                    .build();
            eventRepository.save(event);
        }
        em.flush();
        em.clear();
    }

    @Test
    void 이벤트_목록_조회_테스트() {
        // Given
        EventSearch searchCondition = new EventSearch();
        searchCondition.setTitle("Event");
        searchCondition.setStartTime(LocalDateTime.now().plusDays(1));
        searchCondition.setEndTime(LocalDateTime.now().plusDays(5));

        PageRequest pageRequest = PageRequest.of(0, 10);

        // When
        Page<EventResponse> result = eventRepository.searchEventList(testCalendar.getId(), searchCondition, pageRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(5); // 생성한 5개의 이벤트가 조회되어야 함
        assertThat(result.getTotalElements()).isEqualTo(5);
        assertThat(result.getContent().get(0).getTitle()).contains("Event");
    }
}