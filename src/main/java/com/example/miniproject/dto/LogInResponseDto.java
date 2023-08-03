package com.example.miniproject.dto;

import com.example.miniproject.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogInResponseDto {
    private String token;
    private int exprTime;
    private UserEntity user;
}
