package br.com.fiap.farmacia.eficiente.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RestValueInvalidException extends RuntimeException {
     
    public RestValueInvalidException(String message) {
        super(message);
    }
}
