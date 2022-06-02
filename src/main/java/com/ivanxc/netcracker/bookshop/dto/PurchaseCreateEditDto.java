package com.ivanxc.netcracker.bookshop.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import lombok.Value;

@Value
public class PurchaseCreateEditDto {
    Timestamp date;
    Long shopId;
    Long customerId;
    Long bookId;
    Integer quantity;
    Float totalPrice;

    @JsonCreator
    public PurchaseCreateEditDto(@JsonProperty("date") Timestamp date,
        @JsonProperty("shopId") Long shopId, @JsonProperty("customerId") Long customerId,
        @JsonProperty("bookId") Long bookId, @JsonProperty("quantity") Integer quantity,
        @JsonProperty("totalPrice") Float totalPrice) {
        this.date = date;
        this.shopId = shopId;
        this.customerId = customerId;
        this.bookId = bookId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
}
