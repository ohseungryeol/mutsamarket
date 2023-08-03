package com.example.miniproject.controller;

//import com.example.miniproject.dto.ItemDto;
import com.example.miniproject.dto.CommentRequestDto;
import com.example.miniproject.dto.CommentResponseDto;
import com.example.miniproject.dto.MessageDto;
import com.example.miniproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/items/{itemId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public MessageDto uploadComment(@PathVariable("itemId") Long itemId, @RequestBody CommentRequestDto dto){
        commentService.uploadComment(itemId,dto);
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage("댓글이 등록되었습니다.");
        return messageDto;
    }

    @GetMapping()
    public Page<CommentResponseDto> readPageItemAll(@PathVariable("itemId") Long itemId, @RequestParam (value = "page", defaultValue = "0") int pageNum,
                                                    @RequestParam (value = "limit", defaultValue = "25") int pageSize){

        return commentService.readPageCommentAll(itemId,pageNum,pageSize);
    }
    /*댓글 수정 */
    @PutMapping("/{commentId}")
    public MessageDto modifyComment(@PathVariable("itemId") Long itemId,@PathVariable("commentId") Long commentId, @RequestBody CommentRequestDto commentDto){
        commentService.updateComment(itemId,commentId,commentDto);
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage("댓글이 수정되었습니다.");

        return messageDto;
    }
    /*등록 물품에 댓글에 대한 답글*/
    @PutMapping("/{commentId}/reply")
    public MessageDto replyToComment(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentResponseDto commentResponseDto)

    {
        commentService.reply(itemId,commentId,commentResponseDto);
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage("댓글에 답변이 추가되었습니다.");
        return messageDto;
    }

    @DeleteMapping("/{commentId}")
    public MessageDto deleteComment(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId)
    {
        commentService.deleteComment(itemId,commentId);
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage("댓글을 삭제했습니다.");
        return messageDto;
    }
}
