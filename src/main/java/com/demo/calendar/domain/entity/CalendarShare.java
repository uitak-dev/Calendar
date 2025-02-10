package com.demo.calendar.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CalendarShare {

    @EmbeddedId
    private CalendarShareId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("calendarId")
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;  // 공유 되는 캘린더.

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private Member member;  // 공유 받은 사용자.

    @OneToMany(mappedBy = "calendarShare", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CalendarAccessControl> accessControls = new ArrayList<>();

    @Builder
    public CalendarShare(CalendarShareId calendarShareId, Calendar calendar, Member member) {
        this.id = calendarShareId;
        this.calendar = calendar;
        this.member = member;
        this.accessControls = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CalendarShare that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // 연관관계 편의 메서드
    public void addPermission(Permission permission) {
        CalendarAccessControl calendarAccessControl = new CalendarAccessControl(this, permission);
        accessControls.add(calendarAccessControl);
    }


    @Embeddable
    @Data
    @NoArgsConstructor
    public static class CalendarShareId {

        private Long calendarId;    // 공유 되는 캘린더 UUID.
        private Long memberId;      // 공유 받은 사용자 UUID.

        @Builder
        public CalendarShareId(Long calendarId, Long memberId) {
            this.calendarId = calendarId;
            this.memberId = memberId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CalendarShareId that)) return false;
            return Objects.equals(calendarId, that.calendarId) && Objects.equals(memberId, that.memberId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(calendarId, memberId);
        }
    }
}
