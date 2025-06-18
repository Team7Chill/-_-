package com.example.outsourcing_project.global.security.cookie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieUtil {

    public Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // HTTPS 환경이면 true로
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    public Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst();
    }

    public Cookie deleteCookie(String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // HTTPS 환경이면 true로
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }

    public void addCookie(HttpServletResponse response, Cookie cookie) {
        response.addCookie(cookie);
    }

    public void setRefreshTokenCookie(HttpServletResponse response, String token, int maxAge) {
        Cookie cookie = createCookie("refreshToken", token, maxAge);
        addCookie(response, cookie);
    }
}
