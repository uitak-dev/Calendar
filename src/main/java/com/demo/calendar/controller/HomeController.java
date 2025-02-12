package com.demo.calendar.controller;

import com.demo.calendar.domain.dto.response.CalendarResponse;
import com.demo.calendar.security.MemberContext;
import com.demo.calendar.service.CalendarService;
import com.demo.calendar.service.CalendarShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final CalendarService calendarService;
    private final CalendarShareService calendarShareService;

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal MemberContext memberContext) {

        if (memberContext != null) {
            Long memberId = memberContext.getMemberDto().getId();
            List<CalendarResponse> userCalendars = calendarService.getUserCalendars(memberId);
            List<CalendarResponse> sharedCalendars = calendarShareService.getSharedCalendars(memberId);

            model.addAttribute("userCalendars", userCalendars);
            model.addAttribute("sharedCalendars", sharedCalendars);
        }
        else {
            model.addAttribute("userCalendars", Collections.emptyList());
            model.addAttribute("sharedCalendars", Collections.emptyList());
        }

        return "home";
    }
}
