package com.demo.calendar.utility.mapper;

import com.demo.calendar.domain.dto.request.EventRequest;
import com.demo.calendar.domain.dto.response.EventResponse;
import com.demo.calendar.domain.entity.Event;

public class EventMapper {

    public static Event convertToEntity(EventRequest eventRequest) {

        return Event.builder()
                .id(eventRequest.getId())
                .title(eventRequest.getTitle())
                .content(eventRequest.getContent())
                .startTime(eventRequest.getStartTime())
                .endTime(eventRequest.getEndTime())
                .build();
                // 캘린더에 등록하는 로직은 서비스 계층에서 추가.
    }

    public static EventResponse convertToResponse(Event event) {

        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .content(event.getContent())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .calendar(CalendarMapper.convertToResponse(event.getCalendar()))
                .build();
    }
}
