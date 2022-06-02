package com.ivanxc.netcracker.bookshop.dto;

import java.sql.Timestamp;

public interface PurchaseDateQuantityCustomerSurnameDiscountBookTitleDto {
    Timestamp getPurchaseDate();
    Integer getPurchaseQuantity();
    String getCustomerSurname();
    Short getCustomerDiscount();
    String getBookTitle();
}
