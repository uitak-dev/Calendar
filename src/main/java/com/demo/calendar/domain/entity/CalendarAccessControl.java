package com.demo.calendar.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CalendarAccessControl {

    @Id
    @GeneratedValue
    @Column(name = "calendar_access_control_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "calendar_id", referencedColumnName = "calendar_id"),
            @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    })
    private CalendarShare calendarShare;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Permission permission;

    /**
     * 권한 부여 기간, 승인 여부 등
     * 추가적인 속성 확장 가능.
     */

    public CalendarAccessControl(CalendarShare calendarShare, Permission permission) {
        this.calendarShare = calendarShare;
        this.permission = permission;
    }
}
