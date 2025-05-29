package com.samsamhajo.deepground.notification.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.samsamhajo.deepground.global.message.MessagePublisher;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.notification.dto.NotificationListResponse;
import com.samsamhajo.deepground.notification.dto.NotificationResponse;
import com.samsamhajo.deepground.notification.entity.Notification;
import com.samsamhajo.deepground.notification.entity.NotificationData;
import com.samsamhajo.deepground.notification.entity.data.FriendNotificationData;
import com.samsamhajo.deepground.notification.repository.NotificationDataRepository;
import com.samsamhajo.deepground.notification.repository.NotificationRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private MessagePublisher messagePublisher;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationDataRepository notificationDataRepository;

    @Test
    @DisplayName("수신자에게 알림을 전송한다")
    void sendNotification_success() {
        // given
        Long receiverId = 1L;
        Member member = mock(Member.class);
        NotificationData data = FriendNotificationData.request(member);

        // when
        notificationService.sendNotification(receiverId, data);

        // then
        verify(notificationDataRepository).save(data);

        ArgumentCaptor<List<Notification>> captor = ArgumentCaptor.forClass(List.class);
        verify(notificationRepository).saveAll(captor.capture());

        List<Notification> savedNotifications = captor.getValue();
        assertThat(savedNotifications).hasSize(1);

        verify(messagePublisher).convertAndSendToUser(
                eq(String.valueOf(receiverId)),
                eq("/notifications"),
                any(NotificationResponse.class)
        );
    }

    @Nested
    @DisplayName("알림 목록 조회")
    public class GetNotification {

        private final Long receiverId = 1L;
        private final LocalDateTime cursor = LocalDateTime.now();
        private final int limit = 10;

        @Test
        @DisplayName("다음 페이지가 존재한다")
        void getNotifications_hasNextTrue_success() {
            // given
            List<Notification> notifications = IntStream.range(0, limit + 1)
                    .mapToObj(i -> mock(Notification.class))
                    .toList();

            when(notifications.get(notifications.size() - 2).getCreatedAt())
                    .thenReturn(cursor);
            when(notificationRepository.findByReceiverIdWithCursor(eq(receiverId), eq(cursor), eq(limit)))
                    .thenReturn(notifications);

            // when
            NotificationListResponse response = notificationService.getNotifications(receiverId, cursor, limit);

            // then
            assertThat(response.getNotifications()).hasSize(limit);
            assertThat(response.isHasNext()).isTrue();
            assertThat(response.getNextCursor()).isNotNull();
        }

        @Test
        @DisplayName("다음 페이지가 존재하지 않는다")
        void getNotifications_hasNextFalse_success() {
            // given
            int size = 5;
            List<Notification> notifications = IntStream.range(0, size)
                    .mapToObj(i -> mock(Notification.class))
                    .toList();

            when(notifications.get(size - 1).getCreatedAt())
                    .thenReturn(cursor);
            when(notificationRepository.findByReceiverIdWithCursor(eq(receiverId), eq(cursor), eq(limit)))
                    .thenReturn(notifications);

            // when
            NotificationListResponse response = notificationService.getNotifications(receiverId, cursor, limit);

            // then
            assertThat(response.getNotifications()).hasSize(size);
            assertThat(response.isHasNext()).isFalse();
            assertThat(response.getNextCursor()).isNotNull();
        }
    }
}
