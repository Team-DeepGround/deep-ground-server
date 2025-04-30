package com.samsamhajo.deepground.interest.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "interests")
public class Interest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_id", nullable = false)
    private Long id;

    @Column(name = "interest_name", nullable = false)
    private String name;

    @Column(name = "interest_category")
    private String category;
}
