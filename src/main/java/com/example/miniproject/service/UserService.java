package com.example.miniproject.service;

import com.example.miniproject.dto.LogInResponseDto;
import com.example.miniproject.dto.LoginRequestDto;
import com.example.miniproject.dto.UserJoinRequestDto;
import com.example.miniproject.exception.CustomException;
import com.example.miniproject.exception.ErrorCode;
import com.example.miniproject.exception.NotFoundException;
import com.example.miniproject.jwt.JwtTokenUtils;
import com.example.miniproject.repository.UserRepository;
import com.example.miniproject.entity.UserEntity;
import com.example.miniproject.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    public void join(UserJoinRequestDto userDto){
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(userDto.getUsername());
        if(optionalUserEntity.isPresent()){
            //
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDto.getUsername());
        userEntity.setPassword(userDto.getPassword());

        userRepository.save(userEntity);
    }

    public LogInResponseDto login(LoginRequestDto dto){
        String username = dto.getUsername();
        String password = dto.getPassword();

        System.out.println(username+","+password);
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        // 존재하지 않는 사용자
        if(userEntity.isEmpty()) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        UserEntity user = userEntity.get();
        //비밀번호 오류
        if(!dto.getPassword().equals(user.getPassword())) throw new CustomException(ErrorCode.INVALID_PASSWORD);

        String token = jwtTokenUtils.generateToken(CustomUserDetails.fromEntity(user)); //

        user.setPassword(""); //어차피 save를 안했으므로 db에 password 값은 바뀌지 않음 .
        log.info("token = "+token);
        log.info("username="+jwtTokenUtils.parseClaims(token).getId());
        int exprTime = 3600000  ;

        LogInResponseDto logInResponseDto = new LogInResponseDto(token, exprTime, user);

        return logInResponseDto;


    }
}
