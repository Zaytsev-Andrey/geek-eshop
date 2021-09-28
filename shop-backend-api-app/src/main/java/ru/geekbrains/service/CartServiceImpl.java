package ru.geekbrains.service;

import com.fasterxml.jackson.annotation.*;
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
import java.util.stream.Collectors;

@Service
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    private Map<LineItem, Integer> lineItems;

    public CartServiceImpl() {
        this.lineItems = new HashMap<>();
    }

    @JsonCreator
    public CartServiceImpl(@JsonProperty("lineItems") List<LineItem> lineItems) {
        this.lineItems = lineItems.stream().collect(Collectors.toMap(li -> li, LineItem::getQty));
    }

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
    public boolean isEmpty() {
        return lineItems.isEmpty();
    }

    @Override
    public List<LineItem> getLineItems() {
        lineItems.forEach(LineItem::setQty);
        return new ArrayList<>(lineItems.keySet());
    }

    @JsonIgnore
    @Override
    public BigDecimal getSubTotal() {
        lineItems.forEach(LineItem::setQty);
        return lineItems.keySet().stream()
                .map(LineItem::getItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
