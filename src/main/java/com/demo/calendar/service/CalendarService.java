package com.demo.calendar.service;

import com.demo.calendar.domain.dto.request.CalendarRequest;
import com.demo.calendar.domain.dto.response.CalendarResponse;
import com.demo.calendar.domain.entity.Calendar;
import com.demo.calendar.domain.entity.Member;
import com.demo.calendar.repository.CalendarRepository;
import com.demo.calendar.repository.MemberRepository;
import com.demo.calendar.utility.mapper.CalendarMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.demo.calendar.domain.entity.CalendarShare.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final CalendarShareService calendarShareService;
    private final MemberRepository memberRepository;

    /**
     * 캘린더 생성
     */
    public CalendarResponse createCalendar(Long memberId, CalendarRequest calendarRequest) {
        Member owner = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberId));

        Calendar calendar = Calendar.builder()
                .name(calendarRequest.getName())
                .description(calendarRequest.getDescription())
                .owner(owner)
                .build();

        calendarRepository.save(calendar);

        return CalendarMapper.convertToResponse(calendar);
    }

    /**
     * 캘린더 정보 조회.
     */
    public CalendarResponse getCalendar(Long calendarId, Long memberId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new EntityNotFoundException("Calendar not found with id: " + calendarId));

        CalendarShareId calendarShareId = new CalendarShareId(calendarId, memberId);

        // 캘린더의 소유자가 아니고, 해당 캘린더를 공유 받은적도 없는 사용자인 경우, 접근 불가.
        if (calendar.getOwner().getId() != memberId &&
                !calendarShareService.isCalendarShared(calendarShareId)) {
            throw new AccessDeniedException("해당 캘린더에 대한 접근 권한이 없습니다.");
        }

        return CalendarMapper.convertToResponse(calendar);
    }

    /**
     * 사용자가 소유한 캘린더 목록 조회.
     */
    public List<CalendarResponse> getUserCalendars(Long memberId) {
        List<Calendar> myCalendarList = calendarRepository.findByOwnerId(memberId);
        return myCalendarList.stream().map(CalendarMapper::convertToResponse).toList();
    }

    /**
     * 캘린더 수정.
     */
    public CalendarResponse updateCalendar(Long calendarId, Long memberId, CalendarRequest calendarRequest) {

        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new EntityNotFoundException("Calendar not found with id: " + calendarId));

        // 수정 권한 확인( 소유자만 수정 가능 )
        if (!calendar.getOwner().getId().equals(memberId)) {
            throw new IllegalArgumentException("캘린더의 소유자만 수정할 수 있습니다.");
        }

        // 캘린더 정보 수정
        calendar.updateInfo(calendarRequest.getName(), calendarRequest.getDescription());

        // 저장 및 응답 반환
        return CalendarMapper.convertToResponse(calendar);
    }


    /**
     * 캘린더 삭제
     */
    public void deleteCalendar(Long calendarId, Long memberId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new EntityNotFoundException("Calendar not found with id: " + calendarId));

        if (calendar.getOwner().getId() != memberId) {
            throw new AccessDeniedException("캘린더의 소유자만 삭제할 수 있습니다.");
        }

        calendarRepository.delete(calendar);
    }
}
