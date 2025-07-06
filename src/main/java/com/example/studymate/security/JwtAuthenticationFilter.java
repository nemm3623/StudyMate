package com.example.studymate.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // request -> 요청의 정보를 담고 있음
        String token = jwtTokenProvider.resolveToken(request);

        // 토큰이 존재하고 토큰이 유효할 경우
        if (token != null && jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsername(token);


            // 사용자 정보를 가져와서 인증 객체 생성
            // UserDetails를 사용하는 이유는 SpringSecurity에서 인증 처리를 하기 위해 사용되는 표준이기 때문
            UserDetails userDetails = userDetailService.loadUserByUsername(username);

            // 인증 토큰 생성
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            //  인증 완료 저장 -> 인증 완료되어 다음 필터 진행 X
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 다음 필터로 진행, 없으면 컨트롤러로 요청을 넘김
        filterChain.doFilter(request, response);
    }
}