package com.samsamhajo.deepground.global.message;

public interface MessagePublisher {

    void convertAndSend(String destination, Object payload);

    void convertAndSendToUser(String user, String destination, Object payload);
}
