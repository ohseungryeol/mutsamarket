package com.example.miniproject.service;

import com.example.miniproject.dto.CommentRequestDto;
import com.example.miniproject.dto.CommentResponseDto;
import com.example.miniproject.entity.CommentEntity;
import com.example.miniproject.entity.ItemEntity;
import com.example.miniproject.entity.UserEntity;
import com.example.miniproject.exception.CustomException;
import com.example.miniproject.exception.ErrorCode;
import com.example.miniproject.repository.CommentRepository;
import com.example.miniproject.repository.ItemRepository;
import com.example.miniproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    /*로그인 한 사용자는 해당 중고물품에 댓글등록 */
    public void uploadComment(Long itemId, CommentRequestDto commentDto){
        //아이템이 존재해야 댓글을 달 수 있다.
        Optional<ItemEntity> optionalItem = itemRepository.findById(itemId);
        if(!optionalItem.isPresent()) throw new CustomException(ErrorCode.ITEM_NOT_FOUND);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        UserEntity user = optionalUser.get();
        ItemEntity item = optionalItem.get();
        CommentEntity newEntity = new CommentEntity();
        newEntity.setUser(user);
        //이거 자동으로 아이템 매핑되게 못하나 ?
        newEntity.setItem(item);
        newEntity.setContent(commentDto.getContent());
        newEntity.setWriter(user.getUsername());

      //  newEntity.setItemId(itemId);
        commentRepository.save(newEntity);
    }

    public Page<CommentResponseDto> readPageCommentAll(Long itemId, int pageNum, int pageSize){
        if(!itemRepository.existsById(itemId)) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"등록된 물품이 없습니다.");

        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<CommentEntity> AllComments = commentRepository.findAllByItemId(itemId,pageable);
        return AllComments.map(CommentResponseDto::fromEntity);
    }

    /*해당 중고 물품에 대한 댓글 수정 (댓글작성유저가!)*/
    public void updateComment(Long itemId, Long commentId,CommentRequestDto commentDto){

        Optional<ItemEntity> optionalItem = itemRepository.findById(itemId);
        //중고 물품이 존재하지 않을 때 예외처리
        if(!optionalItem.isPresent()) throw new CustomException(ErrorCode.ITEM_NOT_FOUND);

        //중고 물품에 댓글이 존재하지 않았을 때 예외처리
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if(!optionalComment.isPresent()) throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        //해당 아이템에 댓글이 달려있지 않을 때 예외처리
        ItemEntity itemEntity = optionalItem.get();
        if(itemEntity.getComments()==null) throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        CommentEntity commentEntity = optionalComment.get();


        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        UserEntity user = optionalUser.get();

        if(commentEntity.getItem().getId()!=itemId || itemEntity.getComments()==null){
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }
        //댓글을 등록한 자만이 수정할 수 있다.
        if(commentEntity.getUser().getId().equals(user.getId())){
            commentEntity.setContent(commentDto.getContent());
            commentRepository.save(commentEntity);
        } else {
            throw new CustomException(ErrorCode.NOT_OWNER); //댓글을 등록한 자가 아니다.
        }

    }

    public void reply(Long itemId, Long commentId, CommentResponseDto dto){
        Optional<ItemEntity> optionalItem = itemRepository.findById(itemId);
        //중고 물품이 존재하지 않을 때 예외처리
        if(!optionalItem.isPresent()) throw new CustomException(ErrorCode.ITEM_NOT_FOUND);
        //중고 물품에 댓글이 존재하지 않았을 때 예외처리
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if(!optionalComment.isPresent()) throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);


        ItemEntity itemEntity = optionalItem.get(); //해당 아이템
        CommentEntity commentEntity = optionalComment.get(); //해당 아이템에 대한 댓글
        UserEntity itemOwner = itemEntity.getUser(); //해당 아이템 주인

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        UserEntity loginUser = optionalUser.get(); //인증한 유저

        // 댓글에 대한 아이템이
        if(commentEntity.getItem().getId()!=itemId || itemEntity.getComments()==null){
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        //인증한 유저가 아이템을 등록한 유저라면 답글 가능 .
        if(loginUser.getId()==itemOwner.getId()){
            commentEntity.setReply(dto.getReply());
            commentRepository.save(commentEntity);
        } else{
            throw new CustomException(ErrorCode.NOT_OWNER);
        }

    }

    public void deleteComment(Long itemId, Long commentId){
        Optional<ItemEntity> optionalItem = itemRepository.findById(itemId);
        //중고 물품이 존재하지 않을 때 예외처리
        if(!optionalItem.isPresent()) throw new CustomException(ErrorCode.ITEM_NOT_FOUND);
        //중고 물품에 댓글이 존재하지 않았을 때 예외처리
        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if(!optionalComment.isPresent()) throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);


        ItemEntity itemEntity = optionalItem.get();
        CommentEntity commentEntity = optionalComment.get();
        //인증
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        UserEntity loginUser = optionalUser.get();

        if(commentEntity.getItem().getId()!=itemId || itemEntity.getComments()==null){
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }

        //로그인 한 유저가 댓글을 등록한 유저라면 삭제 가능하다.
        if(commentEntity.getUser().getId()==loginUser.getId()){
             commentRepository.delete(commentEntity);
        } else {
            throw new CustomException(ErrorCode.NOT_OWNER);
        }

    }
}
