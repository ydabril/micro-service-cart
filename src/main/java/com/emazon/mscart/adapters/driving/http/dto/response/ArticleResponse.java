package com.emazon.mscart.adapters.driving.http.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ArticleResponse {
    private Long id;
    private String name;
    private Double price;
    private Integer quantity;
    private BrandResponse brand;
    private List<CategoryResponse> categories;
}
