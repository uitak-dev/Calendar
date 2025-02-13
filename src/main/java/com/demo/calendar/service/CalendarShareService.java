package com.demo.calendar.service;

import com.demo.calendar.domain.dto.response.CalendarResponse;
import com.demo.calendar.domain.dto.response.MemberResponse;
import com.demo.calendar.domain.entity.*;
import com.demo.calendar.domain.entity.CalendarShare.CalendarShareId;
import com.demo.calendar.repository.CalendarRepository;
import com.demo.calendar.repository.CalendarShareRepository;
import com.demo.calendar.repository.MemberRepository;
import com.demo.calendar.utility.mapper.CalendarMapper;
import com.demo.calendar.utility.mapper.MemberMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CalendarShareService {

    private final CalendarShareRepository calendarShareRepository;
    private final CalendarRepository calendarRepository;
    private final MemberRepository memberRepository;

    /**
     * 여러 사용자에게 캘린더 공유.(자동으로 하위 권한 포함)
     */
    public void shareCalendar(Long calendarId, Long loggedInMemberId, List<Long> memberIdList, Permission permission) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new EntityNotFoundException("Calendar not found with id: " + calendarId));

        if (calendar.getOwner().getId() != loggedInMemberId) {
            throw new AccessDeniedException("캘린더의 소유주만 공유할 수 있습니다.");
        }

        List<Member> members = memberIdList.stream()
                .map(memberId -> {
                    return memberRepository.findById(memberId)
                            .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + memberId));
                })
                .toList();

        members.forEach(member -> {
            CalendarShareId calendarShareId = new CalendarShareId(calendar.getId(), member.getId());

            // 중복 공유 방지
            if (calendarShareRepository.existsById(calendarShareId)) return;

            CalendarShare calendarShare = new CalendarShare(calendarShareId, calendar, member);

            // 권한 자동 확장 후 저장
            Set<Permission> permissionSet = permission.getInheritedPermissions();
            permissionSet.forEach(calendarShare::addPermission);

            calendarShareRepository.save(calendarShare);
        });
    }

    /**
     * 특정 사용자에게 공유된 캘린더 목록 조회.
     */
    @Transactional(readOnly = true)
    public List<CalendarResponse> getSharedCalendars(Long memberId) {
        List<CalendarShare> sharedCalendars = calendarShareRepository.findByMemberIdWithFetchJoin(memberId);
        return sharedCalendars.stream()
                .map(calendarShare -> CalendarMapper.convertToResponse(calendarShare.getCalendar()))
                .toList();
    }

    /**
     * 특정 캘린더를 공유 중인 사용자 정보 조회.
     */
    @Transactional(readOnly = true)
    public List<MemberResponse> getSharedUsers(Long calendarId) {
        List<CalendarShare> sharedCalendars = calendarShareRepository.findByCalendarIdWithFetchJoin(calendarId);
        return sharedCalendars.stream()
                .map(calendarShare -> MemberMapper.convertToResponse(calendarShare.getMember()))
                .toList();
    }

    /**
     * 캘린더 공유 해제.
     */
    public void unshareCalendar(Long calendarId, Long ownerId, List<Long> memberIds) {

        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new EntityNotFoundException("Calendar not found with id: " + calendarId));

        if (!calendar.getOwner().getId().equals(ownerId)) {
            throw new AccessDeniedException("캘린더의 소유자가 아닙니다.");
        }

        List<CalendarShareId> shareIds = memberIds.stream()
                .map(memberId -> new CalendarShareId(calendarId, memberId))
                .collect(Collectors.toList());

        calendarShareRepository.deleteAllById(shareIds);
    }

    /**
     * 사용자가 특정 캘린더를 공유 받았는지 확인.
     */
    @Transactional(readOnly = true)
    public Boolean isCalendarShared(CalendarShareId calendarShareId) {
        return calendarShareRepository.existsById(calendarShareId);
    }
}
