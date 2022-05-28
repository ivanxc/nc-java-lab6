package com.ivanxc.netcracker.bookshop.repository;

import com.ivanxc.netcracker.bookshop.dto.CustomerSurnameDiscountDto;
import com.ivanxc.netcracker.bookshop.entity.Customer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select distinct c.district from Customer c")
    List<String> findDistinctDistricts();

    @Query("select c.surname as surname, c.discount as discount from Customer c where c.district = :district")
    List<CustomerSurnameDiscountDto> findCustomerSurnameAndDiscountLivingIn(String district);

}
