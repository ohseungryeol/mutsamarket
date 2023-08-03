package com.example.miniproject.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // DB 제약사항 추가
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String password;


    @OneToMany(mappedBy = "user") //유저별 아이템 조회가 나음.
    private List<ItemEntity> items = new ArrayList<>();

    @OneToMany(mappedBy = "user") //유저별 아이템 조회가 나음.
    private List<CommentEntity> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user") //유저별 아이템 조회가 나음.
    private List<NegotiationEntity> negos = new ArrayList<>();

}
