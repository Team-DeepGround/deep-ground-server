package com.samsamhajo.deepground.entity;

import com.samsamhajo.deepground.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    @Column(name = "comment_id", nullable = false)
    private Long id;

    @Column(name = "comment_content", nullable = false)
    private String commentContent;
}
