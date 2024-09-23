package com.emazon.mscart.adapters.driven.jpa.mysql.repository;

import com.emazon.mscart.adapters.driven.jpa.mysql.entity.ArticleCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ICartRepository extends JpaRepository<ArticleCartEntity, Long> {
    Optional<ArticleCartEntity> findByUserIdAndArticleId(Long userId, Long articleId);
    List<ArticleCartEntity> findByUserId(Long userId);
}
