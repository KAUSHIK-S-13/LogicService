package com.coherent.unnamed.logic.Exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@Component
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int statusCode;
    private String statusMessage;

    public CustomException(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

}