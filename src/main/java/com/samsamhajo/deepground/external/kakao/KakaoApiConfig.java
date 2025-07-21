package com.samsamhajo.deepground.external.kakao;


import com.samsamhajo.deepground.external.ExternalApiCaller;
import com.samsamhajo.deepground.global.utils.GlobalLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakaoApiConfig {
    @Bean
    public KakaoApiCaller kakaoApiCaller(
            @Value("${kakao-api.rest-client}") String restApiKey,
            @Value("${kakao-api.host}") String host,
            ExternalApiCaller externalApiCaller
    ) {
        GlobalLogger.info("KakaoApiConfig", "Kakao API REST Client Key:", restApiKey);
        return new KakaoApiCaller(restApiKey, host, externalApiCaller);
    }
}
