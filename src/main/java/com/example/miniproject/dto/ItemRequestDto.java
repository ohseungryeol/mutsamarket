package com.example.miniproject.dto;

import lombok.Data;

@Data
public class ItemRequestDto {
    private String title;
    private String description;
    private Integer min_price_wanted;
    private String writer;
}
