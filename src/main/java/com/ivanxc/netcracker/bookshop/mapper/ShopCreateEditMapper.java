package com.ivanxc.netcracker.bookshop.mapper;

import com.ivanxc.netcracker.bookshop.dto.ShopCreateEditDto;
import com.ivanxc.netcracker.bookshop.dto.ShopReadDto;
import com.ivanxc.netcracker.bookshop.entity.Shop;
import org.springframework.stereotype.Component;

@Component
public class ShopCreateEditMapper implements Mapper<ShopCreateEditDto, Shop> {

    @Override
    public Shop map(ShopCreateEditDto from) {
        Shop shop = new Shop();
        copy(from, shop);
        return shop;
    }

    public ShopCreateEditDto map(Shop from) {
        return new ShopCreateEditDto(
            from.getName(),
            from.getDistrict(),
            from.getCommission()
        );
    }

    public ShopCreateEditDto map(ShopReadDto from) {
        return new ShopCreateEditDto(
            from.getName(),
            from.getDistrict(),
            from.getCommission()
        );
    }

    @Override
    public Shop map(ShopCreateEditDto from, Shop to) {
        copy(from, to);
        return to;
    }

    private void copy(ShopCreateEditDto from, Shop to) {
        to.setName(from.getName());
        to.setDistrict(from.getDistrict());
        to.setCommission(from.getCommission());
    }
}
