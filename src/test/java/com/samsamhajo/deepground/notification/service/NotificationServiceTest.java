package com.samsamhajo.deepground.notification.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.samsamhajo.deepground.global.message.MessagePublisher;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.notification.dto.NotificationResponse;
import com.samsamhajo.deepground.notification.entity.Notification;
import com.samsamhajo.deepground.notification.entity.NotificationData;
import com.samsamhajo.deepground.notification.entity.data.FriendNotificationData;
import com.samsamhajo.deepground.notification.exception.NotificationErrorCode;
import com.samsamhajo.deepground.notification.exception.NotificationException;
import com.samsamhajo.deepground.notification.repository.NotificationDataRepository;
import com.samsamhajo.deepground.notification.repository.NotificationRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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
    @DisplayName("알림 읽음")
    public class ReadNotification {

        private final String notificationId = "abc";
        private final Long receiverId = 1L;
        private Notification notification;

        @BeforeEach
        void setUp() {
            notification = Notification.of(receiverId, null);
        }

        @Test
        @DisplayName("알림을 읽음 상태로 변경한다")
        void readNotification_success() {
            // given
            when(notificationRepository.findByIdAndReceiverId(notificationId, receiverId))
                    .thenReturn(Optional.ofNullable(notification));

            // when
            notificationService.readNotification(notificationId, receiverId);

            // then
            verify(notificationRepository).findByIdAndReceiverId(notificationId, receiverId);
            verify(notificationRepository).save(notification);
            assertThat(notification.isRead()).isTrue();
        }

        @Test
        @DisplayName("알림을 찾을 수 없다면 예외가 발생한다")
        void readNotification_notFound_throwsException() {
            // given
            when(notificationRepository.findByIdAndReceiverId(notificationId, receiverId))
                    .thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> notificationService.readNotification(notificationId, receiverId))
                    .isInstanceOf(NotificationException.class)
                    .hasMessage(NotificationErrorCode.NOTIFICATION_NOT_FOUND.getMessage());
        }

        @Test
        @DisplayName("이미 읽음 상태인 알림이라면 예외가 발생한다")
        void readNotification_alreadyRead_throwsException() {
            // given
            when(notificationRepository.findByIdAndReceiverId(notificationId, receiverId))
                    .thenReturn(Optional.ofNullable(notification));
            notification.read();

            // when & then
            assertThatThrownBy(() -> notificationService.readNotification(notificationId, receiverId))
                    .isInstanceOf(NotificationException.class)
                    .hasMessage(NotificationErrorCode.NOTIFICATION_ALREADY_READ.getMessage());
        }

        @Test
        @DisplayName("모든 알림을 읽음 상태로 변경한다")
        void readAllNotifications_success() {
            // when
            notificationService.readAllNotifications(receiverId);

            // then
            verify(notificationRepository).updateAllByReceiverId(receiverId);
        }
    }
}
