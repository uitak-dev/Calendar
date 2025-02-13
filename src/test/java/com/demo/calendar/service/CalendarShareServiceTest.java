package com.demo.calendar.service;

import com.demo.calendar.domain.dto.request.CalendarRequest;
import com.demo.calendar.domain.dto.response.CalendarResponse;
import com.demo.calendar.domain.dto.response.MemberResponse;
import com.demo.calendar.domain.entity.Member;
import com.demo.calendar.domain.entity.Permission;
import com.demo.calendar.repository.CalendarRepository;
import com.demo.calendar.repository.MemberRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Member를 클래스 레벨에서 한 번만 저장.
class CalendarShareServiceTest {

    @Autowired
    private CalendarShareService calendarShareService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    private List<Member> savedMembers = new ArrayList<>();

    @BeforeAll
    void setUp() {
        String[] usernames = {"user1", "user2", "user3"};
        String[] emails = {"user1@example.com", "user2@example.com", "user3@example.com"};

        for (int i = 0; i < usernames.length; i++) {
            Member member = Member.builder()
                    .username(usernames[i])
                    .password("rawPassword") // 실제 환경에서는 인코딩된 비밀번호 사용 필요
                    .email(emails[i])
                    .build();

            savedMembers.add(memberRepository.save(member));
        }
    }

    @Test
    @WithUserDetails(value = "user1", userDetailsServiceBeanName = "customUserDetailsService")
    void 캘린더_공유_테스트() {
        // Given
        Member member1 = savedMembers.get(0);
        Member member2 = savedMembers.get(1);
        Member member3 = savedMembers.get(2);

        CalendarRequest request = new CalendarRequest("Shared Calendar", "Test Sharing");
        CalendarResponse createdCalendar = calendarService.createCalendar(member1.getId(), request);

        // When
        calendarShareService.shareCalendar(
                createdCalendar.getId(), member1.getId(),
                List.of(member2.getId(), member3.getId()),
                Permission.READ);

        // Then
        List<CalendarResponse> sharedCalendars2 = calendarShareService.getSharedCalendars(member2.getId());
        assertThat(sharedCalendars2).hasSize(1);
        assertThat(sharedCalendars2.get(0).getName()).isEqualTo("Shared Calendar");

        List<CalendarResponse> sharedCalendars3 = calendarShareService.getSharedCalendars(member3.getId());
        assertThat(sharedCalendars3).hasSize(1);
        assertThat(sharedCalendars3.get(0).getName()).isEqualTo("Shared Calendar");
    }

    @Test
    @WithUserDetails(value = "user1", userDetailsServiceBeanName = "customUserDetailsService")
    void 공유된_사용자_목록_조회_테스트() {
        // Given
        Member member1 = savedMembers.get(0);
        Member member2 = savedMembers.get(1);
        Member member3 = savedMembers.get(2);

        CalendarRequest request = new CalendarRequest("Project Calendar", "Team Project");
        CalendarResponse createdCalendar = calendarService.createCalendar(member1.getId(), request);

        calendarShareService.shareCalendar(
                createdCalendar.getId(), member1.getId(),
                List.of(member2.getId(), member3.getId()),
                Permission.WRITE);

        // When
        List<MemberResponse> sharedUsers = calendarShareService.getSharedUsers(createdCalendar.getId());

        // Then
        assertThat(sharedUsers).hasSize(2);
    }
}