package com.error.springerrorhandler.advice;

import com.error.springerrorhandler.message.CodeMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class CustomResponseEntityExceptionHandler extends CodeResponseEntityExceptionHandler {

    public CustomResponseEntityExceptionHandler(MessageSource messageSource) {
        super((CodeMessageSource) messageSource);
    }
}
