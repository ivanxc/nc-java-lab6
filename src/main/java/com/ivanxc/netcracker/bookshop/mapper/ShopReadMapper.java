package com.ivanxc.netcracker.bookshop.mapper;

import com.ivanxc.netcracker.bookshop.dto.ShopCreateEditDto;
import com.ivanxc.netcracker.bookshop.dto.ShopReadDto;
import com.ivanxc.netcracker.bookshop.entity.Shop;
import org.springframework.stereotype.Component;

@Component
public class ShopReadMapper implements Mapper<Shop, ShopReadDto> {

    @Override
    public ShopReadDto map(Shop from) {
        return new ShopReadDto(
            from.getId(),
            from.getName(),
            from.getDistrict(),
            from.getCommission()
        );
    }

    public ShopReadDto map(ShopCreateEditDto from, long id) {
        return new ShopReadDto(
            id,
            from.getName(),
            from.getDistrict(),
            from.getCommission()
        );
    }
}
