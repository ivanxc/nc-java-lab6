package com.ivanxc.netcracker.bookshop.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class BookCreateEditDto {
    String title;
    Integer price;
    String repo;
    Integer quantity;

    @JsonCreator
    public BookCreateEditDto(@JsonProperty("title") String title, @JsonProperty("price") Integer price,
        @JsonProperty("repo") String repo, @JsonProperty("quantity") Integer quantity) {
        this.title = title;
        this.price = price;
        this.repo = repo;
        this.quantity = quantity;
    }
}
