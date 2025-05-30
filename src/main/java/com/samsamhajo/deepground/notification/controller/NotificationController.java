package com.samsamhajo.deepground.notification.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.notification.dto.NotificationListRequest;
import com.samsamhajo.deepground.notification.dto.NotificationListResponse;
import com.samsamhajo.deepground.notification.service.NotificationService;
import com.samsamhajo.deepground.notification.success.NotificationSuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<SuccessResponse<NotificationListResponse>> getNotifications(
            @Valid @ModelAttribute NotificationListRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();

        NotificationListResponse notifications = notificationService.getNotifications(
                memberId,
                request.getCursor(),
                request.getLimit()
        );
        return ResponseEntity
                .ok(SuccessResponse.of(NotificationSuccessCode.NOTIFICATION_RETRIEVED, notifications));
    }

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
