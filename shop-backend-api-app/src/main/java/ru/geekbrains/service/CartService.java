package ru.geekbrains.service;

import ru.geekbrains.dto.AllCartDto;
import ru.geekbrains.dto.CartItemDto;
import ru.geekbrains.persist.CartItem;
import ru.geekbrains.persist.Product;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CartService {
	
	void addToCart(CartItemDto cartItemDto);
	
	AllCartDto updateCart(CartItemDto cartItemDto);
	
	AllCartDto getCartDto();
	
	List<CartItem> getCartItems();
	
	Map<UUID, Product> getCartProducts(List<CartItem> cartItems);
	
	AllCartDto clear();
	
	AllCartDto deleteItem(CartItemDto cartItemDto);
	
	boolean isEmpty();

}
