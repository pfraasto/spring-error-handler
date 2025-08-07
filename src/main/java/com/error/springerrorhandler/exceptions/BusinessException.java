package com.error.springerrorhandler.exceptions;

import lombok.Getter;

@Getter
public class BusinessException extends CodeException {
    private static final int DEFAULT_HTTP_CODE = 400;

    public BusinessException(String codeError) {
        super(codeError, DEFAULT_HTTP_CODE);
    }

    public BusinessException(String codeError, String message, String shortMessage) {
        super(codeError, message);
        this.shortMessage = shortMessage;
    }

    public BusinessException(Throwable cause, String codeError) {
        super(codeError, cause);
    }
}
