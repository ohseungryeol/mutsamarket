package com.example.miniproject.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

//테이블 정의
@Data
@Entity
@Table(name = "sales_item")
public class ItemEntity {
    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동 오름차순 id지정
    Long id;
    String title;
    String description;
    String image_url;
    Integer min_price_wanted;
    String status;
    String writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") //foreign key 이름이 member_id
    private UserEntity user;

    @OneToMany(mappedBy = "item")
    private List<CommentEntity> comments = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private List<NegotiationEntity> negotiations = new ArrayList<>();

}
