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
    @SuppressWarnings("unchecked")
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if ((request.getURI().getPath().startsWith("/api/") || request.getURI().getPath().startsWith("/public/")) &&
                !(body instanceof CodeResponseEntity) && !(body instanceof InputStreamResource)) {
            @SuppressWarnings("rawtypes")
            CodeResponseEntity codeResponseEntity = new CodeResponseEntity();
            if (body != null && !(body instanceof String)) {
                codeResponseEntity.setData(body);
            }
            return codeResponseEntity;
        }
        return body;
    }
}

