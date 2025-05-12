package com.samsamhajo.deepground.friend.Dto;


import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseDto {

    private int status;
    private String message;
    private Object data;

    public ResponseDto(HttpStatus status, String message, Object data) {
        this.status = status.value();
        this.message = message;
        this.data = data;
    }
}
