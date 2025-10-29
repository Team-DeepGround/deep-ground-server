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

    private static final int KST_OFFSET_HOURS = 9;

    @Override
    public LocalDateTime convert(Date source) {
        return convertToKst(source);
    }
    private LocalDateTime convertToKst(Date date) {
        LocalDateTime utcDateTime = LocalDateTime.ofInstant(
                date.toInstant(),
                ZoneOffset.UTC
        );
        return utcDateTime.minusHours(KST_OFFSET_HOURS);
    }
}



