package com.samsamhajo.deepground.techStack.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tech_stacks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechStack extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tech_stack_id", nullable = false)
    private Long id;

    @Column(name = "tech_stack_name", nullable = false)
    private String name;

    @Column(name = "tech_stack_category")
    private String category;

    private TechStack(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public static TechStack of(String name, String category) {
        return new TechStack(name, category);
    }
}
