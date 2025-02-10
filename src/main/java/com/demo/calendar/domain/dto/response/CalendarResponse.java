package com.demo.calendar.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalendarResponse {

    private Long id;

    private String name;
    private String description;
    private MemberResponse owner;

    private List<EventResponse> events;
}
