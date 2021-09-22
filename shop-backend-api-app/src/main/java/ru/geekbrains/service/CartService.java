package ru.geekbrains.service;

import ru.geekbrains.controller.dto.ProductDto;
import ru.geekbrains.service.dto.LineItem;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {

    void addProductQty(ProductDto productDto, String color, String material,
                       boolean saveGiftWrap, boolean giftWrap, int qty);

    void updateProductQty(ProductDto productDto, String color, String material,
                          boolean saveGiftWrap, boolean giftWrap, int qty);

    void removeProductQty(ProductDto productDto, String color, String material, boolean giftWrap, int qty);

    void removeProduct(ProductDto productDto, String color, String material,
                       boolean saveGiftWrap, boolean giftWrap);

    void clear();

    List<LineItem> getLineItems();

    BigDecimal getSubTotal();
}
