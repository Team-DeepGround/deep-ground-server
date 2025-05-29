package com.samsamhajo.deepground.auth.controller;

import com.samsamhajo.deepground.auth.dto.*;
import com.samsamhajo.deepground.auth.service.AuthService;
import com.samsamhajo.deepground.auth.success.AuthSuccessCode;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        Long memberId = authService.register(request);
        RegisterResponse response = new RegisterResponse(memberId, true);

        return ResponseEntity
                .status(AuthSuccessCode.REGISTER_SUCCESS.getStatus())
                .body(SuccessResponse.of(AuthSuccessCode.REGISTER_SUCCESS, response));
    }

    @GetMapping("/check-email")
    public ResponseEntity<SuccessResponse<Void>> checkEmailDuplicate(@RequestParam String email) {
        authService.checkEmailDuplicate(email);
        return ResponseEntity
                .status(AuthSuccessCode.EMAIL_AVAILABLE.getStatus())
                .body(SuccessResponse.of(AuthSuccessCode.EMAIL_AVAILABLE));
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<SuccessResponse<Void>> checkNicknameDuplicate(@RequestParam String nickname) {
        authService.checkNicknameDuplicate(nickname);
        return ResponseEntity
                .status(AuthSuccessCode.NICKNAME_AVAILABLE.getStatus())
                .body(SuccessResponse.of(AuthSuccessCode.NICKNAME_AVAILABLE));
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        LoginResponse response = authService.login(request);
        return ResponseEntity
                .status(AuthSuccessCode.LOGIN_SUCCESS.getStatus())
                .body(SuccessResponse.of(AuthSuccessCode.LOGIN_SUCCESS, response));
    }

    @PostMapping("/password/reset/send")
    public ResponseEntity<SuccessResponse<PasswordResetEmailResponse>> sendPasswordResetEmail(
            @Valid @RequestBody PasswordResetRequest request
    ) {
        PasswordResetEmailResponse response = authService.sendPasswordResetEmail(request);
        return ResponseEntity
                .status(AuthSuccessCode.PASSWORD_RESET_EMAIL_SENT.getStatus())
                .body(SuccessResponse.of(AuthSuccessCode.PASSWORD_RESET_EMAIL_SENT, response));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<SuccessResponse<PasswordResetResponse>> resetPassword(
            @Valid @RequestBody PasswordResetVerifyRequest request
    ) {
        PasswordResetResponse response = authService.resetPassword(request);
        return ResponseEntity
                .status(AuthSuccessCode.PASSWORD_RESET_SUCCESS.getStatus())
                .body(SuccessResponse.of(AuthSuccessCode.PASSWORD_RESET_SUCCESS, response));
    }
}
