package com.hexaware.repo;

import com.hexaware.entity.User;
import com.hexaware.entity.Product;

import java.util.List;
import java.util.Map;

public interface OrderProcessorRepository {
    boolean createProduct(Product product);
    boolean createCustomer(User user);
    boolean deleteProduct(int productId);
    boolean deleteCustomer(int customerId);
    boolean addToCart(User user, Product product, int quantity);
    boolean removeFromCart(User user, Product product);
    List<Map<Product, Integer>> getAllFromCart(User user);
    /*boolean placeOrder(User user, List<Map<Product, Integer>> cartContents, String shippingAddress);*/
    List<Map<Product, Integer>> getOrdersByCustomer(int customerId);
    List<Map<Product, Integer>> getOrdersByOrderId(int orderId);
	boolean addToCart(int customerId, int productId, int quantity);
	List<Map<Product, Integer>> getAllFromCart(int customerId);
	boolean placeOrder(int customerId, List<Map<Product, Integer>> cartItems, double totalPrice,
			String shippingAddress);
}


