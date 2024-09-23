package com.emazon.mscart.adapters.driving.http.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ArticleCartRequest {
    private final Long articleId;
    private final Long quantity;
}
