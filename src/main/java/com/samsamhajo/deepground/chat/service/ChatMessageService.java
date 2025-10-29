package com.samsamhajo.deepground.chat.service;

import com.samsamhajo.deepground.chat.dto.ChatMessageListResponse;
import com.samsamhajo.deepground.chat.dto.ReadMessageResponse;
import com.samsamhajo.deepground.chat.dto.ChatMessageRequest;
import com.samsamhajo.deepground.chat.dto.ChatMessageResponse;
import com.samsamhajo.deepground.chat.dto.UnreadCountResponse;
import com.samsamhajo.deepground.chat.entity.ChatMedia;
import com.samsamhajo.deepground.chat.entity.ChatMessage;
import com.samsamhajo.deepground.chat.entity.ChatMessageMedia;
import com.samsamhajo.deepground.chat.entity.ChatRoomMember;
import com.samsamhajo.deepground.chat.exception.ChatErrorCode;
import com.samsamhajo.deepground.chat.exception.ChatException;
import com.samsamhajo.deepground.chat.exception.ChatMessageErrorCode;
import com.samsamhajo.deepground.chat.exception.ChatMessageException;
import com.samsamhajo.deepground.chat.repository.ChatMediaRepository;
import com.samsamhajo.deepground.chat.repository.ChatMessageRepository;
import com.samsamhajo.deepground.chat.repository.ChatRoomMemberRepository;
import com.samsamhajo.deepground.global.message.MessagePublisher;
import com.samsamhajo.deepground.sse.dto.SseEvent;
import com.samsamhajo.deepground.sse.dto.SseEventType;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final MessagePublisher messagePublisher;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMediaRepository chatMediaRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public ChatMessageListResponse getMessages(Long chatRoomId, Long memberId, ZonedDateTime cursor, int limit) {
        if (!chatRoomMemberRepository.existsByChatRoomIdAndMemberId(chatRoomId, memberId)) {
            throw new ChatException(ChatErrorCode.CHATROOM_ACCESS_DENIED);
        }

        return getMessages(chatRoomId, cursor, limit);
    }

    public ChatMessageListResponse getMessages(Long chatRoomId, ZonedDateTime cursor, int limit) {
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomIdWithCursor(chatRoomId, cursor, limit);

        boolean hasNext = messages.size() > limit;
        if (hasNext) {
            messages = messages.subList(0, limit);
        }

        ZonedDateTime nextCursor = messages.isEmpty() ? null
                : messages.get(messages.size() - 1).getCreatedAt();

        return ChatMessageListResponse.of(messages, nextCursor, hasNext);
    }

    @Transactional
    public void sendMessage(Long chatRoomId, Long memberId, ChatMessageRequest request) {
        boolean hasMedia = !CollectionUtils.isEmpty(request.getMediaIds());

        if (!StringUtils.hasText(request.getMessage()) && !hasMedia) {
            throw new ChatMessageException(ChatMessageErrorCode.INVALID_MESSAGE);
        }

        ChatMessage chatMessage = hasMedia ? messageTextAndMedia(chatRoomId, memberId, request)
                : ChatMessage.of(chatRoomId, memberId, request.getMessage());
        chatMessageRepository.save(chatMessage);

        // 채팅 메시지 전송
        String destination = "/chatrooms/" + chatRoomId + "/message";
        messagePublisher.convertAndSend(destination, ChatMessageResponse.from(chatMessage));

        // 메시지를 전송한 멤버는 읽음 처리
        readMessage(chatRoomId, memberId, chatMessage.getCreatedAt());

        // 읽지 않은 메시지 개수를 채팅방 멤버에게 전송
        sendUnreadCount(chatRoomId, memberId, chatMessage.getCreatedAt());
    }

    private ChatMessage messageTextAndMedia(Long chatRoomId, Long memberId, ChatMessageRequest request) {
        List<ChatMedia> chatMedia = chatMediaRepository.findAllByIdAndStatusPending(request.getMediaIds());
        List<ChatMessageMedia> media = chatMedia.stream()
                .filter(m -> m.getChatRoomId().equals(chatRoomId) && m.getMemberId().equals(memberId))
                .map(ChatMedia::getMedia)
                .toList();

        if (request.getMediaIds().size() != media.size()) {
            throw new ChatMessageException(ChatMessageErrorCode.MEDIA_NOT_FOUND);
        }

        chatMedia.forEach(ChatMedia::send);
        chatMediaRepository.saveAll(chatMedia);

        List<String> mediaIds = chatMedia.stream()
                .map(ChatMedia::getId)
                .toList();
        return ChatMessage.of(chatRoomId, memberId, request.getMessage(), mediaIds);
    }

    @Transactional
    public void readMessage(Long chatRoomId, Long memberId, ZonedDateTime latestMessageTime) {

        ChatRoomMember member = chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, memberId)
                .orElseThrow(() -> new ChatMessageException(ChatMessageErrorCode.CHATROOM_MEMBER_NOT_FOUND));

        boolean isUpdated = member.updateLastReadMessageTime(latestMessageTime);
        if (!isUpdated) {
            return;
        }

        String destination = "/chatrooms/" + chatRoomId + "/read-receipt";
        messagePublisher.convertAndSend(
                destination,
                ReadMessageResponse.of(memberId, latestMessageTime)
        );
    }

    private void sendUnreadCount(Long chatRoomId, Long memberId, ZonedDateTime latestMessageTime) {
        List<ChatRoomMember> members = chatRoomMemberRepository.findByChatRoomId(chatRoomId).stream()
                .filter(member -> !Objects.equals(member.getMember().getId(), memberId))
                .toList();

        members.forEach(member -> {
            Long unreadCount = chatMessageRepository.countByChatRoomIdAndCreatedAtAfter(
                    chatRoomId,
                    member.getLastReadMessageTime()
            );

            UnreadCountResponse response = UnreadCountResponse.of(chatRoomId, unreadCount, latestMessageTime);
            SseEvent event = SseEvent.of(member.getMember().getId(), SseEventType.UNREAD_COUNT, response);
            eventPublisher.publishEvent(event);
        });
    }
}
