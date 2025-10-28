package com.samsamhajo.deepground.global.kstConverter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@WritingConverter
public class DateToLocalDateTimeKstConvert implements Converter<Date, LocalDateTime> {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    @Override
    public LocalDateTime convert(Date source) {
        if (source == null) {
            return null;
        }
        return source.toInstant().atZone(KST).toLocalDateTime();
    }
}



