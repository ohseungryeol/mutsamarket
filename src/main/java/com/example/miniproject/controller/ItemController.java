package com.example.miniproject.controller;

import com.example.miniproject.dto.ItemResponseDto;
import com.example.miniproject.dto.ItemRequestDto;
import com.example.miniproject.dto.ItemResponseDto;
import com.example.miniproject.dto.MessageDto;
import com.example.miniproject.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items") //locahost8080/items
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
//ddddd
    @PostMapping
    // 아이템 등록 메서드
    public MessageDto create(@RequestBody ItemRequestDto itemDto){ //컨트롤러 -> service -> repository
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage("등록이 완료되었습니다.");
        itemService.uploadItem(itemDto);

        return messageDto;
    }

    /*단일 상품 조회 (무인증)*/
    @GetMapping("/{itemId}")
    public ItemResponseDto read (@PathVariable("itemId") Long id)
    { //쿼리파라미터 id값을 읽은 후 service에 전달
        ItemResponseDto responseDto = itemService.readItem(id);
        return responseDto;
    }


    /*모든 아이템 페이지별 조회(무인증)*/
    @GetMapping()
    public Page<ItemResponseDto> readPageItemAll(@RequestParam (value = "page", defaultValue = "0") int pageNum,
                                                 @RequestParam (value = "limit", defaultValue = "25") int pageSize){

        return itemService.readPageItemAll(pageNum,pageSize);
    }


    /*물품 수정 -> 인증상태 */
    @PutMapping("/{itemId}")
    public MessageDto update(@PathVariable Long itemId, @RequestBody ItemRequestDto dto){
        itemService.updateItem(itemId,dto);
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage("수정이 완료되었습니다.");
        return messageDto;
    }


// 사용자 프로필 이미지 설정 -> 인증상태
    @PutMapping(value = "/{itemId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MessageDto uploadItemImage(
            @PathVariable Long itemId,
            @RequestParam MultipartFile image,
            @RequestParam String writer,
            @RequestParam String password //이미지 등록할때 비밀번호도 첨부
    ){
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage("이미지가 등록되었습니다.");
        itemService.uploadItemImage(itemId, image);

        return messageDto;
    }

    //인증상태
    @DeleteMapping("/{itemId}")
    public MessageDto delete(@PathVariable("itemId") Long id) {
        itemService.deleteItem(id); //물품 삭제

        MessageDto messageDto = new MessageDto();
        messageDto.setMessage("물품을 삭제했습니다.");
        return messageDto;
    }




}
