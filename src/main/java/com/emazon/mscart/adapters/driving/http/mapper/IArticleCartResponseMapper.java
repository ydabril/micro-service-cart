package com.emazon.mscart.adapters.driving.http.mapper;

import com.emazon.mscart.adapters.driving.http.dto.response.ArticleResponse;
import com.emazon.mscart.domain.model.Article;
import com.emazon.mscart.domain.model.Pagination;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IArticleCartResponseMapper {
    Pagination<ArticleResponse> toPaginationResponse(Pagination<Article> pagination);
}
