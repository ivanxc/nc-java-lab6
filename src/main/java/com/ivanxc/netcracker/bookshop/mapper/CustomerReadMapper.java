package com.ivanxc.netcracker.bookshop.mapper;

import com.ivanxc.netcracker.bookshop.dto.CustomerCreateEditDto;
import com.ivanxc.netcracker.bookshop.dto.CustomerReadDto;
import com.ivanxc.netcracker.bookshop.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerReadMapper implements Mapper<Customer, CustomerReadDto> {

    @Override
    public CustomerReadDto map(Customer from) {
        return new CustomerReadDto(
            from.getId(),
            from.getSurname(),
            from.getDistrict(),
            from.getDiscount()
        );
    }

    public CustomerReadDto map(CustomerCreateEditDto from, long id) {
        return new CustomerReadDto(
            id,
            from.getSurname(),
            from.getDistrict(),
            from.getDiscount()
        );
    }
}
