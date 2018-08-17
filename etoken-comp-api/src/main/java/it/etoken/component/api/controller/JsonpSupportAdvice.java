package it.etoken.component.api.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

@ControllerAdvice
public class JsonpSupportAdvice extends AbstractJsonpResponseBodyAdvice {  
    public JsonpSupportAdvice() {  
        super("callback");  
    }  
}  