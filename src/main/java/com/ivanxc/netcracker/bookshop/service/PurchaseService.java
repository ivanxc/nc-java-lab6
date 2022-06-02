package com.ivanxc.netcracker.bookshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.ivanxc.netcracker.bookshop.dto.PurchaseCreateEditDto;
import com.ivanxc.netcracker.bookshop.dto.PurchaseCustomerSurnameShopNameDto;
import com.ivanxc.netcracker.bookshop.dto.PurchaseDateCustomerSurnameDistrictDto;
import com.ivanxc.netcracker.bookshop.dto.PurchaseDateQuantityCustomerSurnameDiscountBookTitleDto;
import com.ivanxc.netcracker.bookshop.dto.PurchaseIdDateCustomerSurnameDto;
import com.ivanxc.netcracker.bookshop.dto.PurchaseQuantityBookTitlePriceRepoDto;
import com.ivanxc.netcracker.bookshop.dto.PurchaseReadDto;
import com.ivanxc.netcracker.bookshop.exception.JsonConvertationException;
import com.ivanxc.netcracker.bookshop.exception.ParameterFormatException;
import com.ivanxc.netcracker.bookshop.mapper.PurchaseCreateEditMapper;
import com.ivanxc.netcracker.bookshop.mapper.PurchaseReadMapper;
import com.ivanxc.netcracker.bookshop.repository.PurchaseRepository;
import java.sql.Timestamp;
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
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseReadMapper purchaseReadMapper;
    private final PurchaseCreateEditMapper purchaseCreateEditMapper;
    ObjectMapper objectMapper = new ObjectMapper();

    public List<PurchaseReadDto> findAll() {
        return purchaseRepository.findAll()
            .stream()
            .map(purchaseReadMapper::map)
            .collect(Collectors.toList());
    }

    public Optional<PurchaseReadDto> findById(long id) {
        return purchaseRepository.findById(id)
            .map(purchaseReadMapper::map);
    }

    @Transactional
    public PurchaseReadDto create(PurchaseCreateEditDto purchaseDto) {
        return Optional.of(purchaseDto)
            .map(purchaseCreateEditMapper::map)
            .map(purchaseRepository::save)
            .map(purchaseReadMapper::map)
            .orElseThrow();
    }

    @Transactional
    public Optional<PurchaseReadDto> update(long id, PurchaseCreateEditDto purchaseDto) {
        // try-catch вместо валидации..
        try {
            return purchaseRepository.findById(id)
                .map(entity -> purchaseCreateEditMapper.map(purchaseDto, entity))
                .map(purchaseRepository::saveAndFlush)
                .map(purchaseReadMapper::map);
        } catch (NullPointerException e) {
            throw new ParameterFormatException("Passed not existing value");
        }
    }

    @Transactional
    public PurchaseReadDto patch(long id, JsonPatch patch) throws JsonConvertationException {
        PurchaseCreateEditDto purchaseCreateEditDto = purchaseRepository.findById(id)
            .map(purchaseCreateEditMapper::map)
            .orElseThrow(() -> new ResourceNotFoundException("No purchase with ID = " + id));

        try {
            JsonNode patched = patch.apply(
                objectMapper.convertValue(purchaseCreateEditDto, JsonNode.class));
            purchaseCreateEditDto = objectMapper.treeToValue(patched, PurchaseCreateEditDto.class);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new JsonConvertationException(e.getMessage());
        }

        return update(id, purchaseCreateEditDto).get();
    }

    @Transactional
    public boolean delete(long id) {
        return purchaseRepository.findById(id)
            .map(entity -> {
                purchaseRepository.delete(entity);
                purchaseRepository.flush();
                return true;
            }).orElse(false);
    }

    public List<Timestamp> findDistinctDates() {
        return purchaseRepository.findDistinctDates();
    }

    public List<PurchaseCustomerSurnameShopNameDto> findCustomerSurnameAndShopNameForEachPurchase() {
        return purchaseRepository.findCustomerSurnameAndShopNameForEachPurchase();
    }

    public List<PurchaseDateQuantityCustomerSurnameDiscountBookTitleDto>
    findDateQuantityDiscountSurnameTitle() {
        return purchaseRepository.findDateQuantityDiscountSurnameTitle();
    }

    public List<PurchaseIdDateCustomerSurnameDto>
    findPurchasesIdDateCustomerSurnameWhereTotalPriceMoreThan(float total) {
        return purchaseRepository.findPurchasesIdDateCustomerSurnameWhereTotalPriceMoreThan(total);
    }

    public List<PurchaseDateCustomerSurnameDistrictDto>
    findPurchasesInCustomerDistrictAfter(Timestamp date) {
        return purchaseRepository.findPurchasesInCustomerDistrictAfter(date);
    }

    public List<PurchaseQuantityBookTitlePriceRepoDto>
    findBoughtBooksInSameDistrictAsRepoWhereStoredQuantityMoreThan(int storedQuantity) {
        return purchaseRepository
            .findBoughtBooksInSameDistrictAsRepoWhereStoredQuantityMoreThan(storedQuantity);
    }
}
