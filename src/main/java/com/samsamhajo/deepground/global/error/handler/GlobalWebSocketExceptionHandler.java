package com.samsamhajo.deepground.global.error.handler;

import com.samsamhajo.deepground.global.error.core.ErrorResponse;
import com.samsamhajo.deepground.global.error.core.WebSocketException;
import com.samsamhajo.deepground.global.utils.GlobalLogger;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalWebSocketExceptionHandler {

    @MessageExceptionHandler(WebSocketException.class)
    @SendToUser("/queue/errors")
    public ErrorResponse handleBaseException(WebSocketException e) {
        GlobalLogger.error(e.toString());
        return ErrorResponse.of(e.getErrorCode());
    }

    @MessageExceptionHandler(Exception.class)
    @SendToUser("/queue/errors")
    public ErrorResponse handleException(Exception e) {
        GlobalLogger.error(e.toString());
        return ErrorResponse.of(GlobalWebSocketErrorCode.INTERNAL_SERVER_ERROR);
    }
}
