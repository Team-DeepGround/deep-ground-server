package com.samsamhajo.deepground.email.dto;

import lombok.Getter;

@Getter
public class EmailResponse {

    private String email;
    private boolean isSuccess;

    public EmailResponse(String email, boolean isSuccess) {
        this.email = email;
        this.isSuccess = isSuccess;
    }
}
