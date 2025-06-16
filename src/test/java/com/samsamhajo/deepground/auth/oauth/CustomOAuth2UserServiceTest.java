package com.samsamhajo.deepground.auth.oauth;

import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.entity.Provider;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CustomOAuth2UserServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 신규_소셜_회원이면_회원가입() {
        // given
        String email = "test@google.com";
        String name = "테스트유저";
        String registrationId = "google";
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", email);
        attributes.put("name", name);

        when(memberRepository.findByEmailAndProvider(email, Provider.GOOGLE))
                .thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        customOAuth2UserService.createTestUserDetails(registrationId, attributes);

        // then
        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(memberCaptor.capture());
        Member saved = memberCaptor.getValue();
        assertThat(saved.getEmail()).isEqualTo(email);
        assertThat(saved.getNickname()).isEqualTo(name);
        assertThat(saved.getProvider()).isEqualTo(Provider.GOOGLE);
        assertThat(saved.isVerified()).isTrue();
    }

    @Test
    void 이미_가입된_회원이면_닉네임만_갱신() {
        // given
        String email = "test@google.com";
        String oldName = "OldName";
        String newName = "NewName";
        String registrationId = "google";

        Member oldMember = Member.createSocialMember(email, oldName, Provider.GOOGLE, "providerId");

        when(memberRepository.findByEmailAndProvider(email, Provider.GOOGLE))
                .thenReturn(Optional.of(oldMember));
        when(memberRepository.save(any(Member.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", email);
        attributes.put("name", newName);

        // when
        customOAuth2UserService.createTestUserDetails(registrationId, attributes);

        // then
        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(memberCaptor.capture());
        Member updated = memberCaptor.getValue();
        assertThat(updated.getNickname()).isEqualTo(newName);
    }
}
