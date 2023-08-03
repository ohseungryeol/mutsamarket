package com.example.miniproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/review")
public class ReviewTestController {
    @PostMapping
    public ResponseEntity<String> writeReview(){
        return ResponseEntity.ok().body("리뷰 등록 완료");
    }
}
