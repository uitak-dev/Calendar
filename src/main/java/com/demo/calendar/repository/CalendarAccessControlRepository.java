package com.demo.calendar.repository;

import com.demo.calendar.domain.entity.CalendarAccessControl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarAccessControlRepository extends JpaRepository<CalendarAccessControl, Long> {

}
