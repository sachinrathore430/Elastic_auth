package com.elasticsearch.authentication.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class LoggingException extends Exception {

    private static final long serialVersionUID = 1L;

    private String code;
    private String message;
    private Object detailedError;

    public LoggingException(String code, String message, Object detailedError){
        super(message);
        this.code = code;
        this.message = message;
        this.detailedError = detailedError;
    }

    public LoggingException(String code, String message){
        super(message);
        this.code = code;
        this.message = message;
    }
}
