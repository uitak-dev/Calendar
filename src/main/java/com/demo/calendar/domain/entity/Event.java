package com.demo.calendar.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    @Id
    @GeneratedValue
    @Column(name = "event_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    private String content;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    private Calendar calendar;

    @Builder
    public Event(Long id, String title, String content, Member owner, LocalDateTime startTime, LocalDateTime endTime, Calendar calendar) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
        this.owner = owner;
        this.calendar = calendar;
    }

    @PrePersist
    @PreUpdate
    private void validateEventTime() {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("종료시간이 시작시간 보다 빠를 수 없습니다.");
        }
    }

    public void updateInfo(String title, String content, LocalDateTime startTime, LocalDateTime endTime) {
        this.title = title;
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
