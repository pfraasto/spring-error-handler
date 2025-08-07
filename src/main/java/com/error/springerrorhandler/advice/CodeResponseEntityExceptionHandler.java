package com.error.springerrorhandler.advice;

import com.error.springerrorhandler.exceptions.CodeException;
import com.error.springerrorhandler.exceptions.CustomHttpResponseException;
import com.error.springerrorhandler.message.CodeMessageSource;
import com.error.springerrorhandler.model.CodeResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.util.List;

@Slf4j
public abstract class CodeResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    protected final CodeMessageSource codeMessageSource;
    private static final String ERROR_INESPERADO = "ERROR INESPERADO";

    protected CodeResponseEntityExceptionHandler(CodeMessageSource codeMessageSource) {
        this.codeMessageSource = codeMessageSource;
    }

    @ExceptionHandler(CodeException.class)
    protected ResponseEntity<Object> handleCodeException(CodeException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode status, WebRequest request) {
        String errorCode = "default";
        String shortMessage = null;
        HttpStatusCode finalStatus = status;

        if (ex instanceof CodeException codeException) {
            errorCode = codeException.getCode();
            shortMessage = codeException.getShortMessage();
            if (codeException.getHttpCode() != null) {
                finalStatus = HttpStatus.resolve(codeException.getHttpCode());
            }
        }

        if (ex instanceof CustomHttpResponseException customEx) {
            HttpStatusCode httpStatus = customEx.getStatus() != null ? customEx.getStatus() : status;
            return new ResponseEntity<>(customEx.getPayload(), headers, httpStatus);
        }

        CodeMessageSource.MessageInfo message = codeMessageSource.getMessage(errorCode);
        CodeResponseEntity<Object> response = new CodeResponseEntity<>();
        response.setMessage(message.message());
        response.setShortMessage(ERROR_INESPERADO.equals(shortMessage) ? shortMessage : message.shortMessage());
        response.setErrorCode(errorCode);

        if (ex instanceof MethodArgumentNotValidException validationEx) {
            List<String> validationErrors = validationEx.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .toList();
            response.setData(validationErrors);
        }

        log.error("code: [{}] message: [{}]", errorCode, response.getMessage(), ex);
        return new ResponseEntity<>(response, headers, finalStatus);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        String errorCode = "default";
        CodeMessageSource.MessageInfo message = codeMessageSource.getMessage(errorCode);

        CodeResponseEntity<Object> response = new CodeResponseEntity<>();
        response.setMessage(message.message());
        response.setShortMessage(message.shortMessage());
        response.setErrorCode(errorCode);

        return new ResponseEntity<>(response, headers, status);
    }
}

