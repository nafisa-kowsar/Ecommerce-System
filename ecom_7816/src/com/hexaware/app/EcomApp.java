package com.hexaware.app;

import com.hexaware.repo.OrderProcessorRepository;
import com.hexaware.repo.OrderProcessorRepositoryImpl;
import com.hexaware.myexceptions.CustomerNotFoundException;
import com.hexaware.myexceptions.ProductNotFoundException;
import com.hexaware.myexceptions.OrderNotFoundException;
import com.hexaware.entity.User;
import com.hexaware.entity.Product;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class EcomApp {


	private OrderProcessorRepository orderProcessorRepository;

	public EcomApp(OrderProcessorRepository orderProcessorRepository) {
	    this.orderProcessorRepository = orderProcessorRepository;
	}
	
    
	
    
    public static void main(String[] args) {
		// TODO Auto-generated method stub
		OrderProcessorRepository o = new OrderProcessorRepositoryImpl();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Register Customer");
            System.out.println("2. Create Product");
            System.out.println("3. Delete Product");
            System.out.println("4. Add to Cart");
            System.out.println("5. View Cart");
            System.out.println("6. Place Order");
            System.out.println("7. View Customer Order");
            System.out.println("8. Exit");
            
        try {
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    registerCustomer(o, scanner);
                    break;

                case 2:
                    createProduct(o, scanner);
                    break;

                case 3:
                    deleteProduct(o, scanner);
                    break;

                case 4:
                    addToCart(o, scanner);
                    break;

                case 5:
                    viewCart(o, scanner);
                    break;

                case 6:
                    placeOrder(o, scanner);
                    break;

                case 7:
                    viewCustomerOrder(o, scanner);
                    break;

                case 8:
                    System.out.println("Exiting the application. Goodbye!");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }catch(java.util.InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid integer.");
            scanner.nextLine(); 
        }    
        }
    }
	public static void registerCustomer(OrderProcessorRepository orderProcessorRepository, Scanner scanner) {
        System.out.println("Registering Customer...");

        
      try {  
        System.out.print("Enter Customer Name: ");
        String name = scanner.next();

        System.out.print("Enter Customer Email: ");
        String email = scanner.next();

        System.out.print("Enter Customer Password: ");
        String password = scanner.next();

       
        User customer = new User();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPassword(password);

        
        boolean isRegistered = orderProcessorRepository.createCustomer(customer);

        if (isRegistered) {
            System.out.println("Customer registered successfully!");
        } else {
            System.out.println("Failed to register customer. Please try again.");
        }
      }catch(CustomerNotFoundException e) {
          System.out.println("Error: " + e.getMessage());
      }
    }
	
	public static void createProduct(OrderProcessorRepository orderProcessorRepository, Scanner scanner) {
	    System.out.println("Creating Product...");

	    try {
	      System.out.print("Enter Product Name: ");
	      String productName = scanner.next();

	      System.out.print("Enter Product Price: ");
	      double productPrice = scanner.nextDouble();
	      
	      System.out.print("Enter Product Description: ");
	      String productDescription = scanner.next();

	   
	      Product product = new Product();
	      product.setName(productName);
	      product.setPrice(productPrice);
	      product.setDescription(productDescription);

	    
	      boolean isCreated = orderProcessorRepository.createProduct(product);

	      if (isCreated) {
	          System.out.println("Product created successfully!");
	      } else {
	          System.out.println("Failed to create product. Please try again.");
	      }
	    }catch(ProductNotFoundException e) {
	          System.out.println("Error: " + e.getMessage());
	    }
	}
	private static void deleteProduct(OrderProcessorRepository orderProcessorRepository, Scanner scanner) {
	    System.out.println("Deleting Product...");

	    
	    System.out.print("Enter Product ID to delete: ");
	    int productId = scanner.nextInt();



	    boolean isDeleted = orderProcessorRepository.deleteProduct(productId);

	    if (isDeleted) {
	        System.out.println("Product deleted successfully!");
	    } else {
	        System.out.println("Failed to delete product. Please check the Product ID and try again.");
	    }
	}
	public static void addToCart(OrderProcessorRepository orderProcessorRepository, Scanner scanner) {
	    System.out.println("Adding to Cart...");

	    
	    System.out.print("Enter Customer ID: ");
	    int customerId = scanner.nextInt();

	    System.out.print("Enter Product ID: ");
	    int productId = scanner.nextInt();

	    System.out.print("Enter Quantity: ");
	    int quantity = scanner.nextInt();

	   
	    boolean isAddedToCart = orderProcessorRepository.addToCart(customerId, productId, quantity);

	    if (isAddedToCart) {
	        System.out.println("Product added to the cart successfully!");
	    } else {
	        System.out.println("Failed to add product to the cart. Please check the details and try again.");
	    }
	}
	private static void viewCart(OrderProcessorRepository orderProcessorRepository, Scanner scanner) {
	    System.out.println("Viewing Cart...");

	   
	    System.out.print("Enter Customer ID: ");
	    int customerId = scanner.nextInt();

	    
	    List<Map<Product, Integer>> cartItems = orderProcessorRepository.getAllFromCart(customerId);

	    if (cartItems.isEmpty()) {
	        System.out.println("Cart is empty.");
	    } else {
	        System.out.println("Products in the Cart:");

	        for (Map<Product, Integer> cartItem : cartItems) {
	            for (Map.Entry<Product, Integer> entry : cartItem.entrySet()) {
	                Product product = entry.getKey();
	                int quantity = entry.getValue();

	                System.out.println("Product ID: " + product.getProductId());
	                System.out.println("Product Name: " + product.getName());
	                System.out.println("Quantity: " + quantity);
	                System.out.println("------------------------------");
	            }
	        }
	    }
	}
	public static void placeOrder(OrderProcessorRepository orderProcessorRepository, Scanner scanner) {
	    System.out.println("Placing Order...");

	    
	  try {  
	    System.out.print("Enter Customer ID: ");
	    int customerId = scanner.nextInt();

	    
	    List<Map<Product, Integer>> cartItems = orderProcessorRepository.getAllFromCart(customerId);
	    if (cartItems.isEmpty()) {
	        System.out.println("Cart is empty. Please add products to the cart before placing an order.");
	        return;
	    }

	    
	    double totalPrice = calculateTotalPrice(cartItems);

	    System.out.print("Enter Shipping Address: ");
	    scanner.nextLine(); 
	    String shippingAddress = scanner.nextLine();

	   
	    boolean isOrderPlaced = orderProcessorRepository.placeOrder(customerId, cartItems, totalPrice, shippingAddress);

	    if (isOrderPlaced) {
	        System.out.println("Order placed successfully!");
	    } else {
	        System.out.println("Failed to place order. Please try again.");
	    }
	  }catch(CustomerNotFoundException | ProductNotFoundException | OrderNotFoundException e) {
          System.out.println("Error: " + e.getMessage());
	  }
	}
    
	public static double calculateTotalPrice(List<Map<Product, Integer>> cartItems) {
	    double totalPrice = 0;

	    for (Map<Product, Integer> cartItem : cartItems) {
	        for (Map.Entry<Product, Integer> entry : cartItem.entrySet()) {
	            Product product = entry.getKey();
	            int quantity = entry.getValue();
	            totalPrice += product.getPrice() * quantity;
	        }
	    }

	    return totalPrice;
	}
	
	private static void viewCustomerOrder(OrderProcessorRepository orderProcessorRepository, Scanner scanner) {
	    System.out.println("Viewing Customer Orders...");

	    
	    System.out.print("Enter Customer ID: ");
	    int customerId = scanner.nextInt();

	    
	    List<Map<Product, Integer>> customerOrders = orderProcessorRepository.getOrdersByCustomer(customerId);

	    if (customerOrders.isEmpty()) {
	        System.out.println("No orders found for the customer.");
	    } else {
	        System.out.println("Customer Orders:");

	        for (Map<Product, Integer> order : customerOrders) {
	            for (Map.Entry<Product, Integer> entry : order.entrySet()) {
	                Product product = entry.getKey();
	                int quantity = entry.getValue();

	                System.out.println("Product ID: " + product.getProductId());
	                System.out.println("Product Name: " + product.getName());
	                System.out.println("Product Price: " + product.getPrice());
	                System.out.println("Quantity: " + quantity);
	                System.out.println("------------------------------");
	            }
	        }
	    }
	}




	}


