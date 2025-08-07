package com.error.springerrorhandler.exceptions;


import lombok.Getter;

@Getter
public class CodeException extends RuntimeException {
    private final String code;
    protected String shortMessage;
    protected Integer httpCode;

    private static final String FORMAT_EXCEPTION = "[%s] %s";

    public CodeException(String code, String msg, Throwable e) {
        super(String.format(FORMAT_EXCEPTION, code, msg), e);
        this.code = code;
    }

    public CodeException(String code, String msg) {
        super(String.format(FORMAT_EXCEPTION, code, msg));
        this.code = code;
    }

    public CodeException(String code, String msg, String shortMessage) {
        super(String.format(FORMAT_EXCEPTION, code, msg));
        this.code = code;
        this.shortMessage = shortMessage;
    }

    public CodeException(String code) {
        super(String.format("[%s]", code));
        this.code = code;
    }

    public CodeException(String code, Integer httpCode) {
        super(String.format("[%s]", code));
        this.code = code;
        this.httpCode = httpCode;
    }

    public CodeException(String msg, Throwable e) {
        this("DEFAULT", msg, e);
    }
}

