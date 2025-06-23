package com.samsamhajo.deepground.member.service;

import com.samsamhajo.deepground.member.Dto.MemberProfileDto;
import com.samsamhajo.deepground.member.entity.Member;
import com.samsamhajo.deepground.member.entity.MemberProfile;
import com.samsamhajo.deepground.member.exception.MemberException;
import com.samsamhajo.deepground.member.repository.MemberRepository;
import com.samsamhajo.deepground.member.repository.ProfileRepository;
import com.samsamhajo.deepground.techStack.entity.MemberTechStack;
import com.samsamhajo.deepground.techStack.entity.TechStack;
import com.samsamhajo.deepground.techStack.repository.TechStackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class ProfileTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private TechStackRepository techStackRepository;

    private Member member;
    private MemberProfile profile;

    @BeforeEach
    void setup() {
        member = Member.createLocalMember(
                "paka@email.com","pa123","알파카");
        memberRepository.save(member);

        TechStack java = techStackRepository.save(TechStack.of("Java", "백엔드"));
        TechStack jS = techStackRepository.save(TechStack.of("JavaScript", "프론트엔드"));

        profile = MemberProfile.create(
                "https://example.com/profile.jpg", // profileImage
                member,
                "안녕하세요, 자바 개발자입니다.",
                "백엔드 개발자",
                "삼삼하조",
                "서울",
                "컴퓨터공학과",
                new ArrayList<>(),
                "https://github.com/paka",
                "https://linkedin.com/in/paka",
                "https://paka.dev",
                "https://twitter.com/paka"
        );

        MemberTechStack javaM = MemberTechStack.of(member,profile,java);
        MemberTechStack jSM = MemberTechStack.of(member,profile,jS);

        profile.getMemberTechStacks().add(javaM);
        profile.getMemberTechStacks().add(jSM);

        profileRepository.save(profile);
    }

    @Test
    public void 프로필_편집_성공() throws Exception {
        //given
        MemberProfileDto edit = MemberProfileDto.builder()
                .profileImage("https://example.com/new.jpg")
                .nickname("파카")
                .introduction("새로운 소개입니다.")
                .job("백엔드 개발자")
                .company("삼삼하조")
                .liveIn("서울")
                .education("컴퓨터공학과")
                .techStack(List.of("Java", "Spring"))
                .githubUrl("https://github.com/new")
                .linkedInUrl("https://linkedin.com/new")
                .websiteUrl("https://new.dev")
                .twitterUrl("https://twitter.com/new")
                .build();

        //when
        MemberProfileDto update = memberService.editMemberProfile(member.getId(), edit, null);

        //then
        assertEquals("https://example.com/new.jpg", update.getProfileImage());
        assertEquals("파카", update.getNickname());
        assertEquals("백엔드 개발자", update.getJob());
        assertEquals("삼삼하조", update.getCompany());
        assertEquals("서울", update.getLiveIn());
        assertEquals("컴퓨터공학과", update.getEducation());
        assertEquals("https://github.com/new", update.getGithubUrl());
        assertEquals("https://linkedin.com/new", update.getLinkedInUrl());
        assertEquals("https://new.dev", update.getWebsiteUrl());
        assertEquals("https://twitter.com/new", update.getTwitterUrl());

    }

    @Test
    void 존재하지_않는_멤버ID_프로필_수정시_예외() {
        // given
        Long invalidMemberId = 9999L;

        MemberProfileDto dto = MemberProfileDto.builder()
                .nickname("fly")
                .build();

        // when & then
        assertThrows(MemberException.class, () ->
                memberService.editMemberProfile(invalidMemberId, dto,null));
    }

}
