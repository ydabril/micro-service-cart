package com.emazon.mscart.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ArticleCart {
    private final Long id;
    private Long userId;
    private final Long articleId;
    private Long quantity;
    private LocalDate registrationDate;
    private LocalDate updateDate;

    public ArticleCart(Long id, Long userId, Long articleId, Long quantity, LocalDate registrationDate, LocalDate updateDate) {
        this.id = id;
        this.userId = userId;
        this.articleId = articleId;
        this.quantity = quantity;
        this.registrationDate = registrationDate;
        this.updateDate = updateDate;
    }

    public Long getId() {
        return id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public Long getUserId() {
        return userId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public Long getQuantity() {
        return quantity;
    }


    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
