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
import static org.mockito.ArgumentMatchers.any;
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
        String email = "test@google.com";
        String name = "테스트유저";
        String registrationId = "google";
        String providerId = "1234567890";

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", email);
        attributes.put("name", name);
        attributes.put("sub", providerId);

        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        customOAuth2UserService.createTestUserDetails(registrationId, attributes);

        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(memberCaptor.capture());
        Member saved = memberCaptor.getValue();

        assertThat(saved.getEmail()).isEqualTo(email);
        assertThat(saved.getNickname()).isEqualTo(name);
        assertThat(saved.getGoogleId()).isEqualTo(providerId);
        assertThat(saved.isVerified()).isTrue();
    }

    @Test
    void 이미_가입된_회원이면_닉네임만_갱신() {
        String email = "test@google.com";
        String oldName = "OldName";
        String newName = "NewName";
        String providerId = "1234567890";
        String registrationId = "google";

        Member oldMember = Member.createSocialMember(email, oldName);

        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(oldMember));
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", email);
        attributes.put("name", newName);
        attributes.put("sub", providerId);

        customOAuth2UserService.createTestUserDetails(registrationId, attributes);

        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(memberCaptor.capture());
        Member updated = memberCaptor.getValue();

        assertThat(updated.getNickname()).isEqualTo(newName);
        assertThat(updated.getGoogleId()).isEqualTo(providerId);
    }
}
