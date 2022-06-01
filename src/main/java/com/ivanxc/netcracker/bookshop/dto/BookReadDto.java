package com.ivanxc.netcracker.bookshop.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class BookReadDto {
    long id;
    String title;
    int price;
    String repo;
    int quantity;

    @JsonCreator
    public BookReadDto(@JsonProperty("id") long id, @JsonProperty("title") String title,
        @JsonProperty("price") int price, @JsonProperty("repo") String repo,
        @JsonProperty("quantity") int quantity) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.repo = repo;
        this.quantity = quantity;
    }
}
