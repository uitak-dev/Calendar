package com.demo.calendar.service;

import com.demo.calendar.domain.dto.request.CalendarRequest;
import com.demo.calendar.domain.dto.response.CalendarResponse;
import com.demo.calendar.domain.entity.Member;
import com.demo.calendar.repository.CalendarRepository;
import com.demo.calendar.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)  // Member를 클래스 레벨에서 한 번만 저장.
class CalendarServiceTest {

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
                .username("testUser")
                .password("rawPassword")
                .email("test@example.com")
                .build();

        savedMember = memberRepository.save(member);
    }

    @Test
    @WithUserDetails(value = "testUser", userDetailsServiceBeanName = "formUserDetailsService")
    void 캘린더_생성_테스트() {
        // Given
        CalendarRequest request = new CalendarRequest("My Calendar", "Test Description");

        // When
        CalendarResponse response = calendarService.createCalendar(savedMember.getId(), request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("My Calendar");
        assertThat(response.getOwner().getId()).isEqualTo(savedMember.getId());
    }

    @Test
    @WithUserDetails(value = "testUser", userDetailsServiceBeanName = "formUserDetailsService")
    void 캘린더_조회_테스트() {
        // Given
        CalendarRequest request = new CalendarRequest("Work Calendar", "Company Events");
        CalendarResponse createdCalendar = calendarService.createCalendar(savedMember.getId(), request);

        // When
        CalendarResponse foundCalendar = calendarService.getCalendar(createdCalendar.getId(), savedMember.getId());

        // Then
        assertThat(foundCalendar).isNotNull();
        assertThat(foundCalendar.getDescription()).isEqualTo("Company Events");
        assertThat(foundCalendar.getOwner().getId()).isEqualTo(savedMember.getId());
    }

    @Test
    @WithUserDetails(value = "testUser", userDetailsServiceBeanName = "formUserDetailsService")
    void 캘린더_수정_테스트() {
        // Given
        CalendarRequest createRequest = new CalendarRequest("Old Calendar", "Old Description");
        CalendarResponse createdCalendar = calendarService.createCalendar(savedMember.getId(), createRequest);

        CalendarRequest updateRequest = new CalendarRequest("Updated Calendar", "Updated Description");

        // When
        CalendarResponse updatedCalendar = calendarService.updateCalendar(createdCalendar.getId(), savedMember.getId(), updateRequest);

        // Then
        assertThat(updatedCalendar).isNotNull();
        assertThat(updatedCalendar.getName()).isEqualTo("Updated Calendar");
        assertThat(updatedCalendar.getDescription()).isEqualTo("Updated Description");
    }


    @Test
    @WithUserDetails(value = "testUser", userDetailsServiceBeanName = "formUserDetailsService")
    void 캘린더_삭제_테스트() {
        // Given
        CalendarRequest request = new CalendarRequest("Temporary Calendar", "For Testing");
        CalendarResponse createdCalendar = calendarService.createCalendar(savedMember.getId(), request);

        // When
        calendarService.deleteCalendar(createdCalendar.getId(), savedMember.getId());

        // Then
        assertThatThrownBy(() -> calendarService.getCalendar(createdCalendar.getId(), savedMember.getId()))
                .isInstanceOf(EntityNotFoundException.class);
    }
}