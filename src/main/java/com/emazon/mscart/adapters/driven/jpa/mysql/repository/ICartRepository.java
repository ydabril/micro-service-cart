package com.emazon.mscart.adapters.driven.jpa.mysql.repository;

import com.emazon.mscart.adapters.driven.jpa.mysql.entity.ArticleCartEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ICartRepository extends JpaRepository<ArticleCartEntity, Long> {
    Optional<ArticleCartEntity> findByUserIdAndArticleId(Long userId, Long articleId);
    List<ArticleCartEntity> findByUserId(Long userId);

    Optional<ArticleCartEntity> findById(Long id);

    @Query("SELECT a.articleId FROM ArticleCartEntity a WHERE a.userId = :userId")
    List<Long> findArticleIdByUserId(@Param("userId") Long userId);
}
