package com.example.miniproject.service;

import com.example.miniproject.dto.ItemResponseDto;
import com.example.miniproject.dto.ItemRequestDto;
import com.example.miniproject.entity.ItemEntity;
import com.example.miniproject.entity.UserEntity;
import com.example.miniproject.exception.CustomException;
import com.example.miniproject.exception.ErrorCode;
import com.example.miniproject.exception.NotFoundException;
import com.example.miniproject.repository.ItemRepository;
import com.example.miniproject.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service

public class ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    /*물픔 등록*/
    //현재 로그인한 유저는 아이템을 등록할 수 있다.
    public void uploadItem(ItemRequestDto dto){
        //현재 인증된 유저 네임을 얻어오는 메서드
        String username = SecurityContextHolder //인증정보를 관리하는 클래스
                .getContext() // 현재 인증정보가 포함된 SecurityContext를 얻어온다.
                .getAuthentication() //SecurityContext에서 인증된 객체를 얻어온다.
                .getName(); //객체의 사용자 이름

        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

        //현재 로그인 한  유저객체
        UserEntity user = optionalUser.get();


        ItemEntity newItem = new ItemEntity();
        newItem.setTitle(dto.getTitle());
        newItem.setUser(user);
        newItem.setWriter(user.getUsername());
        newItem.setMin_price_wanted(dto.getMin_price_wanted());
        newItem.setDescription(dto.getDescription());
        newItem.setStatus("판매중");
        itemRepository.save(newItem);
    }

    /*단일 상품 조회 ->무인증 */
    public ItemResponseDto readItem(Long id){
        Optional<ItemEntity> readEntity = itemRepository.findById(id);
        if(readEntity.isEmpty()){
            throw new CustomException(ErrorCode.ITEM_NOT_FOUND);
        }

        return ItemResponseDto.fromEntity(readEntity.get());
    }

    /*전체 상품 조회 -> 무인증 */
    public List<ItemResponseDto> readItemAll(){
        List<ItemEntity> allItems = itemRepository.findAll();
        List<ItemResponseDto> allItemsDto = new ArrayList<>();

        for(ItemEntity itemEntity: allItems){
            ItemResponseDto itemDto = ItemResponseDto.fromEntity(itemEntity);
            allItemsDto.add(itemDto);
        }

        return allItemsDto;
    }
   //
    public Page<ItemResponseDto> readPageItemAll(int pageNum, int pageSize){
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<ItemEntity> AllItems = itemRepository.findAll(pageable);
        return AllItems.map(ItemResponseDto::fromEntity);
    }
    //인증된 자만이 수정
    public void updateItem(Long itemId, ItemRequestDto dto) {
        Optional<ItemEntity> optionalItemEntity = itemRepository.findById(itemId);
        if (optionalItemEntity.isEmpty()) {
            throw new CustomException(ErrorCode.ITEM_NOT_FOUND);
        }

        // 아이템은 존재하므로 해당 아이템을 올린 사용자인지 체크
        String username = SecurityContextHolder //인증정보를 관리하는 클래스
                .getContext() // 현재 인증정보가 포함된 SecurityContext를 얻어온다.
                .getAuthentication() //SecurityContext에서 인증된 객체를 얻어온다.
                .getName(); //객체의 사용자 이름

        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        //현재 인증한 유저
        UserEntity user = optionalUser.get();
        ItemEntity item = optionalItemEntity.get(); //현재 수정할 아이템

        // 수정할 아이템 작성자와 현재 로그인한 유저가 같은 사람이면 수정 가능
        if(item.getUser().getId().equals(user.getId())){
            item.setDescription(dto.getDescription());
            item.setTitle(dto.getTitle());
            item.setMin_price_wanted(dto.getMin_price_wanted());
            itemRepository.save(item);
        } else{
            //해당 아이템 작성자가 아님 .
            throw new CustomException(ErrorCode.NOT_OWNER);
        }

    }
    //해당 물품 소유자만 이미지 첨부가능 .
    public void uploadItemImage(Long itemId, MultipartFile image) {
        Optional<ItemEntity> optionalItemEntity = itemRepository.findById(itemId);

        if (optionalItemEntity.isEmpty()) {
            throw new CustomException(ErrorCode.ITEM_NOT_FOUND);
        }
        // 작성자나 비밀번호가 맞지 않으면 에러 발생

        // 아이템은 존재하므로 해당 아이템을 올린 사용자인지 체크
        String username = SecurityContextHolder //인증정보를 관리하는 클래스
                .getContext() // 현재 인증정보가 포함된 SecurityContext를 얻어온다.
                .getAuthentication() //SecurityContext에서 인증된 객체를 얻어온다.
                .getName(); //객체의 사용자 이름

        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        //현재 인증한 유저
        UserEntity user = optionalUser.get();
        ItemEntity item = optionalItemEntity.get();


        if(!item.getUser().getId().equals(user.getId())) throw new CustomException(ErrorCode.NOT_OWNER);

        // 폴더 생성
        String imgDir = String.format("media/%d/", itemId);
        try {
            Files.createDirectories(Path.of(imgDir));
        } catch (IOException e){
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String originName = image.getOriginalFilename();
        String path = imgDir + originName;

        // 이미지 저장
        try {
            image.transferTo(Path.of(path));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // 이미지 url 저장

        item.setImage_url(imgDir + originName);
        itemRepository.save(item);

    }


    //애초에 itemRepository를 뒤질 필요가없음 인증된 자만이 삭제할 것이므로
    public void deleteItem(Long itemId) {

        Optional<ItemEntity> optionalItem = itemRepository.findById(itemId);
        if(!optionalItem.isPresent()) throw new CustomException(ErrorCode.ITEM_NOT_FOUND);

        ItemEntity item = optionalItem.get();
        String username = SecurityContextHolder //인증정보를 관리하는 클래스
                .getContext() // 현재 인증정보가 포함된 SecurityContext를 얻어온다.
                .getAuthentication() //SecurityContext에서 인증된 객체를 얻어온다.
                .getName(); //객체의 사용자 이름

        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        //현재 인증한 유저
        UserEntity user = optionalUser.get();

        // 수정할 아이템 작성자와 현재 인증한 유저가 같은 사람이면 삭제가능
        if(item.getUser().getId().equals(user.getId())){
            itemRepository.delete(item);
        } else { //권한 없는 사용자
            throw new CustomException(ErrorCode.NOT_OWNER);
        }

        List<ItemEntity> items = user.getItems();

    }

}
