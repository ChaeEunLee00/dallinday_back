package com.by.dallinday.common.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            Map<String, Object> claims = verifyJws(request);
            setAuthenticationToContext(claims);
        }
        catch (SignatureException se) {
            request.setAttribute("exception", se);
        }
        catch (ExpiredJwtException ee) {
            request.setAttribute("exception", ee);
        }
        catch (Exception e) {
            request.setAttribute("exception", e);
        }
        filterChain.doFilter(request,response);
    }


    private Map<String, Object> verifyJws(HttpServletRequest request) { // 요청헤더 토큰추출
        String jws = request.getHeader("Authorization").replace("Bearer ","");
        String base64EncodedSecretKey = jwtTokenizer.encodedBase64SecretKey(jwtTokenizer.getSecretKey());
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();
        return claims;
    }


    private void setAuthenticationToContext(Map<String, Object> claims) { // 사용자 인증•권한정보 생성해서 SecurityContextHolder 에 저장
        Map<String, Object> principal = new HashMap<>();
        log.info("memberId : "+claims.get("memberId"));
        log.info("email : "+claims.get("email"));
        log.info("username : "+claims.get("username"));
        log.info("role : "+claims.get("role"));

        principal.put("memberId", claims.get("memberId"));
        principal.put("email", claims.get("email"));
        principal.put("username", claims.get("username"));
        principal.put("role", claims.get("role"));

        // 마지막 인자로 권한 (GrantedAuthority) 리스트를 줘야 인증된 것으로 인식 -> 그래야 이후 oAuth login 필터가 작동안함
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException { // 토큰이 유효한헤더를 포함하는지 확인
        String authorization = request.getHeader("Authorization");
        return authorization == null || !authorization.startsWith("Bearer");
    }
}
