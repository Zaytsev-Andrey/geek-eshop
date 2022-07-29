package ru.geekbrains.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import ru.geekbrains.persist.CartItem;

public interface CartRepository {

	List<CartItem> getCartItems();
	
	void save(CartItem cartItem);
	
	void update(CartItem cartItem);
	
	void deleteAll();
	
	void delete(CartItem cartItem);
	
	boolean isEmpty();
}
