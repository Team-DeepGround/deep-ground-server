//package com.samsamhajo.deepground.qna.question.controller;
//
//import com.samsamhajo.deepground.feed.feed.model.FeedMediaResponse;
//import com.samsamhajo.deepground.media.MediaUtils;
//import com.samsamhajo.deepground.qna.question.Dto.QuestionMediaResponse;
//import com.samsamhajo.deepground.qna.question.entity.QuestionMedia;
//import com.samsamhajo.deepground.qna.question.service.QuestionMediaService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.core.io.InputStreamResource;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.webjars.NotFoundException;
//
//@RestController
//@RequestMapping("/question")
//@RequiredArgsConstructor
//public class QuestionMediaController {
//
//
//    private final QuestionMediaService questionMediaService;
//
//    @GetMapping("/media/{imageUrl:.+}")
//    public ResponseEntity<InputStreamResource> getImage(@PathVariable String imageUrl) {
//        // DB에 저장된 값이 /media/TOooRK0fhC_haerin.jpg라면
//        String dbMediaUrl = "/media/" + imageUrl;
//        System.out.println("imageUrl 파라미터: " + imageUrl);
//        System.out.println("DB에서 찾을 mediaUrl: " + dbMediaUrl);
//
//        QuestionMedia questionMedia = questionMediaService.findByMediaUrl(dbMediaUrl)
//                .orElseThrow(() -> new NotFoundException("이미지를 찾을 수 없습니다."));
//
//        InputStreamResource imageStream = MediaUtils.getMedia(questionMedia.getMediaUrl());
//        String extension = questionMedia.getExtension();
//        MediaType mediaType = MediaUtils.getMediaType(extension);
//
//        return ResponseEntity.ok()
//                .contentType(mediaType)
//                .body(imageStream);
//    }
//}
