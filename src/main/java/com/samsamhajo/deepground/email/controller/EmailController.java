package com.samsamhajo.deepground.email.controller;

import com.samsamhajo.deepground.email.dto.EmailRequest;
import com.samsamhajo.deepground.email.dto.EmailResponse;
import com.samsamhajo.deepground.email.dto.EmailVerifyRequest;
import com.samsamhajo.deepground.email.service.EmailService;
import com.samsamhajo.deepground.email.success.EmailSuccessCode;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<SuccessResponse<EmailResponse>> sendVerificationEmail(
            @Valid @RequestBody EmailRequest request
    ) {
        EmailResponse response = emailService.sendVerificationEmail(request);

        return ResponseEntity
                .status(EmailSuccessCode.EMAIL_SENT.getStatus())
                .body(SuccessResponse.of(EmailSuccessCode.EMAIL_SENT, response));
    }

    @PostMapping("/verify")
    public ResponseEntity<SuccessResponse<Void>> verifyEmail(
            @Valid @RequestBody EmailVerifyRequest request
    ) {
        emailService.verifyEmail(request);

        return ResponseEntity
                .status(EmailSuccessCode.EMAIL_VERIFIED.getStatus())
                .body(SuccessResponse.of(EmailSuccessCode.EMAIL_VERIFIED));
    }
}
