package com.example.miniproject.dto;

import com.example.miniproject.entity.ItemEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

//컨트롤러에서 선택한 필드만 responsebody로 반환할 수 있음 null값은 자동 생략
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ItemResponseDto { //사용자에게 보여질 데이터
    private Long id;
    private String title; //제목
    private String description; //설명
    private String image_url; //이미지
    private Integer min_price_wanted; //최소가격
    private String status;
    private String writer;
    //정적메서드라 클래스명으로 호출가능
    public static ItemResponseDto fromEntity(ItemEntity itemEntity){ //사용자에게 보여질 데이터 DTO 형식으로 변환
        ItemResponseDto dto = new ItemResponseDto();
        dto.setId(itemEntity.getId());
        dto.setTitle(itemEntity.getTitle());
        dto.setDescription(itemEntity.getDescription());
        dto.setMin_price_wanted(itemEntity.getMin_price_wanted());
        dto.setImage_url(itemEntity.getImage_url());
        dto.setStatus(itemEntity.getStatus());
        dto.setWriter(itemEntity.getWriter());
        return dto;
    }
}


