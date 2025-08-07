package com.error.springerrorhandler;

import com.error.springerrorhandler.advice.CustomResponseBodyAdvice;
import com.error.springerrorhandler.advice.CustomResponseEntityExceptionHandler;
import com.error.springerrorhandler.message.CodeMessageSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableConfigurationProperties
@EnableWebMvc
@ComponentScan("com.error.springerrorhandler")
public class SpringErrorHandlerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "messageSource")
    public MessageSource messageSource() {
        return new CodeMessageSource("default", 60000, "classpath:i18n/messages", "classpath:i18n/messages-legacy");
    }
    
    @Bean
    public CustomResponseEntityExceptionHandler customResponseEntityExceptionHandler(MessageSource messageSource) {
        return new CustomResponseEntityExceptionHandler(messageSource);
    }
    
    @Bean
    public CustomResponseBodyAdvice customResponseBodyAdvice() {
        return new CustomResponseBodyAdvice();
    }
}
