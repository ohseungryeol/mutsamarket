package com.example.miniproject.controller;


import com.example.miniproject.dto.MessageDto;
import com.example.miniproject.dto.NegotiationRequestDto;
import com.example.miniproject.dto.NegotiationResponseDto;
import com.example.miniproject.entity.CommentEntity;
import com.example.miniproject.repository.ItemRepository;
import com.example.miniproject.service.NegotiationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/items/{itemId}/proposals")
@RequiredArgsConstructor
public class NegotiationController {
    private final NegotiationService negotiationService;

    @PostMapping
    public MessageDto uploadSuggest(@PathVariable("itemId") Long itemId, @RequestBody NegotiationRequestDto dto){
        negotiationService.upload(itemId,dto);
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage("구매 제안이 등록되었습니다.");
        return messageDto;
    }


    @GetMapping
    // 해당 물품에 대한 모든 제안조회(해당 물품 주인만 조회가능)
    // 혹은 제안을 등록한 사람 (자신이 달은 모든 댓글 조회가능)
    public Page<NegotiationResponseDto> readAllSuggest
            (@PathVariable("itemId") Long itemId,
             @RequestParam("page") int page)
    {
        return negotiationService.readSuggestAll(itemId,page);
    }

    @PutMapping("/{proposalId}")
    public MessageDto updateSuggest(@PathVariable("itemId") Long itemId, @PathVariable("proposalId") Long proposalId, @RequestBody NegotiationRequestDto dto){
        MessageDto messageDto = negotiationService.update(itemId,proposalId,dto);
        return messageDto;
    }

    @DeleteMapping("/{proposalId}")
    public MessageDto deleteSuggest(@PathVariable("itemId") Long itemId,
                                    @PathVariable("proposalId") Long proposalId)
    {
        negotiationService.delete(itemId,proposalId);
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage("제안이 삭제되었습니다.");
        return messageDto;
    }

}
