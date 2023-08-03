package com.example.miniproject.controller;

import com.example.miniproject.dto.LogInResponseDto;
import com.example.miniproject.dto.LoginRequestDto;
import com.example.miniproject.dto.MessageDto;
import com.example.miniproject.dto.UserJoinRequestDto;
import com.example.miniproject.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController //html을 응답할 때는 @Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
    //private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsManager manager;
    private final UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "login-form";
    }


    /*@GetMapping("/register")
    public String signUpForm() {
        return "register-form";
    }*/


    @PostMapping("/register")
    public String signUpRequest(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("password-check") String passwordCheck
    ) {
        if (password.equals(passwordCheck)) //새로운 사용자 추가
            manager.createUser(User.withUsername(username)
                    .password(passwordEncoder.encode(password))
                    .build()
            );
        return "redirect:/users/login";
    }

    @GetMapping("/my-profile")
    public String myProfile(Authentication authentication) {
        log.info(authentication.getName());
        log.info(((User) authentication.getPrincipal()).getUsername());

        return "my-profile";
    }
    @PostMapping("/join")
    public MessageDto join(@RequestBody UserJoinRequestDto dto){
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage("회원가입 성공");
        userService.join(dto);

        return messageDto;
    }

    @PostMapping("/login")
    public LogInResponseDto login(@RequestBody LoginRequestDto dto){

        LogInResponseDto logInResponseDto = userService.login(dto);
        return logInResponseDto;

    }
}
