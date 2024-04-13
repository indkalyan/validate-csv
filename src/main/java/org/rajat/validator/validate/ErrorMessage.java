package org.rajat.validator.validate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {

    private ErrorMessageType errorMessageType;
    private String message;

    public enum ErrorMessageType {
        WARN,
        ERROR
    }

}
