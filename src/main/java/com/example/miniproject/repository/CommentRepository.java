package com.example.miniproject.repository;

import com.example.miniproject.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    // item id 당 모든 댓글들을 반환하는 메서드 추가 (직접 추가 가능)
    Page<CommentEntity> findAllByItemId(Long itemId, Pageable pageable);
}
