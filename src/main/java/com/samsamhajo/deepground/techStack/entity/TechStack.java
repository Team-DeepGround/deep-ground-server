package com.samsamhajo.deepground.techStack.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "tech_stacks")
public class TechStack extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tech_stack_id", nullable = false)
    private Long id;

    @Column(name = "tech_stack_name", nullable = false)
    private String name;

    @Column(name = "tech_stack_category")
    private String category;
}
