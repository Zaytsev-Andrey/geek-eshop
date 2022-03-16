package ru.geekbrains.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.geekbrains.persist.CartItem;


//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
//@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto implements Serializable {

	private String id;

    private String productTitle;

    private Boolean changeGiftWrap;

    private Boolean giftWrap;
    
    private String cost;
    
    private String totalCost;

    private Integer qty;
    
    
        
    public static CartItemDto fromCartItem(CartItem cartItem) {
    	CartItemDto cartItemDto = new CartItemDto();
    	cartItemDto.setId(cartItem.getId().toString());
    	cartItemDto.setGiftWrap(cartItem.getGiftWrap());
    	cartItemDto.setChangeGiftWrap(cartItem.getGiftWrap());
    	cartItemDto.setQty(cartItem.getQty());
    	return cartItemDto;
    }
    
    public CartItem toCartItem() {
    	CartItem cartItem = new CartItem();
    	cartItem.setId(UUID.fromString(id));
    	cartItem.setGiftWrap(giftWrap);
    	cartItem.setQty(qty);
    	return cartItem;
    }
}
