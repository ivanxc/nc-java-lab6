package com.ivanxc.netcracker.bookshop.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ShopCreateEditDto {
    String name;
    String district;
    Short commission;

    @JsonCreator
    public ShopCreateEditDto(@JsonProperty("name") String name,
        @JsonProperty("district") String district, @JsonProperty("commission") Short commission) {
        this.name = name;
        this.district = district;
        this.commission = commission;
    }
}
