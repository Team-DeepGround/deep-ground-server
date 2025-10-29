package com.samsamhajo.deepground.global;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
public abstract class BaseDocument {

    @CreatedDate
    @Field("created_at")
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @Field("modified_at")
    private ZonedDateTime modifiedAt;
}
