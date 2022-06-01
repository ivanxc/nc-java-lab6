package com.ivanxc.netcracker.bookshop.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class CustomerCreateEditDto {
    String surname;
    String district;
    Short discount;

    @JsonCreator
    public CustomerCreateEditDto(@JsonProperty("surname") String surname,
        @JsonProperty("district") String district, @JsonProperty("discount") Short discount) {
        this.surname = surname;
        this.district = district;
        this.discount = discount;
    }
}
