package com.error.springerrorhandler.message;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;

public class CodeMessageSource extends ReloadableResourceBundleMessageSource {
    private static final String SHORT_MESSAGE_SUFIX = ".short";
    private static final String DEFAULT_MESSAGE = "Error";
    private static final String DEFAULT_MESSAGE_SHORT = "ERROR";
    private final String defaultMessage;
    private final String defaultShortMessage;

    public CodeMessageSource(String defaultCode, long cacheMillis, String... baseNames) {
        this.setBasenames(baseNames);
        this.setCacheMillis(cacheMillis);
        this.setDefaultEncoding(StandardCharsets.UTF_8.name());
        defaultMessage = getMessage(defaultCode, null, DEFAULT_MESSAGE, LocaleContextHolder.getLocale());
        defaultShortMessage = getMessage(defaultCode + SHORT_MESSAGE_SUFIX, null, DEFAULT_MESSAGE_SHORT, LocaleContextHolder.getLocale());
    }

    public MessageInfo getMessage(String code) {
        String message = getMessage(code, null, defaultMessage, LocaleContextHolder.getLocale());
        String shortMessage;
        if (isDefaultMessage(message)) {
            shortMessage = defaultShortMessage;
        } else {
            shortMessage = getMessage(code + SHORT_MESSAGE_SUFIX, null, defaultShortMessage, LocaleContextHolder.getLocale());
        }
        return new MessageInfo(message, shortMessage);
    }

    @Override
    protected String getDefaultMessage(String code) {
        return defaultMessage;
    }

    private boolean isDefaultMessage(String msg) {
        return msg != null && msg.equals(defaultMessage);
    }

    public record MessageInfo(String message, String shortMessage) {
    }
}
