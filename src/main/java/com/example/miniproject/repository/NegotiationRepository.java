package com.example.miniproject.repository;

import com.example.miniproject.entity.CommentEntity;
import com.example.miniproject.entity.NegotiationEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NegotiationRepository extends JpaRepository<NegotiationEntity,Long> {
    Page<NegotiationEntity> findAllByItemId(Long itemId, Pageable pageable);
    Page<NegotiationEntity> findAllById(Long Id, Pageable pageable);

    Page<NegotiationEntity> findByUser_IdAndItemId(Long userId, Long itemId,Pageable pageable);

    @Query("select n " +
            "from NegotiationEntity n " +
            "join fetch n.item " +
            "join fetch n.user " +
            "where n.item.id =:itemId " +
            "and n.user.id =:userId")
    Page<NegotiationEntity> findAllByItemIdAndUserId(@Param("itemId") Long itemId, @Param("userId") Long userId, Pageable pageable);
}
