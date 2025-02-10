package com.demo.calendar.repository;

import com.demo.calendar.domain.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    List<Calendar> findByOwnerId(Long ownerId);
}
