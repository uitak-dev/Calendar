package com.demo.calendar.security;

import com.demo.calendar.domain.dto.MemberDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 어떤 요청에 대해서 해당 필터가 수행 되어야 할지 정의해야 한다.
    public RestAuthenticationFilter(HttpSecurity http) {
        // 어떤 요청에 대해서 해당 필터가 수행 되어야 할지 정의해야 한다.
        super(new AntPathRequestMatcher("/api/login", "POST"));
        setSecurityContextRepository(getSecurityContextRepository(http));
    }

    // 인증 정보를 세션에 저장하기 위해, HttpSessionSecurityContextRepository 를 추가한다.
    private static SecurityContextRepository getSecurityContextRepository(HttpSecurity http) {
        SecurityContextRepository securityContextRepository = http.getSharedObject(SecurityContextRepository.class);
        if (securityContextRepository == null) {
            securityContextRepository = new DelegatingSecurityContextRepository(
                    new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository()
            );
        }
        return securityContextRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        // json 요청 이면서, POST 방식인 경우에만 인증 처리.
        if (!HttpMethod.POST.matches(request.getMethod()) ||
                !request.getHeader("content-type").startsWith("application/json")) {
            throw new IllegalArgumentException("Authentication method is not supported");
        }

        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        MemberDto memberDto = objectMapper.readValue(messageBody, MemberDto.class);

        // 요청에 ID와 PW가 없는 경우.
        if (!StringUtils.hasText(memberDto.getUsername()) || !StringUtils.hasText(memberDto.getPassword())) {
            throw new AuthenticationServiceException("Username or Password is not provided");
        }

        // 인증 받기 전에 ID와 PW로 인증 객체 생성.
        RestAuthenticationToken authenticationToken = new RestAuthenticationToken(memberDto.getUsername(), memberDto.getPassword());

        // 인증 처리는 AuthenticationManager 에게 위임, 인증 객체 반환.
        return getAuthenticationManager().authenticate(authenticationToken);
    }
}
