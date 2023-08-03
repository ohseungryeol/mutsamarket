package com.example.miniproject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequestDto {
    @NotNull
    private String content;
    private String reply;
}
