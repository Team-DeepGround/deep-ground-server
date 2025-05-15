package com.samsamhajo.deepground.auth.controller;

import com.samsamhajo.deepground.auth.dto.RegisterRequest;
import com.samsamhajo.deepground.auth.dto.RegisterResponse;
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
}
