package com.demo.calendar.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Calendar {

    @Id
    @GeneratedValue
    @Column(name = "calendar_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member owner;

    // 하나의 캘린더는 여러 이벤트를 가짐
    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events = new ArrayList<>();

    // 캘린더 공유 정보
    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CalendarShare> shares = new ArrayList<>();

    @Builder
    public Calendar(Long id, String name, String description, Member owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.events = new ArrayList<>();
        this.shares = new ArrayList<>();
    }

    public void updateInfo(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
