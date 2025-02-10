package com.demo.calendar.repository;

import com.demo.calendar.domain.entity.CalendarShare;
import com.demo.calendar.domain.entity.CalendarShare.CalendarShareId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarShareRepository extends JpaRepository<CalendarShare, CalendarShareId> {

    // 특정 사용자가 공유받은 캘린더 목록 조회.
    List<CalendarShare> findByMemberId(Long memberId);

    // 특정 캘린더를 공유 받은 사용자 목록 조회.
    List<CalendarShare> findByCalendarId(Long calendarId);

    // 특정 사용자가 특정 캘린더를 공유 받았는지 확인.
    boolean existsById(CalendarShareId calendarShareId);
}
