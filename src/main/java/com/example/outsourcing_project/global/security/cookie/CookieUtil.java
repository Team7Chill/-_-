package com.example.outsourcing_project.global.security.cookie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieUtil {

    // 쿠키 생성 (이름, 값, 유효시간)
    public Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);     // 자바스크립트에서 접근 불가
        cookie.setSecure(false);      // HTTPS 환경이면 true로 변경 필요
        cookie.setPath("/");          // 모든 경로에서 사용 가능
        cookie.setMaxAge(maxAge);     // 쿠키 유효시간 설정(초 단위)
        return cookie;
    }

    // 요청에서 특정 이름의 쿠키 찾기 (Optional 반환)
    public Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst();
    }

    // 쿠키 삭제용 쿠키 생성 (값 null, 유효시간 0으로 설정)
    public Cookie deleteCookie(String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);  // HTTPS 환경이면 true로 변경 필요
        cookie.setPath("/");
        cookie.setMaxAge(0);      // 즉시 만료 처리
        return cookie;
    }

    // 응답에 쿠키 추가 (Set-Cookie 헤더 생성)
    public void addCookie(HttpServletResponse response, Cookie cookie) {
        response.addCookie(cookie);
    }

    // refreshToken 쿠키 생성 후 응답 헤더에 추가
    public void setRefreshTokenCookie(HttpServletResponse response, String token, int maxAge) {
        Cookie cookie = createCookie("refreshToken", token, maxAge);
        addCookie(response, cookie);
    }
}

