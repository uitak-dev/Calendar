package com.demo.calendar.domain.dto.request;

import com.demo.calendar.domain.entity.Permission;
import lombok.Data;

import java.util.List;

@Data
public class CalendarShareRequest {
    private List<Long> memberIds;
    private Permission permission;
}

