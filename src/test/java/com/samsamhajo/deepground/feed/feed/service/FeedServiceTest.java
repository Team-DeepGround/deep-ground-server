package com.samsamhajo.deepground.feed.feed.service;

import com.samsamhajo.deepground.feed.feed.entity.Feed;
import com.samsamhajo.deepground.feed.feed.entity.FeedMedia;
import com.samsamhajo.deepground.feed.feed.exception.FeedErrorCode;
import com.samsamhajo.deepground.feed.feed.exception.FeedException;
import com.samsamhajo.deepground.feed.feed.model.FeedCreateRequest;
import com.samsamhajo.deepground.feed.feed.model.FeedListResponse;
import com.samsamhajo.deepground.feed.feed.model.FeedResponse;
import com.samsamhajo.deepground.feed.feed.model.FeedUpdateRequest;
import com.samsamhajo.deepground.feed.feed.repository.FeedRepository;
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
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

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
    private MemberRepository memberRepository;

    private Member member = Member.createLocalMember("choiyt3465@naver.com", "q1w2e3r4!", "yt");


    @Mock
    private PlatformTransactionManager transactionManager;

    private TransactionStatus status;

    @BeforeEach
    void setUp() {
        // 테스트 시작 전 트랜잭션 시작
        status = transactionManager.getTransaction(new DefaultTransactionDefinition());
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
    @DisplayName("피드 목록 조회 테스트")
    void getFeeds_Success() {
        // given
        int page = 0;
        int size = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        
        Feed feed1 = Feed.of("첫 번째 피드", member);
        Feed feed2 = Feed.of("두 번째 피드", member);
        
        List<Feed> feeds = List.of(feed1, feed2);
        Page<Feed> feedPage = new PageImpl<>(feeds, pageable, feeds.size());
        
        List<FeedMedia> feed1Medias = List.of(
                FeedMedia.of("url1", "jpg", feed1),
                FeedMedia.of("url2", "jpg", feed1)
        );
        
        List<FeedMedia> feed2Medias = List.of(
                FeedMedia.of("url3", "jpg", feed2)
        );
        
        // Mock 설정
        when(feedRepository.findAll(pageable)).thenReturn(feedPage);
        when(feedMediaService.findAllByFeed(feed1)).thenReturn(feed1Medias);
        when(feedMediaService.findAllByFeed(feed2)).thenReturn(feed2Medias);
        
        // when
        FeedListResponse result = feedService.getFeeds(pageable);
        
        // then
        assertThat(result).isNotNull();
        assertThat(result.getFeeds()).hasSize(2);
        assertThat(result.getCurrentPage()).isEqualTo(page);
        assertThat(result.getTotalPages()).isEqualTo(1);
        
        // 첫 번째 피드 검증
        FeedResponse firstFeed = result.getFeeds().get(0);
        assertThat(firstFeed.getContent()).isEqualTo("첫 번째 피드");
        assertThat(firstFeed.getMemberName()).isEqualTo("yt");
        assertThat(firstFeed.getImageIds()).hasSize(2);
        
        // 두 번째 피드 검증
        FeedResponse secondFeed = result.getFeeds().get(1);
        assertThat(secondFeed.getContent()).isEqualTo("두 번째 피드");
        assertThat(secondFeed.getMemberName()).isEqualTo("yt");
        assertThat(secondFeed.getImageIds()).hasSize(1);
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
        Long memberId = 1L;
        
        Feed feed = Feed.of("테스트 피드 내용", member);
        
        given(feedRepository.getById(feedId)).willReturn(feed);
        
        // when
        feedService.deleteFeed(feedId, memberId);
        
        // then
        verify(feedRepository, times(1)).getById(feedId);
        verify(feedMediaService, times(1)).deleteAllByFeedId(feedId);
        assertThat(feed.isDeleted()).isTrue();
    }
}