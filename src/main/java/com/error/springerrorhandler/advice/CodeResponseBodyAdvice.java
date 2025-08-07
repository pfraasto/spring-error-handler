package com.error.springerrorhandler.advice;

import com.error.springerrorhandler.model.CodeResponseEntity;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

public abstract class CodeResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        String path = request.getURI().getPath();

        if ((path.startsWith("/api/") || path.startsWith("/public/"))
                && !(body instanceof CodeResponseEntity)
                && !(body instanceof InputStreamResource)) {

            CodeResponseEntity<Object> wrapper = new CodeResponseEntity<>();
            if (body != null && !(body instanceof String)) {
                wrapper.setData(body);
            }
            return wrapper;
        }

        return body;
    }
}

