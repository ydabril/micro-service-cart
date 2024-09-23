package com.emazon.mscart.domain.model;

import java.math.BigDecimal;
import java.util.List;

public class Article {
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private Long quantity;
    private Brand brand;
    private List<Category> categories ;

    public Article(long id, String name, BigDecimal price, Long quantity, Brand brand, List<Category> categories) {


        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.brand = brand;
        this.categories = categories;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
    public Long getQuantity() {
        return quantity;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}