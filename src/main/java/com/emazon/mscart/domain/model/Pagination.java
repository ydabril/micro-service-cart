package com.emazon.mscart.domain.model;

import java.util.List;

public class Pagination<T> {
    private final List<T> list;
    private final int currentPage;
    private final int pageSize;
    private long totalElements;
    private final int totalPages;
    private final boolean hasNextPage;
    private final boolean hasPreviousPage;
    private Long totalPrice;

    public Pagination(List<T> list, int currentPage, int pageSize, long totalElements, int totalPages, boolean hasNextPage, boolean hasPreviousPage) {
        this.list = list;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.hasNextPage = hasNextPage;
        this.hasPreviousPage = hasPreviousPage;
        this.totalPrice = 0L;
    }

    // Getters y setters para totalPrice
    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public List<T> getList() {
        return list;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }
}
