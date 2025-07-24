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
        Member member = memberRepository.findByEmail(attributes.getEmail())
                .map(existing ->{
                    existing.linkSocialAccount(attributes.getProvider(), attributes.getProviderId());
                    existing.update(attributes.getName());
                    return existing;
                })
                .orElseGet(()->{
                    Member newMember = attributes.toEntity();
                    newMember.linkSocialAccount(attributes.getProvider(), attributes.getProviderId());
                    return newMember;
                });

        return memberRepository.save(member);
    }

    // 테스트 용
    public CustomUserDetails createTestUserDetails(String registrationId, Map<String, Object> attributes) {
        String userNameAttributeName = switch (registrationId) {
            case "google" -> "sub";
            case "naver" -> "id";
            default -> "email";
        };

        OAuthAttributes oauthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, attributes);

        Member member = memberRepository.findByEmail(oauthAttributes.getEmail())
                .map(entity -> {
                    entity.linkSocialAccount(oauthAttributes.getProvider(), oauthAttributes.getProviderId());
                    entity.updateNickname(oauthAttributes.getName());
                    return memberRepository.save(entity);
                })
                .orElseGet(() -> {
                    Member newMember = oauthAttributes.toEntity();
                    newMember.linkSocialAccount(oauthAttributes.getProvider(), oauthAttributes.getProviderId());
                    return memberRepository.save(newMember);
                });

        return new CustomUserDetails(member, attributes);
    }
}
