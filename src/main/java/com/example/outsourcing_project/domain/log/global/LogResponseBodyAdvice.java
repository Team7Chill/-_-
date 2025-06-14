package com.example.outsourcing_project.domain.log.global;

import com.example.outsourcing_project.domain.log.domain.model.Log;
import com.example.outsourcing_project.domain.log.domain.repository.LogRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Component
@AllArgsConstructor
public class LogResponseBodyAdvice  implements ResponseBodyAdvice<Object> {

    private final LogRepository logRepository;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        // 현재 HTTP 요청 정보를 가져오기
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        String method = attributes.getRequest().getMethod();
        String url = attributes.getRequest().getRequestURL().toString();

        assert body != null;
        saveLogToDb(url, method, body.toString());

        return null;
    }


    private void saveLogToDb(String url, String method, String responseBody) {
        // 로그 객체 생성
        Log log = new Log();

        // 로그를 DB에 저장
        logRepository.save(log);
    }
}
