package com.ivanxc.netcracker.bookshop.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.ivanxc.netcracker.bookshop.dto.CustomerCreateEditDto;
import com.ivanxc.netcracker.bookshop.dto.CustomerReadDto;
import com.ivanxc.netcracker.bookshop.dto.CustomerSurnameDiscountDto;
import com.ivanxc.netcracker.bookshop.exception.ParameterFormatException;
import com.ivanxc.netcracker.bookshop.exception.ResourceNotFoundException;
import com.ivanxc.netcracker.bookshop.response.DeleteResponse;
import com.ivanxc.netcracker.bookshop.service.CustomerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerRestController {

    private final CustomerService customerService;

    @GetMapping
    List<CustomerReadDto> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    CustomerReadDto findById(@PathVariable Long id) {
        return customerService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No customer with ID = " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CustomerReadDto create(@RequestBody CustomerCreateEditDto customer) {
        return customerService.create(customer);
    }

    @PutMapping("/{id}")
    CustomerReadDto updateById(@PathVariable Long id, @RequestBody CustomerCreateEditDto customer) {
        return customerService.update(id, customer)
            .orElseThrow(() -> new ResourceNotFoundException("No customer with ID = " + id));
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public CustomerReadDto patchById(@PathVariable Long id, @RequestBody JsonPatch patch) {
        return customerService.patch(id, patch);
    }

    @DeleteMapping("/{id}")
    DeleteResponse delete(@PathVariable Long id) {
        if (customerService.delete(id)) {
            return new DeleteResponse("Customer with ID = " + id + " was deleted");
        } else {
            throw new ResourceNotFoundException("No customer with ID = " + id);
        }
    }

    @GetMapping("/districts/distinct")
    List<String> findDistinctDistricts() {
        return customerService.findDistinctDistricts();
    }

    @GetMapping("/surname-and-discount-by-district")
    public List<CustomerSurnameDiscountDto> findCustomerSurnameAndDiscountLivingIn(
        @RequestParam String district) {
        if (district.length() == 0) {
            throw new ParameterFormatException("Parameter district cannot be empty.");
        }
        return customerService.findCustomerSurnameAndDiscountLivingIn(district);
    }
}
