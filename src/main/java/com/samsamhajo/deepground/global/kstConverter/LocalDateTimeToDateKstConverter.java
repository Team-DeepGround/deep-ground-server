package com.samsamhajo.deepground.global.kstConverter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@WritingConverter
public class LocalDateTimeToDateKstConverter implements Converter<LocalDateTime, Date> {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    @Override
    public Date convert(LocalDateTime source) {
        if (source == null) return null;
        return Date.from(source.atZone(KST).toInstant());
    }
}