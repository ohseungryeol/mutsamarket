package com.example.miniproject.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "negotiation")
public class NegotiationEntity {
    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동 오름차순 id지정
    Long id;
    Integer suggested_price;
    String status;
    String writer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id") //foreign key 이름이 member_id
    private ItemEntity item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") //foreign key 이름이 member_id
    private UserEntity user;
}
