package com.samsamhajo.deepground.global.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    @Bean
    public JavaTimeModule javaTimeModule() {
        JavaTimeModule module = new JavaTimeModule();

        // JSON -> LocalDateTime (역직렬화) 규칙을 KST 기준으로 오버라이드합니다.
        module.addDeserializer(LocalDateTime.class, new KstLocalDateTimeDeserializer());
        return module;
    }

    /**
     * 클라이언트가 보낸 JSON 문자열(UTC "Z" 포함)을
     * KST 기준의 LocalDateTime으로 변환하는 커스텀 로직
     */
    static class KstLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

        protected KstLocalDateTimeDeserializer() { super(LocalDateTime.class); }

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String text = p.getText();
            try {
                // "2025-10-29T08:00:00Z" (Z 정보가 있는 경우)
                ZonedDateTime zdt = ZonedDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME);

                // KST 시간대로 변환 (08:00Z -> 17:00+09:00)
                ZonedDateTime kstZdt = zdt.withZoneSameInstant(KST);

                // KST의 LocalDateTime 값(17:00)을 반환
                return kstZdt.toLocalDateTime();

            } catch (Exception e) {
                // "2025-10-29T17:00:00" (Z 정보가 없는 경우 등)
                // KST라고 가정하고 그대로 파싱 (예외 처리)
                return LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }
        }
    }
}
