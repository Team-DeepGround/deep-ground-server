package com.samsamhajo.deepground.communityPlace.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateReviewDto {
    private double scope;
    @Size(min = 2, message = "리뷰 내용은 최소 2글자 이상 입력해야합니다.")
    private String content;
    private AddressDto address;
    private List<MultipartFile> images = new ArrayList<>();


}
