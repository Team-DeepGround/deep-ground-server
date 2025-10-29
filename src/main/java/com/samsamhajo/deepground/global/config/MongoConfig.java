package com.samsamhajo.deepground.global.config;

import com.samsamhajo.deepground.global.kstConverter.DateToLocalDateTimeKstConvert;
import com.samsamhajo.deepground.global.kstConverter.LocalDateTimeToDateKstConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
//    @Bean
//    public MongoTransactionManager transactionManager(MongoDatabaseFactory factory) {
//        return new MongoTransactionManager(factory);
//    }

    @Bean
    public  MappingMongoConverter mappingMongoConverter(
            MongoDatabaseFactory mongoDatabaseFactory,
            MongoMappingContext mongoMappingContext,
            LocalDateTimeToDateKstConverter dateKstConverter
//            DateToLocalDateTimeKstConvert localDateTimeKstConverter
    )   {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver (mongoDatabaseFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);

        converter.setCustomConversions(new MongoCustomConversions(
                List.of(dateKstConverter)
        ));

        converter.afterPropertiesSet();

        return converter;

    }
}
