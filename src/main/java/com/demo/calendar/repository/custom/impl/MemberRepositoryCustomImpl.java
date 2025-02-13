package com.demo.calendar.repository.custom.impl;

import com.demo.calendar.domain.dto.response.MemberResponse;
import com.demo.calendar.domain.dto.search.MemberSearch;
import com.demo.calendar.domain.entity.QMember;
import com.demo.calendar.repository.MemberRepository;
import com.demo.calendar.repository.custom.MemberRepositoryCustom;
import com.demo.calendar.utility.mapper.MemberMapper;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(JPQLTemplates.DEFAULT, em);
    }

    private static final QMember member = QMember.member;

    @Override
    public Page<MemberResponse> searchMemberList(MemberSearch memberSearch, Long memberId, Pageable pageable) {

        List<MemberResponse> content = queryFactory
                .selectFrom(member)
                .where(
                        member.id.ne(memberId),
                        usernameEq(memberSearch.getUsername()),
                        emailEq(memberSearch.getEmail())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch()
                .stream()
                .map(MemberMapper::convertToResponse)
                .toList();

        // 전체 개수 조회 (페이징 처리)
        JPAQuery<Long> countQuery = queryFactory
                .select(member.count())
                .from(member)
                .where(
                        member.id.ne(memberId),
                        usernameEq(memberSearch.getUsername()),
                        emailEq(memberSearch.getEmail())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression usernameEq(String username) {
        // null, isEmpty() 검사
        return StringUtils.hasLength(username) ? member.username.containsIgnoreCase(username) : null;
    }

    private BooleanExpression emailEq(String email) {
        // null, isEmpty() 검사
        return StringUtils.hasLength(email) ? member.email.containsIgnoreCase(email) : null;
    }
}
