package com.demo.calendar.domain.dto.search;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class EventSearch {

    private String title;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String startTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private String endTime;
}
