package com.example.miniproject.dto;

import com.example.miniproject.entity.NegotiationEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL) //null 값은 responsebody에 미포함
@Data
public class NegotiationResponseDto {
    private Long id;
    private Integer suggested_price;
    private String status;
    private String writer;


    public static NegotiationResponseDto fromEntity(NegotiationEntity entity){
        /*대상 물품, 제안 가격, 작성자 */
        NegotiationResponseDto dto = new NegotiationResponseDto();
        dto.setSuggested_price(entity.getSuggested_price());
        dto.setWriter(entity.getWriter());
        dto.setStatus(entity.getStatus());



        return dto;
    }
}
