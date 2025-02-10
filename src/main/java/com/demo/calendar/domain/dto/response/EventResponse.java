package com.demo.calendar.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventResponse {

    private Long id;

    private String title;
    private String content;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private CalendarResponse calendar;
}
