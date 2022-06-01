package com.ivanxc.netcracker.bookshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.ivanxc.netcracker.bookshop.dto.CustomerCreateEditDto;
import com.ivanxc.netcracker.bookshop.dto.CustomerReadDto;
import com.ivanxc.netcracker.bookshop.dto.CustomerSurnameDiscountDto;
import com.ivanxc.netcracker.bookshop.exception.JsonConvertationException;
import com.ivanxc.netcracker.bookshop.mapper.CustomerCreateEditMapper;
import com.ivanxc.netcracker.bookshop.mapper.CustomerReadMapper;
import com.ivanxc.netcracker.bookshop.repository.CustomerRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerReadMapper customerReadMapper;
    private final CustomerCreateEditMapper customerCreateEditMapper;
    ObjectMapper objectMapper = new ObjectMapper();

    public List<CustomerReadDto> findAll() {
        return customerRepository.findAll()
            .stream()
            .map(customerReadMapper::map)
            .collect(Collectors.toList());
    }

    public Optional<CustomerReadDto> findById(long id) {
        return customerRepository.findById(id)
            .map(customerReadMapper::map);
    }

    @Transactional
    public CustomerReadDto create(CustomerCreateEditDto customerDto) {
        return Optional.of(customerDto)
            .map(customerCreateEditMapper::map)
            .map(customerRepository::save)
            .map(customerReadMapper::map)
            .orElseThrow();
    }

    @Transactional
    public Optional<CustomerReadDto> update(long id, CustomerCreateEditDto customerDto) {
        return customerRepository.findById(id)
            .map(entity -> customerCreateEditMapper.map(customerDto, entity))
            .map(customerRepository::saveAndFlush)
            .map(customerReadMapper::map);
    }

    @Transactional
    public CustomerReadDto patch(long id, JsonPatch patch) throws JsonConvertationException {
        CustomerCreateEditDto customerCreateEditDto = customerRepository.findById(id)
            .map(customerCreateEditMapper::map)
            .orElseThrow(() -> new ResourceNotFoundException("No customer with ID = " + id));

        try {
            JsonNode patched = patch.apply(
                objectMapper.convertValue(customerCreateEditDto, JsonNode.class));
            customerCreateEditDto = objectMapper.treeToValue(patched, CustomerCreateEditDto.class);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new JsonConvertationException(e.getMessage());
        }

        return update(id, customerCreateEditDto)
            .orElseThrow(() -> new ResourceNotFoundException("No customer with ID = " + id));
    }

    @Transactional
    public boolean delete(long id) {
        return customerRepository.findById(id)
            .map(entity -> {
                customerRepository.delete(entity);
                customerRepository.flush();
                return true;
            }).orElse(false);
    }

    public List<String> findDistinctDistricts() {
        return customerRepository.findDistinctDistricts();
    }

    public List<CustomerSurnameDiscountDto> findCustomerSurnameAndDiscountLivingIn(String district) {
        return customerRepository.findCustomerSurnameAndDiscountLivingIn(district);
    }
}
