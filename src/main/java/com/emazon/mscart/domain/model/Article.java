package com.emazon.mscart.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Article {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    private Long cartId;
    private Long quantity;
    private Long stock;
    private Brand brand;
    private List<Category> categories ;

    private String imagePath;

    private LocalDate restockNextDate;

    public Article(Long id, String name, BigDecimal price, Long cartId, Long quantity, Long stock, Brand brand, List<Category> categories, String imagePath, LocalDate restockNextDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.cartId = cartId;
        this.quantity = quantity;
        this.stock = stock;
        this.brand = brand;
        this.categories = categories;
        this.imagePath = imagePath;
        this.restockNextDate = restockNextDate;
    }

    public Long getId() {
        return id;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
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

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public LocalDate getRestockNextDate() {
        return restockNextDate;
    }

    public void setRestockNextDate(LocalDate restockNextDate) {
        this.restockNextDate = restockNextDate;
    }
}