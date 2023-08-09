package com.example.miniproject.service;


import com.example.miniproject.dto.MessageDto;
import com.example.miniproject.dto.NegotiationRequestDto;
import com.example.miniproject.dto.NegotiationResponseDto;
import com.example.miniproject.entity.CommentEntity;
import com.example.miniproject.entity.ItemEntity;
import com.example.miniproject.entity.NegotiationEntity;
import com.example.miniproject.entity.UserEntity;
import com.example.miniproject.exception.CustomException;
import com.example.miniproject.exception.ErrorCode;
import com.example.miniproject.repository.ItemRepository;
import com.example.miniproject.repository.NegotiationRepository;
import com.example.miniproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NegotiationService {
    private final NegotiationRepository negotiationRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public void upload(Long itemId, NegotiationRequestDto dto){
        //등록된 물품이 있어야 구매제안이 가능, 존재하지 않으면 예외처리
        Optional<ItemEntity> optionalItem = itemRepository.findById(itemId);
        if(!optionalItem.isPresent()) throw new CustomException(ErrorCode.ITEM_NOT_FOUND);


        NegotiationEntity negotiationEntity = new NegotiationEntity();
        ItemEntity itemEntity = optionalItem.get(); //해당 물품
        //인증한 유저
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        UserEntity user = optionalUser.get();

        negotiationEntity.setStatus("제안");
        negotiationEntity.setWriter(user.getUsername());
        negotiationEntity.setUser(user);//제안 글을 작성한 유저 등록
        negotiationEntity.setItem(itemEntity); //물품 등록
        negotiationEntity.setSuggested_price(dto.getSuggested_price());

        negotiationRepository.save(negotiationEntity);
    }
    public Page<NegotiationResponseDto> readSuggestAll(Long itemId, int page){
        //해당 물품이 없을시 예외처리
        // 해당 물품에 대한 모든 제안조회(해당 물품 주인만 조회가능)
        // 혹은 제안을 등록한 사람 (자신이 달은 모든 댓글 조회가능)
        Optional<ItemEntity> optionalItem = itemRepository.findById(itemId);
        if(!optionalItem.isPresent()) throw new CustomException(ErrorCode.ITEM_NOT_FOUND);

        Pageable pageable = PageRequest.of(page,25);
        ItemEntity itemEntity = optionalItem.get(); //해당 물품
        //인증한 유저
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        UserEntity user = optionalUser.get();

        //인증한 유저와 해당 물품의 주인이 같다면 해당 물품에 달린 모든 제안을 조회 가능하다.
        if(itemEntity.getUser().getId() == user.getId()){
            Page<NegotiationEntity> allSuggests= negotiationRepository.findAllByItemId(itemId,pageable);
            return allSuggests.map(NegotiationResponseDto::fromEntity);
        }
        Page<NegotiationEntity> allByItemIdAndUserId = negotiationRepository.findAllByItemIdAndUserId(itemId, user.getId(),pageable);
        return allByItemIdAndUserId.map(NegotiationResponseDto::fromEntity);


    }



    public MessageDto update(Long itemId, Long proposalId, NegotiationRequestDto dto){
        MessageDto messageDto = new MessageDto();
        Optional<ItemEntity> optionalItem = itemRepository.findById(itemId);

        //등록된 물품이 아닐 경우 예외처리
        if(!optionalItem.isPresent()) throw new CustomException(ErrorCode.ITEM_NOT_FOUND);
        //등록된 제안 글이 아닐 경우
        Optional<NegotiationEntity> optionalNegotiationEntity = negotiationRepository.findById(proposalId);
        if(optionalNegotiationEntity.isEmpty()) throw new CustomException(ErrorCode.NEGOTIATION_NOT_FOUND);

        System.out.println(dto.getStatus());


        //등록된 물품에 제안 글이 없을 경우 예외처리
        NegotiationEntity entity = optionalNegotiationEntity.get();
        /*if(entity.getItem().getId() !=itemId){
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }*/

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        UserEntity user = optionalUser.get(); //인증 유저
        ItemEntity itemEntity = optionalItem.get(); //현재 물품


        // 제안 등록자일 때
        if(user.getId()==entity.getUser().getId()){
            String status = entity.getStatus();

            if(status.equals("제안")) {
                entity.setSuggested_price(dto.getSuggested_price());
                negotiationRepository.save(entity);
                messageDto.setMessage("제안이 수정되었습니다.");

            } else if (status.equals("수락")){
                entity.setStatus("확정");
                negotiationRepository.save(entity);
                messageDto.setMessage("구매가 확정되었습니다.");
                //물품의 상태 판매완료
                itemEntity.setStatus("판매 완료");

                List<NegotiationEntity> negotiations = itemEntity.getNegotiations();
                //자신빼고 모든 제안 상태 거절
                for(NegotiationEntity negotiationEntity: negotiations){
                    if(negotiationEntity.getId()==proposalId) continue;
                    negotiationEntity.setStatus("거절");
                    negotiationRepository.save(negotiationEntity);
                }
            }
            //대상 물품의 주인이라면 status를 수락 혹은 거절로 바꿀 수 있다.
            //item=1 proposal =1
            //로그인 승민 user=2
            //로그인한 자가 대상 물품의 주인이라면
        } else if (user.getId()==itemEntity.getUser().getId()){
            if(dto.getStatus().equals("수락")){
                entity.setStatus("수락");
                negotiationRepository.save(entity);
                messageDto.setMessage("제안을 수락하였습니다.");
            } else if (dto.getStatus().equals("거절")){
                entity.setStatus("거절");
                negotiationRepository.save(entity);
                messageDto.setMessage("제안이 거절하였습니다.");
            }
        } else {
            //권한 없는 사용자들.
            throw new CustomException(ErrorCode.INVALID_AUTH);
        }


        return messageDto;
    }

    public void delete(Long itemId, Long proposalId){
        //등록된 물품이 아닐 경우 예외처리
        Optional<ItemEntity> optionalItem = itemRepository.findById(itemId);
        if(!optionalItem.isPresent()) throw new CustomException(ErrorCode.ITEM_NOT_FOUND);
        //등록된 물품에 제안 글이 없을 경우 예외처리
        ItemEntity itemEntity = optionalItem.get();
        Optional<NegotiationEntity> optionalNegotiation = negotiationRepository.findById(proposalId);
        NegotiationEntity negotiationEntity = optionalNegotiation.get();
        if(!optionalNegotiation.isPresent()) throw new CustomException(ErrorCode.NEGOTIATION_NOT_FOUND);



        if(negotiationEntity.getItem().getId() !=itemId){
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        UserEntity user = optionalUser.get();

        if(user.getId()==negotiationEntity.getUser().getId()){
            negotiationRepository.delete(negotiationEntity);
        } else{
            throw new CustomException(ErrorCode.INVALID_AUTH);
        }

    }
}
