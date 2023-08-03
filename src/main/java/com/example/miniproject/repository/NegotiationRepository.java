package com.example.miniproject.repository;

import com.example.miniproject.entity.CommentEntity;
import com.example.miniproject.entity.NegotiationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NegotiationRepository extends JpaRepository<NegotiationEntity,Long> {
    Page<NegotiationEntity> findAllByItemId(Long itemId, Pageable pageable);
    Page<NegotiationEntity> findAllById(Long Id, Pageable pageable);
}
