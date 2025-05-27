package com.samsamhajo.deepground.notification.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.notification.service.NotificationService;
import com.samsamhajo.deepground.notification.success.NotificationSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<SuccessResponse<?>> readNotification(
            @PathVariable String notificationId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();

        notificationService.readNotification(notificationId, memberId);
        return ResponseEntity
                .ok(SuccessResponse.of(NotificationSuccessCode.NOTIFICATION_READ));
    }

    @PatchMapping("/read-all")
    public ResponseEntity<SuccessResponse<?>> readAllNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();

        notificationService.readAllNotifications(memberId);
        return ResponseEntity
                .ok(SuccessResponse.of(NotificationSuccessCode.NOTIFICATION_READ));
    }
}
