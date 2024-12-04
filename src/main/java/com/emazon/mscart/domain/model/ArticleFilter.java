package com.emazon.mscart.domain.model;

import java.util.List;

public class ArticleFilter {
    private List<Long> articleIds;
    private final String sortDirection;
    private Integer page;
    private Integer size;

    private final String categoryName;
    private final String brandName;

    public ArticleFilter(List<Long> articleIds, String sortDirection,  Integer page, Integer size, String caregoryName, String brandName) {
        this.articleIds = articleIds;
        this.sortDirection = sortDirection;
        this.page = page;
        this.size = size;
        this.categoryName = caregoryName;
        this.brandName = brandName;
    }

    public void setArticleIds(List<Long> articleIds) {
        this.articleIds = articleIds;
    }

    public List<Long> getArticleIds() {
        return articleIds;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getSize() {
        return size;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getBrandName() {
        return brandName;
    }
}
