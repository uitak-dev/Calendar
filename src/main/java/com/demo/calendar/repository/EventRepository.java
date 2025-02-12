package com.demo.calendar.repository;

import com.demo.calendar.domain.entity.Event;
import com.demo.calendar.repository.custom.EventRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
    List<Event> findByCalendarId(Long calendarId);
}
