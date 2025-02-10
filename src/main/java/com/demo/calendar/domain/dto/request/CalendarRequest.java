package com.demo.calendar.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalendarRequest {

    private Long id;
    private String name;
    private String description;

    public CalendarRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
