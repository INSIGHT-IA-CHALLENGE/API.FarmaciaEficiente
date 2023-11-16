package br.com.fiap.farmacia.eficiente.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)    
public class RestNotFoundException extends RuntimeException {
        
    public RestNotFoundException (String message){
        super(message);
    }
    
}
