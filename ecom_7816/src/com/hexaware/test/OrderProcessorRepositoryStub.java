package com.hexaware.test;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.hexaware.entity.Product;
import com.hexaware.entity.User;
import com.hexaware.repo.OrderProcessorRepository;


public class OrderProcessorRepositoryStub implements OrderProcessorRepository  {
	
	private boolean createProductCalled;
    private boolean addToCartCalled;
    private boolean placeOrderCalled;
    private boolean throwException;
	private OrderProcessorRepositoryStub orderProcessorRepository;
	private Scanner scanner;
    
	public OrderProcessorRepositoryStub(OrderProcessorRepositoryStub stubRepository, Scanner stubScanner) {
	    this.orderProcessorRepository = stubRepository;
	    this.scanner = stubScanner;
	}

   


	@Override
    public boolean createProduct(Product product) {
        createProductCalled = true;
        if (throwException) {
            throw new RuntimeException("Product not found");
        }
        return true;
    }

    @Override
    public boolean addToCart(int customerId, int productId, int quantity) {
        addToCartCalled = true;
        return true;
    }

    @Override
    public boolean placeOrder(int customerId, List<Map<Product, Integer>> cartItems, double totalPrice, String shippingAddress) {
        placeOrderCalled = true;
        return true;
    }

    // Add more methods if needed

    public boolean isCreateProductCalled() {
        return createProductCalled;
    }

    public boolean isAddToCartCalled() {
        return addToCartCalled;
    }

    public boolean isPlaceOrderCalled() {
        return placeOrderCalled;
    }

    public void setThrowException(boolean throwException) {
        this.throwException = throwException;
    }

	@Override
	public boolean createCustomer(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteProduct(int productId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteCustomer(int customerId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addToCart(User user, Product product, int quantity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeFromCart(User user, Product product) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Map<Product, Integer>> getAllFromCart(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean placeOrder(User user, List<Map<Product, Integer>> cartContents, String shippingAddress) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Map<Product, Integer>> getOrdersByCustomer(int customerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<Product, Integer>> getOrdersByOrderId(int orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<Product, Integer>> getAllFromCart(int customerId) {
		// TODO Auto-generated method stub
		return null;
	}
}


