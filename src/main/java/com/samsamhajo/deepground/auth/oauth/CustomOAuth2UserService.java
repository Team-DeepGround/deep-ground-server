package com.samsamhajo.deepground.auth.oauth;

import com.samsamhajo.deepground.auth.security.CustomUserDetails;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        try {
            return this.process(userRequest, oauth2User);
        } catch (Exception e) {
            throw new OAuth2AuthenticationException(e.getMessage());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User oauth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(
                registrationId,
                userNameAttributeName,
                oauth2User.getAttributes()
        );

        Member member = saveOrUpdate(attributes);

        return new CustomUserDetails(member);
    }

    private Member saveOrUpdate(OAuthAttributes attributes) {
        Member member = memberRepository.findByEmailAndProvider(attributes.getEmail(), attributes.getProvider())
                .map(entity -> entity.update(attributes.getName()))
                .orElse(attributes.toEntity());

        return memberRepository.save(member);
    }

    // 테스트 용
    public CustomUserDetails createTestUserDetails(String registrationId, Map<String, Object> attributes) {
        String userNameAttributeName = switch (registrationId) {
            case "google" -> "email";
            case "kakao" -> "id";
            case "naver" -> "id";
            default -> "email";
        };
        OAuthAttributes oauthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, attributes);

        Member member = memberRepository.findByEmailAndProvider(oauthAttributes.getEmail(), oauthAttributes.getProvider())
                .map(entity -> {
                    entity.update(oauthAttributes.getName());
                    return memberRepository.save(entity); // 반드시 save 호출!
                })
                .orElseGet(() -> memberRepository.save(oauthAttributes.toEntity()));

        return new CustomUserDetails(member, attributes);
    }
}
