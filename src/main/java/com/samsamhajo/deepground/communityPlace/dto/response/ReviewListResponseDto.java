    package com.samsamhajo.deepground.communityPlace.dto.response;

    import com.samsamhajo.deepground.communityPlace.dto.request.SearchReviewSummaryDto;
    import lombok.Getter;

    import java.util.List;

    @Getter
    public class ReviewListResponseDto {
        private List<SearchReviewSummaryDto> reviews;
        private int totalPages;

        public static ReviewListResponseDto of(List<SearchReviewSummaryDto> reviews, int totalPages) {
            return new ReviewListResponseDto(reviews, totalPages);
        }

        private ReviewListResponseDto(List<SearchReviewSummaryDto> reviews, int totalPages) {
            this.reviews = reviews;
            this.totalPages = totalPages;
        }

    }
