package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.geekbrains.dto.AllCartDto;
import ru.geekbrains.dto.CartItemDto;
import ru.geekbrains.mapper.Mapper;
import ru.geekbrains.persist.CartItem;
import ru.geekbrains.persist.Product;
import ru.geekbrains.repository.CartRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
	
	private final CartRepository cartRepository;
	
	private final ProductService productService;

	private final Mapper<CartItem, CartItemDto> cartItemMapper;
	
	@Autowired
	public CartServiceImpl(CartRepository cartRepository, ProductService productService, Mapper<CartItem, CartItemDto> cartItemMapper) {
		this.cartRepository = cartRepository;
		this.productService = productService;
		this.cartItemMapper = cartItemMapper;
	}

	@Override
	public void addToCart(CartItemDto cartItemDto) {
		cartRepository.save(cartItemMapper.toEntity(cartItemDto));
	}
	
	

	@Override
	public AllCartDto updateCart(CartItemDto cartItemDto) {
		if (cartItemDto.getGiftWrap() != cartItemDto.getChangeGiftWrap()) {
			CartItem cartItem = cartItemMapper.toEntity(cartItemDto);
			cartItem.setGiftWrap(cartItemDto.getChangeGiftWrap());
			cartRepository.delete(cartItem);
			cartRepository.save(cartItemMapper.toEntity(cartItemDto));
		} else {
			cartRepository.update(cartItemMapper.toEntity(cartItemDto));
		}
		
		return this.getCartDto();
	}
	
	

	@Override
	public List<CartItem> getCartItems() {
		return cartRepository.getCartItems();
	}

	@Override
	public AllCartDto getCartDto() {
		List<CartItem> cartItems = cartRepository.getCartItems();
		
		List<CartItemDto> cartItemDtos = cartItems.stream()
				.map(cartItemMapper::toDto)
				.collect(Collectors.toList());
		
		Map<UUID, Product> products = getCartProducts(cartItems);
		
		BigDecimal subtotal = BigDecimal.ZERO;
		for(CartItemDto item : cartItemDtos) {
			Product product = products.get(UUID.fromString(item.getId()));
			BigDecimal totalCost = product.getCost().multiply(BigDecimal.valueOf(item.getQty()));
			item.setProductTitle(product.getTitle());
			item.setCost(product.getCost().toString());
			item.setTotalCost(totalCost.toString());
			subtotal = subtotal.add(totalCost);
		}
		
		return new AllCartDto(cartItemDtos, subtotal.toString());
	}
	
	@Override
	public Map<UUID, Product> getCartProducts(List<CartItem> cartItems) {
		return productService.findProductByIdIn(cartItems.stream()
				.map(CartItem::getId)
				.collect(Collectors.toSet()))
					.stream()
					.collect(Collectors.toMap(Product::getId, Function.identity()));
	}

	@Override
	public AllCartDto clear() {
		cartRepository.deleteAll();
		return this.getCartDto();
	}

	@Override
	public AllCartDto deleteItem(CartItemDto cartItemDto) {
		cartRepository.delete(cartItemMapper.toEntity(cartItemDto));
		return this.getCartDto();
	}

	@Override
	public boolean isEmpty() {
		return cartRepository.isEmpty();
	}
	
}
