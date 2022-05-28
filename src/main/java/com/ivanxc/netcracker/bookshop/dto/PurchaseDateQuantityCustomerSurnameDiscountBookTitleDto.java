package com.ivanxc.netcracker.bookshop.dto;

import com.ivanxc.netcracker.bookshop.entity.Purchase;
import java.sql.Timestamp;
import java.util.Objects;

public interface PurchaseDateQuantityCustomerSurnameDiscountBookTitleDto {
    Timestamp getPurchaseDate();
    Integer getPurchaseQuantity();
    String getCustomerSurname();
    Short getCustomerDiscount();
    String getBookTitle();
}
