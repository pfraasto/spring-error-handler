package com.error.springerrorhandler.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CodeResponseEntity<T> {
    private String errorCode;
    private String message;
    private String shortMessage;
    private T data;
}