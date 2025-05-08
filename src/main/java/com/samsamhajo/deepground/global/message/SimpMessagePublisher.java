package com.samsamhajo.deepground.global.message;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimpMessagePublisher implements MessagePublisher {

    private final SimpMessageSendingOperations operations;

    @Override
    public void convertAndSend(String destination, Object payload) {
        operations.convertAndSend("/topic" + destination, payload);
    }

    @Override
    public void convertAndSendToUser(String user, String destination, Object payload) {
        operations.convertAndSendToUser(user, "/queue" + destination, payload);
    }
}
