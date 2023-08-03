package com.example.miniproject.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NegotiationRequestDto {
    private Integer suggested_price;
    private String status;
}
