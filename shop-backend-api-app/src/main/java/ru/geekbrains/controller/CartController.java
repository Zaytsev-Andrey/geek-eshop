package ru.geekbrains.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import ru.geekbrains.dto.AllCartDto;
import ru.geekbrains.dto.CartItemDto;
import ru.geekbrains.service.CartService;


@RestController
@RequestMapping("/cart")
@Slf4j
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    
    @GetMapping("/all")
    public AllCartDto getCart() {
    	log.info("Load cart");
    	return cartService.getCartDto();
    }
    
    @PostMapping(produces = "application/json", consumes = "application/json")
    public void addCartItem(@RequestBody CartItemDto cartItemDto) {
    	cartService.addToCart(cartItemDto);
    }
    
    @PutMapping(produces = "application/json", consumes = "application/json")
    public AllCartDto updateCartItem(@RequestBody CartItemDto cartItemDto) {
    	return cartService.updateCart(cartItemDto);
    }
    
    @DeleteMapping("/all")
    public AllCartDto clear() {
    	return cartService.clear();
    }
    
    @DeleteMapping(produces = "application/json", consumes = "application/json")
    public AllCartDto deleteCartItem(@RequestBody CartItemDto cartItemDto) {
    	return cartService.deleteItem(cartItemDto);
    }

}
