package com.demo.calendar.domain.dto.search;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventSearch {

    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endTime;
}
