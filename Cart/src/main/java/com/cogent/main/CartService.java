package com.cogent.main;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService
{
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CartItemRepository cartItemRepository;
	@Autowired
	private ProductClient productClient;

	public List<Product> getCart(int userId)
	{
		DetailedCart de = DetailedCart.builder()
				.userId(userId)
				.products(cartRepository.findById(userId)
						.get()
						.getCartItems()
						.stream()
						.map(d -> Product.builder()
								.id(productClient.oneProduct(d.getProductId())
										.getId())
								.name(productClient.oneProduct(d.getProductId())
										.getName())
								.price(productClient.oneProduct(d.getProductId())
										.getPrice())
								.description(productClient.oneProduct(d.getProductId())
										.getDescription())
								.category(productClient.oneProduct(d.getProductId())
										.getCategory())
								.quantity(d.getQuantity())
								.imageUrl(productClient.oneProduct(d.getProductId())
										.getImageUrl())
								.build())
						.collect(Collectors.toList()))
				.build();
		return de.getProducts();
	}

	public CartEntity addItem(int userId, int num)
	{
		// Find Data for this user
		CartEntity cartEntity = cartRepository.findById(userId)
				.get();
		// Adds 1 to quantity if item already exists
		if (cartItemRepository.findByUserIdAndProductId(userId, num)
				.isPresent())
		{
			CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, num)
					.get();
			cartItem.setQuantity(cartItem.getQuantity() + 1);
			cartItemRepository.save(cartItem);

		} // Adds item to list if new
		else
		{
			CartItem cartItem = CartItem.builder()
					.productId(num)
					.quantity(1)
					.cartEntity(cartEntity)
					.build();
			cartItemRepository.save(cartItem);
		}
		return cartRepository.findById(userId)
				.get();
	}

	public CartEntity removeItem(int userId, int productId)
	{
		// Subtracts 1 from quantity if item already exists
		if (cartItemRepository.findByUserIdAndProductId(userId, productId)
				.isPresent())
		{
			CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId)
					.get();
			if (cartItem.getQuantity() > 1)
			{
				cartItem.setQuantity(cartItem.getQuantity() - 1);
				cartItemRepository.save(cartItem);
			} else
			{
				cartItemRepository.delete(cartItem);
			}

		}
		return cartRepository.findById(userId)
				.get();
	}

	public CartEntity updateCart(int userId, List<CartItem> cartItems)
	{
		CartEntity cartEntity = cartRepository.findById(userId)
				.get();

		cartItemRepository.deleteAll(cartItemRepository.findByCartEntity(cartEntity)
				.get());

		cartItems.stream()
				.peek(c -> c.setCartEntity(cartEntity))
				.forEach(c -> cartItemRepository.save(c));

		CartEntity updatedCartEntity = cartRepository.findById(userId)
				.get();
		return updatedCartEntity;
	}

	public void init(int userId)
	{
		cartRepository.save(CartEntity.builder()
				.userId(userId)
				.cartItems(new ArrayList<CartItem>())
				.build());
	}

	public void delete(int userId)
	{
		cartRepository.deleteById(userId);
	}

	public void clear(int userId)
	{
		CartEntity cartEntity = cartRepository.findById(userId)
				.get();
		cartItemRepository.deleteAll(cartItemRepository.findByCartEntity(cartEntity)
				.get());
	}

}
