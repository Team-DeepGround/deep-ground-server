package com.samsamhajo.deepground.external.kakao;

import com.samsamhajo.deepground.external.ExternalApiCaller;
import com.samsamhajo.deepground.external.kakao.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoApiCaller {

    private final String REST_API_KEY;
    private final String KAKAO_API_HOST;
    private final ExternalApiCaller externalApiCaller;

    private Map<String, String> getAuthHeader() {
        return Map.of("Authorization", "KakaoAK " + REST_API_KEY);
    }

    public AddressSearchResponseDto getAddressSearch(AddressSearchRequestDto req) {
        return externalApiCaller.get(
                KAKAO_API_HOST + "/v2/local/search/address.json",
                getAuthHeader(),
                req.toParam(),
                new ParameterizedTypeReference<AddressSearchResponseDto>() {
                }
        );
    }

    public Coord2RegionCodeResponseDto getCoord2RegionCode(Coord2RegionCodeRequestDto req) {
        return externalApiCaller.get(
                KAKAO_API_HOST + "/v2/local/geo/coord2regioncode.json",
                getAuthHeader(),
                req.toParam(),
                new ParameterizedTypeReference<Coord2RegionCodeResponseDto>() {
                }
        );
    }

    public Coord2AddressResponseDto getCoord2Address(Coord2AddressRequestDto req) {
        return externalApiCaller.get(
                KAKAO_API_HOST + "/v2/local/geo/coord2address.json",
                getAuthHeader(),
                req.toParam(),
                new ParameterizedTypeReference<Coord2AddressResponseDto>() {
                }
        );
    }

    public TranscoordResponseDto getTranscoord(TranscoordRequestDto req) {
        return externalApiCaller.get(
                KAKAO_API_HOST + "/v2/local/geo/transcoord.json",
                getAuthHeader(),
                req.toParam(),
                new ParameterizedTypeReference<TranscoordResponseDto>() {
                }
        );
    }

    public KeywordSearchResponseDto getKeywordSearch(KeywordSearchRequestDto req) {
        return externalApiCaller.get(
                KAKAO_API_HOST + "/v2/local/search/keyword.json",
                getAuthHeader(),
                req.toParam(),
                new ParameterizedTypeReference<KeywordSearchResponseDto>() {
                }
        );
    }

    public CategorySearchResponseDto getCategorySearch(CategorySearchRequestDto req) {
        return externalApiCaller.get(
                KAKAO_API_HOST + "/v2/local/search/category.json",
                getAuthHeader(),
                req.toParam(),
                new ParameterizedTypeReference<CategorySearchResponseDto>() {
                }
        );
    }
}
