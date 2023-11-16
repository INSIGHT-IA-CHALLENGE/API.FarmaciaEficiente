package br.com.fiap.farmacia.eficiente.exception;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RestConflictException extends RuntimeException{
    
    public RestConflictException (String message){
        super(message);
    }
}
