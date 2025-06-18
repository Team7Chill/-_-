package com.example.outsourcing_project.global.security.cookie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Optional;

public class CookieUtil {

    private CookieUtil() {
        // 유틸 클래스라 인스턴스 생성 막기
    }

    public static Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // HTTPS 환경이면 변경하세요
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst();
    }

    public static Cookie deleteCookie(String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // HTTPS 환경이면 true
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }

    public static void addCookie(HttpServletResponse response, Cookie cookie) {
        response.addCookie(cookie);
    }

    // refreshToken 쿠키 생성 + 응답 헤더에 추가
    public static void setRefreshTokenCookie(HttpServletResponse response, String token, int maxAge) {
        Cookie cookie = createCookie("refreshToken", token, maxAge);
        addCookie(response, cookie);
    }
}
