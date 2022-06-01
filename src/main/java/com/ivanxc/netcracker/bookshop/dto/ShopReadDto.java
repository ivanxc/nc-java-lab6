package com.ivanxc.netcracker.bookshop.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ShopReadDto {
    Long id;
    String name;
    String district;
    Short commission;

    @JsonCreator
    public ShopReadDto(@JsonProperty("id") Long id, @JsonProperty("name") String name,
        @JsonProperty("district") String district, @JsonProperty("commission") Short commission) {
        this.id = id;
        this.name = name;
        this.district = district;
        this.commission = commission;
    }
}
