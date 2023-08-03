package com.example.miniproject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "comment")
public class CommentEntity {
    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동 오름차순 id지정
    Long id;
    @NotNull
    String writer;

    @NotNull
    String content;

    String reply;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id") //foreign key 이름이 member_id
    private ItemEntity item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") //foreign key 이름이 member_id
    private UserEntity user;
}
