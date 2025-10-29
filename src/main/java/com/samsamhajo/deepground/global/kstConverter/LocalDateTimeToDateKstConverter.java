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

    private static final int KST_OFFSET_HOURS = 9;

    @Override
    public Date convert(LocalDateTime source) {
        return convertToKst(source);
    }
    private Date convertToKst(LocalDateTime localDateTime) {
        return  Timestamp.valueOf(localDateTime.plusHours(KST_OFFSET_HOURS));
    }
}
