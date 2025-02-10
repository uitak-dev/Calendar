package com.demo.calendar.utility.mapper;

import com.demo.calendar.domain.dto.request.CalendarRequest;
import com.demo.calendar.domain.dto.response.CalendarResponse;
import com.demo.calendar.domain.entity.Calendar;

public class CalendarMapper {

    public static Calendar convertToEntity(CalendarRequest calendarRequest) {
        return Calendar.builder()
                .id(calendarRequest.getId())
                .name(calendarRequest.getName())
                .description(calendarRequest.getDescription())
                .build();
    }

    public static CalendarResponse convertToResponse(Calendar calendar) {
        return CalendarResponse.builder()
                .id(calendar.getId())
                .name(calendar.getName())
                .description(calendar.getDescription())
                .owner(MemberMapper.convertToResponse(calendar.getOwner()))
                .build();
    }
}
