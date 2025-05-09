package com.samsamhajo.deepground.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager.Builder;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig {

    @Bean
    public ChannelInterceptor csrfChannelInterceptor() {
        return new ChannelInterceptor() {
        };
    }

    @Bean
    AuthorizationManager<Message<?>> authorizationManager(Builder authorizationManager) {
        authorizationManager
                .nullDestMatcher().permitAll()
                // TODO: simpSubscribeDestMatchers
                .anyMessage().denyAll();
        return authorizationManager.build();
    }
}
