package com.emazon.mscart.domain.util;

public class OutOfStockResponse {
    private String message;
    private String estimatedRestockDate;

    public OutOfStockResponse(String message, String estimatedRestockDate) {
        this.message = message;
        this.estimatedRestockDate = estimatedRestockDate;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEstimatedRestockDate() {
        return estimatedRestockDate;
    }

    public void setEstimatedRestockDate(String estimatedRestockDate) {
        this.estimatedRestockDate = estimatedRestockDate;
    }
}
