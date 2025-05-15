package com.samsamhajo.deepground.auth.controller;

import com.samsamhajo.deepground.auth.dto.RegisterRequest;
import com.samsamhajo.deepground.auth.dto.RegisterResponse;
import com.samsamhajo.deepground.auth.service.AuthService;
import com.samsamhajo.deepground.auth.success.AuthSuccessCode;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
