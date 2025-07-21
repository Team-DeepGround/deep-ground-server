package com.samsamhajo.deepground.address.entity;


import com.samsamhajo.deepground.qna.answer.dto.AnswerDetailDto;
import com.samsamhajo.deepground.studyGroup.entity.StudyGroupAddress;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "gu")
    private String gu;

    @Column(name = "dong")
    private String dong;

    @OneToMany
    @JoinColumn(name = "address_id")
    private List<StudyGroupAddress> studyGroupAddresses = new ArrayList<>();

    private Address (String city, String gu, String dong){
        this.city =city;
        this.gu = gu;
        this.dong = dong;
    }

}
