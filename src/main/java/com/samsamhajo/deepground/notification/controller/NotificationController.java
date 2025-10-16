package com.samsamhajo.deepground.notification.controller;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.global.success.SuccessResponse;
import com.samsamhajo.deepground.notification.dto.NotificationListRequest;
import com.samsamhajo.deepground.notification.dto.NotificationListResponse;
import com.samsamhajo.deepground.notification.dto.NotificationUnreadResponse;
import com.samsamhajo.deepground.notification.service.NotificationService;
import com.samsamhajo.deepground.notification.success.NotificationSuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
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

    @GetMapping("/unread-count")
    public ResponseEntity<SuccessResponse<?>> getUnreadCount(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getId();

        NotificationUnreadResponse response = notificationService.getUnreadCount(memberId);
        return ResponseEntity
                .ok(SuccessResponse.of(NotificationSuccessCode.NOTIFICATION_UNREAD_COUNT_RETRIEVED, response));
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<SuccessResponse> deleteNotification(
            @PathVariable String notificationId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        notificationService.deleteNotification(userDetails.getMember().getId(), notificationId);
        return ResponseEntity
                .ok(SuccessResponse.of(NotificationSuccessCode.NOTIFICATION_SUCCESS_DELETED));
    }
}
