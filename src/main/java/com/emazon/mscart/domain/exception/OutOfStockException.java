package com.emazon.mscart.domain.exception;

import com.emazon.mscart.domain.util.OutOfStockResponse;

public class OutOfStockException extends RuntimeException {
    private OutOfStockResponse outOfStockResponse;

    public OutOfStockException(OutOfStockResponse outOfStockResponse) {
        this.outOfStockResponse = outOfStockResponse;
    }

    public OutOfStockResponse getOutOfStockResponse() {
        return outOfStockResponse;
    }
}
