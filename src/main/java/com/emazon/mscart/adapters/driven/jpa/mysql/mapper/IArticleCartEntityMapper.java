package com.emazon.mscart.adapters.driven.jpa.mysql.mapper;

import com.emazon.mscart.adapters.driven.jpa.mysql.entity.ArticleCartEntity;
import com.emazon.mscart.domain.model.ArticleCart;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface IArticleCartEntityMapper {
    ArticleCartEntity toEntity(ArticleCart articleCart);
    ArticleCart toModel(ArticleCartEntity articleCartEntity);

    default Optional<ArticleCart> toArticleOptional(Optional<ArticleCartEntity> articleCartEntityOptional) {
        return articleCartEntityOptional.map(this::toModel);
    }
}
