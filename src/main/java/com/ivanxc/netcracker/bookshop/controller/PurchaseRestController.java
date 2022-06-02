package com.ivanxc.netcracker.bookshop.controller;

import com.github.fge.jsonpatch.JsonPatch;
import com.ivanxc.netcracker.bookshop.dto.PurchaseCreateEditDto;
import com.ivanxc.netcracker.bookshop.dto.PurchaseCustomerSurnameShopNameDto;
import com.ivanxc.netcracker.bookshop.dto.PurchaseDateCustomerSurnameDistrictDto;
import com.ivanxc.netcracker.bookshop.dto.PurchaseDateQuantityCustomerSurnameDiscountBookTitleDto;
import com.ivanxc.netcracker.bookshop.dto.PurchaseIdDateCustomerSurnameDto;
import com.ivanxc.netcracker.bookshop.dto.PurchaseQuantityBookTitlePriceRepoDto;
import com.ivanxc.netcracker.bookshop.dto.PurchaseReadDto;
import com.ivanxc.netcracker.bookshop.exception.ParameterFormatException;
import com.ivanxc.netcracker.bookshop.exception.ResourceNotFoundException;
import com.ivanxc.netcracker.bookshop.response.DeleteResponse;
import com.ivanxc.netcracker.bookshop.service.PurchaseService;
import java.sql.Timestamp;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseRestController {

    private final PurchaseService purchaseService;

    @GetMapping
    public List<PurchaseReadDto> findAll() {
        return purchaseService.findAll();
    }

    @GetMapping("/{id}")
    public PurchaseReadDto findById(@PathVariable Long id) {
        return purchaseService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No purchase with ID = " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PurchaseReadDto create(@RequestBody PurchaseCreateEditDto purchase) {
        return purchaseService.create(purchase);
    }

    @PutMapping("/{id}")
    public PurchaseReadDto updateById(@PathVariable Long id, @RequestBody PurchaseCreateEditDto purchase) {
        return purchaseService.update(id, purchase)
            .orElseThrow(() -> new ResourceNotFoundException("No purchase with ID = " + id));
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public PurchaseReadDto patchById(@PathVariable Long id, @RequestBody JsonPatch patch) {
        return purchaseService.patch(id, patch);
    }

    @DeleteMapping("/{id}")
    public DeleteResponse delete(@PathVariable Long id) {
        if (purchaseService.delete(id)) {
            return new DeleteResponse("Purchase with ID = " + id + " was deleted");
        } else {
            throw new ResourceNotFoundException("No purchase with ID = " + id);
        }
    }

    @GetMapping("/dates/distinct")
    public List<Timestamp> findDistinctDates() {
        return purchaseService.findDistinctDates();
    }

    @GetMapping("/customer-surname-and-shop-name-for-each")
    public List<PurchaseCustomerSurnameShopNameDto> findCustomerSurnameAndShopNameForEachPurchase() {
        return purchaseService.findCustomerSurnameAndShopNameForEachPurchase();
    }

    @GetMapping("/date-quantity-and-customer-discount-surname-and-book-title")
    public List<PurchaseDateQuantityCustomerSurnameDiscountBookTitleDto>
    findDateQuantityDiscountSurnameTitle() {
        return purchaseService.findDateQuantityDiscountSurnameTitle();
    }

    @GetMapping("/id-date-and-customer-surname-where-total-price-more-than")
    public List<PurchaseIdDateCustomerSurnameDto>
    findPurchasesIdDateCustomerSurnameWhereTotalPriceMoreThan(String total) {
        float totalPrice = Float.parseFloat(total.replace(',', '.'));
        return purchaseService
            .findPurchasesIdDateCustomerSurnameWhereTotalPriceMoreThan(totalPrice);
    }

    @GetMapping("/in-customer-district-after")
    public List<PurchaseDateCustomerSurnameDistrictDto>
    findPurchasesInCustomerDistrictAfter(String date) {
        try {
            Timestamp timestamp = Timestamp.valueOf(date);
            return purchaseService.findPurchasesInCustomerDistrictAfter(timestamp);
        } catch (IllegalArgumentException e) {
            throw new ParameterFormatException("Parameter date doesn't "
                + "match pattern yyyy-[m]m-[d]d hh:mm:ss[.f...].");
        }
    }

    @GetMapping("/books-purchased-in-the-customer-district-and-stored-more-than")
    public List<PurchaseQuantityBookTitlePriceRepoDto>
    findBoughtBooksInSameDistrictAsRepoWhereStoredQuantityMoreThan(Integer storedQuantity) {
        return purchaseService
            .findBoughtBooksInSameDistrictAsRepoWhereStoredQuantityMoreThan(storedQuantity);
    }
}
