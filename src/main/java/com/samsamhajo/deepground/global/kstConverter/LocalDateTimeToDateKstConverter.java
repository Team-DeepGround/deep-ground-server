package com.samsamhajo.deepground.global.kstConverter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@ReadingConverter
public class LocalDateTimeToDateKstConverter implements Converter<Date, LocalDateTime> {

    private static final int KST_OFFSET_HOURS = 9;

    @Override
    public LocalDateTime convert(Date source) {
        return convertToKst(source);
    }

    private LocalDateTime convertToKst(Date date){
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.minusHours(KST_OFFSET_HOURS);
    }
}
