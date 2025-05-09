package com.samsamhajo.deepground.global.config;

import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableWebSocketSecurity
public class WebSocketSecurityConfig {

    /*
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
     */
}
