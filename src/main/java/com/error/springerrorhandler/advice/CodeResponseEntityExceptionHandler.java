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

    @ExceptionHandler(value = {CodeException.class})
    protected ResponseEntity<Object> handlerCodeCore(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handlerException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
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

        if (ex instanceof CustomHttpResponseException mainException) {
            HttpStatusCode httpStatus = mainException.getStatus() == null ? status : mainException.getStatus();
            return new ResponseEntity<>(mainException.getPayload(), headers, httpStatus);
        }

        CodeResponseEntity<Object> codeResponseEntity = new CodeResponseEntity<>();
        CodeMessageSource.MessageInfo message = codeMessageSource.getMessage(errorCode);
        codeResponseEntity.setMessage(message.message());
        if (shortMessage == null || !shortMessage.equals(ERROR_INESPERADO)) {
            codeResponseEntity.setShortMessage(message.shortMessage());
        } else {
            codeResponseEntity.setShortMessage(shortMessage);
        }
        codeResponseEntity.setErrorCode(errorCode);

        if (ex instanceof MethodArgumentNotValidException argumentNotValidException) {
            List<String> validationErrors = argumentNotValidException.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(error -> String.format("%s", error.getDefaultMessage()))
                    .toList();

            codeResponseEntity.setData(validationErrors);
        }

        log.error("code: [{}] message: [{}]", codeResponseEntity.getErrorCode(), codeResponseEntity.getMessage(), ex);
        return new ResponseEntity<>(codeResponseEntity, headers, finalStatus);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String errorCode = "default";

        CodeResponseEntity<Object> codeResponseEntity = new CodeResponseEntity<>();
        CodeMessageSource.MessageInfo message = codeMessageSource.getMessage(errorCode);
        codeResponseEntity.setMessage(message.message());
        codeResponseEntity.setShortMessage(message.shortMessage());
        codeResponseEntity.setErrorCode(errorCode);

        return new ResponseEntity<>(codeResponseEntity, headers, status);
    }
}

