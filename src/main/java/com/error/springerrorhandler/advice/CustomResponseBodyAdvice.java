package com.error.springerrorhandler.advice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@RequiredArgsConstructor
public class CustomResponseBodyAdvice extends CodeResponseBodyAdvice {

}
