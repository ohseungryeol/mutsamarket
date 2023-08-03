package com.example.miniproject.dto;

import com.example.miniproject.entity.CommentEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL) //null 값은 responsebody에 미포함
@Data
public class CommentResponseDto {
    private Long id;
    private Long itemId;
    private String writer;
    private String password;
    private String content;
    private String reply;

    public static CommentResponseDto fromEntity(CommentEntity commentEntity){
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(commentEntity.getId());
       // dto.setItemId(commentEntity.getItemId());
        dto.setWriter(commentEntity.getWriter());
        dto.setContent(commentEntity.getContent());
        dto.setReply(commentEntity.getReply());

        return dto;
    }
}
