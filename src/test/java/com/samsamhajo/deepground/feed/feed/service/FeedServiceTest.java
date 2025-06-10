package com.samsamhajo.deepground.feed.feed.service;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feed.exception.FeedErrorCode;
import com.samsamhajo.deepground.feed.feed.exception.FeedException;
import com.samsamhajo.deepground.feed.feed.model.FeedCreateRequest;
import com.samsamhajo.deepground.feed.feed.model.FeedUpdateRequest;
import com.samsamhajo.deepground.feed.feed.model.FetchFeedsResponse;
import com.samsamhajo.deepground.feed.feed.repository.FeedRepository;
import com.samsamhajo.deepground.feed.feedcomment.service.FeedCommentService;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.exception.MemberException;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedServiceTest {

    @InjectMocks
    private FeedService feedService;

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private FeedMediaService feedMediaService;

    @Mock
    private FeedCommentService feedCommentService;

    @Mock
    private FeedLikeService feedLikeService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PlatformTransactionManager transactionManager;

    private TransactionStatus status;
    private Member member;

    @BeforeEach
    void setUp() {
        // 테스트 시작 전 트랜잭션 시작
        status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        
        // 테스트용 회원 객체 생성
        member = mock(Member.class);
        when(member.getId()).thenReturn(1L);
        when(member.getNickname()).thenReturn("testUser");
    }

    @AfterEach
    void tearDown() {
        // 테스트 종료 후 롤백 처리
        if (status != null) {
            transactionManager.rollback(status);
        }
    }

    @Test
    @DisplayName("피드 생성 성공 테스트")
    void createFeed_Success() {
        // given
        String content = "테스트 피드 내용";
        Long memberId = 1L;
        
        Feed feed = Feed.of(content, member);
        
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
        given(feedRepository.save(any(Feed.class))).willReturn(feed);
        
        // when
        Feed createdFeed = feedService.createFeed(new FeedCreateRequest(content, null), memberId);
        
        // then
        assertThat(createdFeed).isNotNull();
        assertThat(createdFeed.getContent()).isEqualTo(content);
        assertThat(createdFeed.getMember()).isEqualTo(member);
    }

    @Test
    @DisplayName("피드 생성 실패 테스트 - 존재하지 않는 회원")
    void createFeed_Fail_MemberNotFound() {
        // given
        String content = "테스트 피드 내용";
        Long nonExistentMemberId = 999L;
        
        given(memberRepository.findById(nonExistentMemberId)).willReturn(Optional.empty());
        
        // when & then
        assertThatThrownBy(() -> feedService.createFeed(new FeedCreateRequest(content, null), nonExistentMemberId))
                .isInstanceOf(MemberException.class);
    }

    @Test
    @DisplayName("피드 목록 조회 성공 테스트")
    void getFeeds_Success() {
        // given
        Feed feed1 = mock(Feed.class);
        Feed feed2 = mock(Feed.class);
        List<Feed> feeds = List.of(feed1, feed2);
        Pageable pageable = PageRequest.of(0, 10);
        Long memberId = 1L;

        when(feed1.getId()).thenReturn(1L);
        when(feed1.getContent()).thenReturn("피드1");
        when(feed1.getMember()).thenReturn(member);
        when(feed1.getCreatedAt()).thenReturn(LocalDateTime.now());

        when(feed2.getId()).thenReturn(2L);
        when(feed2.getContent()).thenReturn("피드2");
        when(feed2.getMember()).thenReturn(member);
        when(feed2.getCreatedAt()).thenReturn(LocalDateTime.now());

        when(feedRepository.findAll(pageable)).thenReturn(new PageImpl<>(feeds));
        when(feedMediaService.findAllMediaIdsByFeedId(any())).thenReturn(List.of(1L, 2L));
        when(feedCommentService.countFeedCommentsByFeedId(any())).thenReturn(2);
        when(feedLikeService.countFeedLikeByFeedId(any())).thenReturn(3);
        when(feedLikeService.isLiked(any(), any())).thenReturn(false);

        // when
        FetchFeedsResponse response = feedService.getFeeds(pageable, memberId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getFeeds()).hasSize(2);

        var firstFeed = response.getFeeds().get(0);
        assertThat(firstFeed.getContent()).isEqualTo("피드1");
        assertThat(firstFeed.getMemberId()).isEqualTo(1L);
        assertThat(firstFeed.getMemberName()).isEqualTo("testUser");
        assertThat(firstFeed.getMediaIds()).hasSize(2);
        assertThat(firstFeed.getCommentCount()).isEqualTo(2);
        assertThat(firstFeed.getLikeCount()).isEqualTo(3);
    }
    
    @Test
    @DisplayName("피드 수정 성공 테스트")
    void updateFeed_Success() {
        // given
        Long feedId = 1L;
        Long memberId = 1L;
        String originalContent = "원본 피드 내용";
        String updatedContent = "수정된 피드 내용";
        
        Feed originalFeed = Feed.of(originalContent, member);
        
        MockMultipartFile mockImage = new MockMultipartFile(
                "image", 
                "test.jpg", 
                "image/jpeg", 
                "테스트 이미지 데이터".getBytes()
        );
        
        List<MultipartFile> updatedImages = List.of(mockImage);
        FeedUpdateRequest updateRequest = new FeedUpdateRequest(updatedContent, updatedImages);

        when(feedRepository.getById(feedId)).thenReturn(originalFeed);
        
        // when
        Feed updatedFeed = feedService.updateFeed(feedId, updateRequest, memberId);
        
        // then
        assertThat(updatedFeed).isNotNull();
        assertThat(updatedFeed.getContent()).isEqualTo(updatedContent);
        
        // feedMediaService의 메서드가 호출되었는지 검증
        verify(feedMediaService, times(1)).updateFeedMedia(eq(originalFeed), eq(updateRequest));
    }
    
    @Test
    @DisplayName("피드 수정 실패 테스트 - 빈 내용")
    void updateFeed_Fail_EmptyContent() {
        // given
        Long feedId = 1L;
        Long memberId = 1L;
        String emptyContent = "";
        
        MockMultipartFile mockImage = new MockMultipartFile(
                "image", 
                "test.jpg", 
                "image/jpeg", 
                "테스트 이미지 데이터".getBytes()
        );
        
        List<MultipartFile> updatedImages = List.of(mockImage);
        FeedUpdateRequest updateRequest = new FeedUpdateRequest(emptyContent, updatedImages);
        
        // when & then
        assertThatThrownBy(() -> feedService.updateFeed(feedId, updateRequest, memberId))
                .isInstanceOf(FeedException.class)
                .hasFieldOrPropertyWithValue("errorCode", FeedErrorCode.INVALID_FEED_CONTENT);
        
        // feedRepository.getById가 호출되지 않았는지 검증
        verify(feedRepository, times(0)).getById(anyLong());
    }
    
    @Test
    @DisplayName("피드 수정 실패 테스트 - 존재하지 않는 피드")
    void updateFeed_Fail_FeedNotFound() {
        // given
        Long nonExistentFeedId = 999L;
        Long memberId = 1L;
        String updatedContent = "수정된 피드 내용";
        
        FeedUpdateRequest updateRequest = new FeedUpdateRequest(updatedContent, null);
        
        when(feedRepository.getById(nonExistentFeedId))
                .thenThrow(new FeedException(FeedErrorCode.FEED_NOT_FOUND));
        
        // when & then
        assertThatThrownBy(() -> feedService.updateFeed(nonExistentFeedId, updateRequest, memberId))
                .isInstanceOf(FeedException.class)
                .hasFieldOrPropertyWithValue("errorCode", FeedErrorCode.FEED_NOT_FOUND);
    }
    
    @Test
    @DisplayName("피드 삭제 성공 테스트")
    void deleteFeed_Success() {
        // given
        Long feedId = 1L;
        
        Feed feed = Feed.of("테스트 피드 내용", member);
        
        given(feedRepository.getById(feedId)).willReturn(feed);
        
        // when
        feedService.deleteFeed(feedId);
        
        // then
        verify(feedRepository, times(1)).getById(feedId);
        verify(feedCommentService, times(1)).deleteFeedCommentByFeed(feedId);
        verify(feedLikeService, times(1)).deleteAllByFeedId(feedId);
        verify(feedMediaService, times(2)).deleteAllByFeedId(feedId); // deleteRelatedEntities와 deleteFeed에서 각각 호출
    }

    @Test
    @DisplayName("피드 삭제 실패 테스트 - 존재하지 않는 피드")
    void deleteFeed_Fail_FeedNotFound() {
        // given
        Long nonExistentFeedId = 999L;
        
        when(feedRepository.getById(nonExistentFeedId))
                .thenThrow(new FeedException(FeedErrorCode.FEED_NOT_FOUND));
        
        // when & then
        assertThatThrownBy(() -> feedService.deleteFeed(nonExistentFeedId))
                .isInstanceOf(FeedException.class)
                .hasFieldOrPropertyWithValue("errorCode", FeedErrorCode.FEED_NOT_FOUND);
    }
}