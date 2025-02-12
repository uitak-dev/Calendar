package com.demo.calendar.repository.custom;

import com.demo.calendar.domain.dto.response.EventResponse;
import com.demo.calendar.domain.dto.search.EventSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventRepositoryCustom {

    public Page<EventResponse> searchEventList(Long calendarId, EventSearch eventSearch, Pageable pageable);
}
