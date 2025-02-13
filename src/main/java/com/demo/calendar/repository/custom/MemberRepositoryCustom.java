package com.demo.calendar.repository.custom;

import com.demo.calendar.domain.dto.response.MemberResponse;
import com.demo.calendar.domain.dto.search.MemberSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {

    public Page<MemberResponse> searchMemberList(MemberSearch memberSearch, Long memberId, Pageable pageable);
}
