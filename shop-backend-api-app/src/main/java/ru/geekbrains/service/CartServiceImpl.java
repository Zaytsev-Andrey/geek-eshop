package ru.geekbrains.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import ru.geekbrains.controller.dto.ProductDto;
import ru.geekbrains.service.dto.LineItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    private Map<LineItem, Integer> lineItems = new HashMap<>();

    @Override
    public void addProductQty(ProductDto productDto, String color, String material,
                              boolean saveGiftWrap, boolean giftWrap, int qty) {
        LineItem lineItem = new LineItem(productDto, color, material, saveGiftWrap, giftWrap, qty);
        lineItems.put(lineItem, lineItems.getOrDefault(lineItem, 0) + qty);
    }

    @Override
    public void updateProductQty(ProductDto productDto, String color, String material,
                                 boolean saveGiftWrap, boolean giftWrap, int qty) {
        LineItem lineItem = new LineItem(productDto, color, material, saveGiftWrap, giftWrap, qty);
        if (saveGiftWrap != giftWrap) {
            lineItems.remove(lineItem);
            lineItem.setSaveGiftWrap(giftWrap);
            qty += lineItems.getOrDefault(lineItem, 0);
        }

        lineItems.put(lineItem, qty);
    }

    @Override
    public void removeProductQty(ProductDto productDto, String color, String material, boolean giftWrap, int qty) {

    }

    @Override
    public void removeProduct(ProductDto productDto, String color, String material,
                              boolean saveGiftWrap, boolean giftWrap) {
        LineItem lineItem = new LineItem(productDto, color, material, saveGiftWrap, giftWrap, null);
        lineItems.remove(lineItem);
    }

    @Override
    public void clear() {
        lineItems.clear();
    }

    @Override
    public List<LineItem> getLineItems() {
        lineItems.forEach(LineItem::setQty);
        return new ArrayList<>(lineItems.keySet());
    }

    @Override
    public BigDecimal getSubTotal() {
        lineItems.forEach(LineItem::setQty);
        return lineItems.keySet().stream()
                .map(LineItem::getItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
