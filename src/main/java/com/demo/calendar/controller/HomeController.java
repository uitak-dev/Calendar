package com.demo.calendar.controller;

import com.demo.calendar.service.CalendarService;
import com.demo.calendar.service.CalendarShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/calendar/create")
    public String createCalendarPage() {
        return "calendar-create";
    }

    @GetMapping("/event/create")
    public String createEventPage() {
        return "event-create";
    }

    @GetMapping("/calendar/*/detail")
    public String calendarDetailPage() {
        return "calendar-detail";
    }

    @GetMapping("/event/*/detail")
    public String eventDetailPage() {
        return "event-detail";
    }

    @GetMapping("/calendar/edit")
    public String calendarEditPage() {
        return "calendar-edit";
    }

    @GetMapping("/event/edit")
    public String eventEditPage() {
        return "event-edit";
    }

    @GetMapping("/calendar/share")
    public String calendarSharePage() {
        return "calendar-share";
    }
}
