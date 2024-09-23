package com.emazon.mscart.adapters.driven.jpa.mysql.adapter;

import com.emazon.mscart.adapters.driven.jpa.mysql.entity.ArticleCartEntity;
import com.emazon.mscart.adapters.driven.jpa.mysql.mapper.IArticleCartEntityMapper;
import com.emazon.mscart.adapters.driven.jpa.mysql.repository.ICartRepository;
import com.emazon.mscart.domain.model.Article;
import com.emazon.mscart.domain.model.ArticleCart;
import com.emazon.mscart.domain.spi.IArticleCartPersistencePort;
import com.emazon.mscart.infraestructure.feign_client.StockFeignClient;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ArticleCartAdapter implements IArticleCartPersistencePort {
    private final IArticleCartEntityMapper articleCartEntityMapper;
    private final ICartRepository cartRepository;

    @Override
    public void addArticleCart(ArticleCart articleCart) {
        cartRepository.save(articleCartEntityMapper.toEntity(articleCart));
    }

    @Override
    public Optional<ArticleCart> findByUserIdAndArticleId(Long userId, Long articleId) {
        Optional<ArticleCartEntity> articleCartEntity = cartRepository.findByUserIdAndArticleId(userId, articleId);
        return articleCartEntity.map(articleCartEntityMapper::toModel);
    }

    @Override
    public void updateArticleCart(ArticleCart articleCart) {
        cartRepository.save(articleCartEntityMapper.toEntity(articleCart));
    }

    @Override
    public void updateAll(List<ArticleCart> articleCarts) {
        List<ArticleCartEntity> articleCartEntities = articleCarts.stream()
                .map(articleCartEntityMapper::toEntity)
                .toList();

        cartRepository.saveAll(articleCartEntities);
    }

    @Override
    public List<ArticleCart> findByUserId(Long userId) {
        return cartRepository.findByUserId(userId).stream()
                .map(articleCartEntityMapper::toModel)
                .toList();
    }
}
