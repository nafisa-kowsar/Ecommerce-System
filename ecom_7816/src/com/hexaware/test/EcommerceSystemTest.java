package com.hexaware.test;
import com.hexaware.app.EcomApp;
import com.hexaware.entity.*;
import com.hexaware.myexceptions.CustomerNotFoundException;
import com.hexaware.repo.*;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EcommerceSystemTest {
	
	
	OrderProcessorRepository order = new OrderProcessorRepositoryImpl();
	
	@Test
	public void testCreateCustomer() {
		User user = new User();
		user.setCustomerId(125);
		user.setName("Test");
		user.setEmail("test123@gmail.com");
		user.setPassword("test");
		boolean isCustomerCreated = order.createCustomer(user);
		assertEquals(true, isCustomerCreated);
		
	}
	
	@Test
	public void testCreateProduct(){
		Product product = new Product();
		product.setName("test_product_1");
		product.setPrice(20.0);
		product.setDescription("Test description");
		boolean isProductCreated = order.createProduct(product);
		assertEquals(true, isProductCreated);
				
	}
	
	@Test
	public void testPlaceOrder() {
		User user = new User();
		user.setCustomerId(129);
		user.setName("Test");
		user.setEmail("test123@gmail.com");
		user.setPassword("test");
		if(order.createCustomer(user)) {
			int customerId = user.getCustomerId();
			Product product = new Product();
			product.setName("test_product_2");
			product.setPrice(25.0);
			product.setDescription("Test description");
		
			if(order.createProduct(product)) {
				int productId = product.getProductId();
				int quantity = 10;

				if(order.addToCart(customerId, productId, quantity)) {
					List<Map<Product, Integer>> cartItems = order.getAllFromCart(customerId);
	    
					if (cartItems.isEmpty()) {
						System.out.println("Cart is empty. Please add products to the cart before placing an order.");
						return;
					}
					double totalPrice = ((EcomApp) order).calculateTotalPrice(cartItems);
					String shippingAddress = "Test Address";
					boolean isOrderPlaced = order.placeOrder(customerId, cartItems, totalPrice, shippingAddress);
					assertEquals(true, isOrderPlaced);
				}
			}
		}
		
	}
	
	@Test(expected = RuntimeException.class)
    public void testExceptionThrown_CustomerNotFound() {
    	boolean flag = false;
    	Scanner scanner = null;
		try {
            EcomApp.registerCustomer(order, scanner);
        } catch(CustomerNotFoundException e){
        	flag = true;
        	e.printStackTrace();
        	
        }finally {
            // Clean up resources
            scanner.close();
        }
		assertEquals(true,flag);
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionThrown_ProductNotFound() {
    	boolean flag = false;
    	Scanner scanner = null;
    	try {
            // Call the static method to test and expect an exception
            EcomApp.createProduct(order, scanner);
        }catch(CustomerNotFoundException e){
        	flag = true;
        	e.printStackTrace();
        }finally {
            // Clean up resources
            scanner.close();
        }
        assertEquals(true,flag);
    }

    

}