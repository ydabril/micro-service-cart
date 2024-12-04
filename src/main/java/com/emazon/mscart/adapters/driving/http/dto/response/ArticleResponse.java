package com.emazon.mscart.adapters.driving.http.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
public class ArticleResponse {
    private Long id;
    private String name;
    private Double price;
    private Long cartId;
    private Integer quantity;
    private String stock;
    private BrandResponse brand;
    private List<CategoryResponse> categories;
    private String imagePath;
    private LocalDate restockNextDate;
}
