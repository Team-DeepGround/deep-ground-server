package com.samsamhajo.deepground.interest.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "interests")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_id", nullable = false)
    private Long id;

    @Column(name = "interest_name", nullable = false)
    private String name;

    @Column(name = "interest_category")
    private String category;

    private Interest(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public static Interest of(String name, String category) {
        return new Interest(name, category);
    }
}
