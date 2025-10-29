package com.samsamhajo.deepground.global.kstConverter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

@Component
@ReadingConverter
public class DateToLocalDateTimeKstConvert implements Converter<Date, LocalDateTime> {

//    @Override
//    public LocalDateTime convert(Date source) {
//        if (source == null) return null;
//        return LocalDateTime.ofInstant(source.toInstant(), ZoneOffset.UTC);
//    }

    private static final ZoneId KST = ZoneId.of("Asia/Seoul"); // KST 정의

    @Override
    public LocalDateTime convert(Date source) {
        if (source == null) return null;

        // DB의 UTC Date를 KST LocalDateTime으로 변환
        return source.toInstant().atZone(KST).toLocalDateTime();
    }
}



