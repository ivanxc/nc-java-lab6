package com.ivanxc.netcracker.bookshop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.ivanxc.netcracker.bookshop.dto.ShopCreateEditDto;
import com.ivanxc.netcracker.bookshop.dto.ShopReadDto;
import com.ivanxc.netcracker.bookshop.entity.Shop;
import com.ivanxc.netcracker.bookshop.exception.JsonConvertationException;
import com.ivanxc.netcracker.bookshop.mapper.ShopCreateEditMapper;
import com.ivanxc.netcracker.bookshop.mapper.ShopReadMapper;
import com.ivanxc.netcracker.bookshop.repository.ShopRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;
    private final ShopReadMapper shopReadMapper;
    private final ShopCreateEditMapper shopCreateEditMapper;
    ObjectMapper objectMapper = new ObjectMapper();

    public List<ShopReadDto> findAll() {
        return shopRepository.findAll()
            .stream()
            .map(shopReadMapper::map)
            .collect(Collectors.toList());
    }

    public Optional<ShopReadDto> findById(long id) {
        return shopRepository.findById(id)
            .map(shopReadMapper::map);
    }

    @Transactional
    public ShopReadDto create(ShopCreateEditDto shopDto) {
        return Optional.of(shopDto)
            .map(shopCreateEditMapper::map)
            .map(shopRepository::save)
            .map(shopReadMapper::map)
            .orElseThrow();
    }

    @Transactional
    public Optional<ShopReadDto> update(long id, ShopCreateEditDto shopDto) {
        return shopRepository.findById(id)
            .map(entity -> shopCreateEditMapper.map(shopDto, entity))
            .map(shopRepository::saveAndFlush)
            .map(shopReadMapper::map);
    }

    @Transactional
    public ShopReadDto patch(long id, JsonPatch patch) throws JsonConvertationException {
        ShopCreateEditDto shopCreateEditDto = shopRepository.findById(id)
            .map(shopCreateEditMapper::map)
            .orElseThrow(() -> new ResourceNotFoundException("No shop with ID = " + id));

        try {
            JsonNode patched = patch.apply(
                objectMapper.convertValue(shopCreateEditDto, JsonNode.class));
            shopCreateEditDto = objectMapper.treeToValue(patched, ShopCreateEditDto.class);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new JsonConvertationException(e.getMessage());
        }

        return update(id, shopCreateEditDto).get();
    }

    @Transactional
    public boolean delete(long id) {
        return shopRepository.findById(id)
            .map(entity -> {
                shopRepository.delete(entity);
                shopRepository.flush();
                return true;
            }).orElse(false);
    }

    public List<String> findShopsNameOfDistricts(String[] districts) {
        return shopRepository.findShopsNameOfDistricts(districts);
    }

    public List<Shop> findShopsWhereCustomersHaveDiscountBetweenExcludingDistrict(
        short discountFrom, short discountTo, String excludedDistrict) {
        return shopRepository.findShopsWhereCustomersHaveDiscountBetweenExcludingDistrict(
            discountFrom, discountTo, excludedDistrict);
    }

}
