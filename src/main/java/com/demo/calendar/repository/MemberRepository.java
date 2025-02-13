package com.demo.calendar.repository;

import com.demo.calendar.domain.entity.Member;
import com.demo.calendar.repository.custom.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByUsername(String username);
}
