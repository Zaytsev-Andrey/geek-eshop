package ru.geekbrains.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.controller.dto.AddLineItemDto;
import ru.geekbrains.controller.dto.AllCartDto;
import ru.geekbrains.controller.dto.ProductDto;
import ru.geekbrains.service.CartService;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.service.dto.LineItem;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private static Logger logger = LoggerFactory.getLogger(CartController.class);

    private CartService cartService;

    private final ProductService productService;

    @Autowired
    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public List<LineItem> addToCart(@RequestBody AddLineItemDto addLineItemDto) {
        logger.info("New LineItem. ProductId={}, qty={}", addLineItemDto.getProductId(), addLineItemDto.getQty());

        ProductDto productDto = productService.findById(addLineItemDto.getProductId())
                .orElseThrow(RuntimeException::new);
        cartService.addProductQty(productDto,
                addLineItemDto.getColor(),
                addLineItemDto.getMaterial(),
                addLineItemDto.isSaveGiftWrap(),
                addLineItemDto.isGiftWrap(),
                addLineItemDto.getQty());

        return cartService.getLineItems();
    }

    @GetMapping("/all")
    public AllCartDto findAll() {
        return new AllCartDto(cartService.getLineItems(), cartService.getSubTotal());
    }

    @PostMapping(path = "/update", produces = "application/json", consumes = "application/json")
    public AllCartDto updateLineItem(@RequestBody AddLineItemDto addLineItemDto) {
        logger.info("Updating LineItem. ProductId={}, qty={}", addLineItemDto.getProductId(), addLineItemDto.getQty());

        ProductDto productDto = productService.findById(addLineItemDto.getProductId())
                .orElseThrow(RuntimeException::new);

        cartService.updateProductQty(productDto,
                addLineItemDto.getColor(),
                addLineItemDto.getMaterial(),
                addLineItemDto.isSaveGiftWrap(),
                addLineItemDto.isGiftWrap(),
                addLineItemDto.getQty());

        return new AllCartDto(cartService.getLineItems(), cartService.getSubTotal());
    }

    @PostMapping(path = "/remove", produces = "application/json", consumes = "application/json")
    public AllCartDto removeLineItem(@RequestBody AddLineItemDto addLineItemDto) {
        logger.info("Deleting LineItem. ProductId={}, qty={}", addLineItemDto.getProductId(), addLineItemDto.getQty());

        ProductDto productDto = productService.findById(addLineItemDto.getProductId())
                .orElseThrow(RuntimeException::new);

        cartService.removeProduct(productDto,
                addLineItemDto.getColor(),
                addLineItemDto.getMaterial(),
                addLineItemDto.isSaveGiftWrap(),
                addLineItemDto.isGiftWrap());

        return new AllCartDto(cartService.getLineItems(), cartService.getSubTotal());
    }

    @DeleteMapping
    public AllCartDto clear() {
        logger.info("Clearing cart");

        cartService.clear();

        return new AllCartDto(cartService.getLineItems(), cartService.getSubTotal());
    }
}
