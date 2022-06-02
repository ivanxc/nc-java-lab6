package com.ivanxc.netcracker.bookshop.mapper;

import com.ivanxc.netcracker.bookshop.dto.PurchaseCreateEditDto;
import com.ivanxc.netcracker.bookshop.dto.PurchaseReadDto;
import com.ivanxc.netcracker.bookshop.entity.Book;
import com.ivanxc.netcracker.bookshop.entity.Customer;
import com.ivanxc.netcracker.bookshop.entity.Purchase;
import com.ivanxc.netcracker.bookshop.entity.Shop;
import com.ivanxc.netcracker.bookshop.repository.BookRepository;
import com.ivanxc.netcracker.bookshop.repository.CustomerRepository;
import com.ivanxc.netcracker.bookshop.repository.ShopRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PurchaseCreateEditMapper implements Mapper<PurchaseCreateEditDto, Purchase> {

    private final BookRepository bookRepository;
    private final CustomerRepository customerRepository;
    private final ShopRepository shopRepository;

    @Override
    public Purchase map(PurchaseCreateEditDto from) {
        Purchase purchase = new Purchase();
        copy(from, purchase);
        return purchase;
    }

    public PurchaseCreateEditDto map(Purchase from) {
        return new PurchaseCreateEditDto(
            from.getDate(),
            from.getShop().getId(),
            from.getCustomer().getId(),
            from.getBook().getId(),
            from.getQuantity(),
            from.getTotalPrice()
        );
    }

    public PurchaseCreateEditDto map(PurchaseReadDto from) {
        return new PurchaseCreateEditDto(
            from.getDate(),
            from.getShopId(),
            from.getCustomerId(),
            from.getBookId(),
            from.getQuantity(),
            from.getTotalPrice()
        );
    }

    @Override
    public Purchase map(PurchaseCreateEditDto from, Purchase to) {
        copy(from, to);
        return to;
    }

    private void copy(PurchaseCreateEditDto from, Purchase to) {
        to.setId(from.getBookId());
        to.setDate(from.getDate());
        to.setShop(getShop(from.getShopId()));
        to.setCustomer(getCustomer(from.getCustomerId()));
        to.setBook(getBook(from.getBookId()));
        to.setQuantity(from.getQuantity());
        to.setTotalPrice(from.getTotalPrice());
    }

    private Shop getShop(Long shopId) {
        return Optional.ofNullable(shopId)
            .flatMap(shopRepository::findById)
            .orElse(null);
    }

    private Customer getCustomer(Long customerId) {
        return Optional.ofNullable(customerId)
            .flatMap(customerRepository::findById)
            .orElse(null);
    }

    private Book getBook(Long bookId) {
        return Optional.ofNullable(bookId)
            .flatMap(bookRepository::findById)
            .orElse(null);
    }
}
