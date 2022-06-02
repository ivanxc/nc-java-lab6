package com.ivanxc.netcracker.bookshop.mapper;

import com.ivanxc.netcracker.bookshop.dto.PurchaseCreateEditDto;
import com.ivanxc.netcracker.bookshop.dto.PurchaseReadDto;
import com.ivanxc.netcracker.bookshop.entity.Purchase;
import org.springframework.stereotype.Component;

@Component
public class PurchaseReadMapper implements Mapper<Purchase, PurchaseReadDto> {

    @Override
    public PurchaseReadDto map(Purchase from) {
        return new PurchaseReadDto(
            from.getId(),
            from.getDate(),
            from.getShop().getId(),
            from.getCustomer().getId(),
            from.getBook().getId(),
            from.getQuantity(),
            from.getTotalPrice()
        );
    }

    public PurchaseReadDto map(PurchaseCreateEditDto from, long id) {
        return new PurchaseReadDto(
            id,
            from.getDate(),
            from.getShopId(),
            from.getCustomerId(),
            from.getBookId(),
            from.getQuantity(),
            from.getTotalPrice()
        );
    }
}
