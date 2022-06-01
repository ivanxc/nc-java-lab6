package com.ivanxc.netcracker.bookshop.mapper;

import com.ivanxc.netcracker.bookshop.dto.CustomerCreateEditDto;
import com.ivanxc.netcracker.bookshop.dto.CustomerReadDto;
import com.ivanxc.netcracker.bookshop.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerCreateEditMapper implements Mapper<CustomerCreateEditDto, Customer> {

    @Override
    public Customer map(CustomerCreateEditDto from) {
        Customer customer = new Customer();
        copy(from, customer);
        return customer;
    }

    public CustomerCreateEditDto map(Customer from) {
        return new CustomerCreateEditDto(
            from.getSurname(),
            from.getDistrict(),
            from.getDiscount()
        );
    }

    public CustomerCreateEditDto map(CustomerReadDto from) {
        return new CustomerCreateEditDto(
            from.getSurname(),
            from.getDistrict(),
            from.getDiscount()
        );
    }

    @Override
    public Customer map(CustomerCreateEditDto from, Customer to) {
        copy(from, to);
        return to;
    }

    private void copy(CustomerCreateEditDto from, Customer to) {
        to.setSurname(from.getSurname());
        to.setDistrict(from.getDistrict());
        to.setDiscount(from.getDiscount());
    }
}
