package ru.geekbrains.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ru.geekbrains.persist.CartItem;

@Component
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartRepositoryImpl implements CartRepository {

	private final Map<CartItem, Integer> cartItems;

	public CartRepositoryImpl() {
		this.cartItems = new HashMap<CartItem, Integer>();
	}

	@JsonCreator
	public CartRepositoryImpl(@JsonProperty("cartItems") List<CartItem> cartItems) {
		this.cartItems = cartItems.stream().collect(Collectors.toMap(item -> item, CartItem::getQty));
	}
	
	@Override
	public List<CartItem> getCartItems() {
		cartItems.forEach(CartItem::setQty);
		return new ArrayList<>(cartItems.keySet());
	}

	@Override
	public void save(CartItem cartItem) {
		cartItems.put(cartItem, cartItems.getOrDefault(cartItem, 0) + cartItem.getQty());
	}
	
	@Override
	public void update(CartItem cartItem) {
		cartItems.replace(cartItem, cartItem.getQty());
	}

	@Override
	public void deleteAll() {
		cartItems.clear();
	}

	@Override
	public void delete(CartItem cartItem) {
		cartItems.remove(cartItem);
	}

	@Override
	public boolean isEmpty() {
		return cartItems.isEmpty();
	}
	
}
