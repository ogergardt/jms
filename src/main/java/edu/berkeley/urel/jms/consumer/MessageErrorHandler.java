package edu.berkeley.urel.jms.consumer;

import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class MessageErrorHandler implements ErrorHandler {
   
    @Override
    public void handleError(Throwable t) {
        log.error(t.getMessage(), t);
    }
}
