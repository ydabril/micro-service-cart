package com.emazon.mscart.adapters.driving.http.mapper;

import com.emazon.mscart.adapters.driving.http.dto.request.ArticleCartRequest;
import com.emazon.mscart.domain.model.ArticleCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IArticleCartRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "registrationDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    ArticleCart toModel(ArticleCartRequest articleCartRequest);
}
