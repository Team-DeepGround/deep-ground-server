package com.samsamhajo.deepground.support.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import com.samsamhajo.deepground.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inquiries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inquiry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "inquiry_status", nullable = false)
    private InquiryStatus status = InquiryStatus.PENDING;

    private Inquiry(Member member, String title, String content, InquiryStatus status){
        this.member = member;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    public static Inquiry of(Member member, String title, String content, InquiryStatus status) {
        return new Inquiry(member, title, content, status);
    }

    public void changeStatus(InquiryStatus newStatus) {
        this.status = newStatus;
    }
}
