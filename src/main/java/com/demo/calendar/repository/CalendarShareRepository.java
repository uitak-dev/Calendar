package com.demo.calendar.repository;

import com.demo.calendar.domain.entity.CalendarShare;
import com.demo.calendar.domain.entity.CalendarShare.CalendarShareId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalendarShareRepository extends JpaRepository<CalendarShare, CalendarShareId> {

    // 특정 사용자가 공유받은 캘린더 목록 조회.
    @Query("select cs from CalendarShare cs join fetch cs.calendar where cs.member.id = :memberId")
    List<CalendarShare> findByMemberIdWithFetchJoin(@Param("memberId") Long memberId);

    // 특정 캘린더를 공유 받은 사용자 목록 조회.
    @Query("select cs from CalendarShare cs join fetch cs.member where cs.calendar.id = :calendarId")
    List<CalendarShare> findByCalendarIdWithFetchJoin(@Param("calendarId") Long calendarId);

    // 특정 사용자가 특정 캘린더를 공유 받았는지 확인.
    boolean existsById(CalendarShareId calendarShareId);
}
