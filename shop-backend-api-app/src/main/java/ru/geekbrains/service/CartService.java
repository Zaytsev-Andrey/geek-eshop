package ru.geekbrains.service;

import ru.geekbrains.dto.AllCartDto;
import ru.geekbrains.dto.CartItemDto;
import ru.geekbrains.dto.ProductDto;
import ru.geekbrains.persist.CartItem;
import ru.geekbrains.persist.model.Product;

import java.math.BigDecimal;
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
	
	boolean isEmpti();

//    void addProductQty(ProductDto productDto, boolean saveGiftWrap, boolean giftWrap, int qty);
//
//    void updateProductQty(ProductDto productDto, boolean saveGiftWrap, boolean giftWrap, int qty);
//
//    void removeProductQty(ProductDto productDto, boolean giftWrap, int qty);
//
//    void removeProduct(ProductDto productDto, boolean saveGiftWrap, boolean giftWrap);
//
    
//
//    boolean isEmpty();
//
//    List<LineItem> getLineItems();
//
//    BigDecimal getSubTotal();
}
