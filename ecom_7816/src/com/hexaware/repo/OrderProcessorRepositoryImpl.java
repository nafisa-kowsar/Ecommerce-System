package com.hexaware.repo;

import com.hexaware.entity.User;
import com.hexaware.entity.Product;
import com.hexaware.util.DBConnection;

import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;



public class OrderProcessorRepositoryImpl implements OrderProcessorRepository {
	 
	private static Connection connection = DBConnection.getConnection();
	

	@Override
    public boolean createProduct(Product product) {
    	String sql = "INSERT INTO products (product_id, name, price,description) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
        	ps.setInt(1, product.getProductId());
            ps.setString(2, product.getName());
            ps.setDouble(3, product.getPrice());
            ps.setString(4, product.getDescription());
            

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    @Override
    public boolean createCustomer(User user) {
    	String sql = "INSERT INTO customers (name, email, password) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
        	
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    @Override
    public boolean deleteProduct(int productId) {
    	String sql = "DELETE FROM products WHERE product_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, productId);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    @Override
    public boolean deleteCustomer(int customerId) {
    	String sql = "DELETE FROM customers WHERE customer_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, customerId);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    @Override
    public boolean addToCart(User user, Product product, int quantity) {
    	String sql = "INSERT INTO cart (customer_id, product_id, quantity) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
        	ps.setInt(1, user.getCustomerId());
            ps.setInt(2, product.getProductId());
            ps.setInt(3, quantity);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean addToCart(int customerId, int productId, int quantity) {
        String sql = "INSERT INTO cart (customer_id, product_id, quantity) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception, log error, or throw a custom exception as needed
            return false;
        }
    }
    

    @Override
    public boolean removeFromCart(User user, Product product) {
    	String sql = "DELETE FROM cart WHERE customer_id = ? AND product_id = ?";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, user.getCustomerId());
                ps.setInt(2, product.getProductId());

                int rowsAffected = ps.executeUpdate();

                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
      
    

    @Override
    public List<Map<Product, Integer>> getAllFromCart(User user) {
    	List<Map<Product, Integer>> cartContents = new ArrayList<>();

        String sql = "SELECT product_id, quantity FROM cart WHERE customer_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, user.getCustomerId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("product_id");
                    int quantity = rs.getInt("quantity");

                    // Assuming you have a method to retrieve product details by ID
                    Product product = getProductDetailsById(productId);

                    Map<Product, Integer> cartItem = new HashMap<>();
                    cartItem.put(product, quantity);

                    cartContents.add(cartItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cartContents;
    }
    
    @Override
    public List<Map<Product, Integer>> getAllFromCart(int customerId) {
        List<Map<Product, Integer>> cartItems = new ArrayList<>();

        String sql = "SELECT p.product_id, p.name, p.price, c.quantity " +
                     "FROM products p " +
                     "JOIN cart c ON p.product_id = c.product_id " +
                     "WHERE c.customer_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("product_id");
                    String productName = rs.getString("name");
                    double productPrice = rs.getDouble("price");
                    int quantity = rs.getInt("quantity");

                    
                    Product product = new Product();
                    product.setProductId(productId);
                    product.setName(productName);
                    product.setPrice(productPrice);

                    
                    Map<Product, Integer> cartItem = new HashMap<>();
                    cartItem.put(product, quantity);

                    cartItems.add(cartItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
        }

        return cartItems;
    }
    
    public Product getProductDetailsById(int productId) {
    	String sql = "SELECT * FROM products WHERE product_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, productId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int fetchedProductId = resultSet.getInt("product_id");
                    String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");
                    String description = resultSet.getString("description");

                   
                    return new Product(fetchedProductId, name, price, description);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; 
    }

    
    /*public boolean placeOrder(User user, List<Map<Product, Integer>> cartContents, String shippingAddress) {
        try {
             connection.setAutoCommit(false);

             
             int orderId = insertOrder(user, cartContents, shippingAddress);

            
             insertOrderItems(orderId, cartContents);

             
             connection.commit();

            
             connection.setAutoCommit(true);

             return true;
         } catch (SQLException e) {
             
             try {
                 connection.rollback();
             } catch (SQLException rollbackException) {
                 rollbackException.printStackTrace();
             }

             e.printStackTrace();
             return false;
         }
     }*/
    @Override
    public boolean placeOrder(int customerId, List<Map<Product, Integer>> cartItems, double totalPrice, String shippingAddress) {
    	System.out.println("Placing Order in the database...");
        System.out.println("Customer ID: " + customerId);
        System.out.println("Total Price: " + totalPrice);
        System.out.println("Shipping Address: " + shippingAddress);
        System.out.println("Ordered Products:");

        for (Map<Product, Integer> cartItem : cartItems) {
            for (Map.Entry<Product, Integer> entry : cartItem.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                System.out.println("Product ID: " + product.getProductId());
                System.out.println("Quantity: " + quantity);
            }
        }
    	
    	try {
            connection.setAutoCommit(false);

            User user = new User();
            user.setCustomerId(customerId);
            int orderId = insertOrder(user, cartItems, shippingAddress);
            insertOrderItems(orderId, cartItems);
            
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            try {
                
                connection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }

            e.printStackTrace();
            return false;
        }
    	
            
    }
    

    private int insertOrder(User user,List<Map<Product, Integer>> cartContents, String shippingAddress) throws SQLException {
        String sql = "INSERT INTO orders (customer_id, order_date, total_price, shipping_address) VALUES (?, CURDATE(), ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, user.getCustomerId());
            
            double totalPrice = calculateTotalPrice(user, cartContents);
            ps.setDouble(2, totalPrice);
            ps.setString(3, shippingAddress);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            
            throw new SQLException("Failed to insert into orders table.");
        }
    }
    private void insertOrderItems(int orderId, List<Map<Product, Integer>> cartContents) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity) VALUES (?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Map<Product, Integer> cartItem : cartContents) {
                Product product = cartItem.keySet().iterator().next();
                int quantity = cartItem.get(product);

                ps.setInt(1, orderId);
                ps.setInt(2, product.getProductId());
                ps.setInt(3, quantity);

                ps.addBatch();
            }

           
            ps.executeBatch();
        }
    }
    
    private double calculateTotalPrice(User user, List<Map<Product, Integer>> cartContents) {
        double totalPrice = 0.0;

        for (Map<Product, Integer> cartItem : cartContents) {
            Product product = cartItem.keySet().iterator().next();
            int quantity = cartItem.get(product);

            
            double productPrice = product.getPrice();

            
            totalPrice += productPrice * quantity;
        }
        return totalPrice;
    }



 
    @Override
    public List<Map<Product, Integer>> getOrdersByCustomer(int customerId) {
    	List<Map<Product, Integer>> orders = new ArrayList<>();

        String sql = "SELECT oi.product_id, oi.quantity " +
                     "FROM orders o " +
                     "JOIN order_items oi ON o.order_id = oi.order_id " +
                     "WHERE o.customer_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, customerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int productId = resultSet.getInt("product_id");
                    int quantity = resultSet.getInt("quantity");

                   
                    Product product = getProductDetailsById(productId);

                    Map<Product, Integer> orderItem = new HashMap<>();
                    orderItem.put(product, quantity);

                    orders.add(orderItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }
    


    @Override
    public List<Map<Product, Integer>> getOrdersByOrderId(int orderId) {
    	 List<Map<Product, Integer>> orderDetails = new ArrayList<>();

         String sql = "SELECT oi.product_id, oi.quantity " +
                      "FROM order_items oi " +
                      "WHERE oi.order_id = ?";

         try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
             preparedStatement.setInt(1, orderId);

             try (ResultSet resultSet = preparedStatement.executeQuery()) {
                 while (resultSet.next()) {
                     int productId = resultSet.getInt("product_id");
                     int quantity = resultSet.getInt("quantity");

                     
                     Product product = getProductDetailsById(productId);

                     Map<Product, Integer> orderItem = new HashMap<>();
                     orderItem.put(product, quantity);

                     orderDetails.add(orderItem);
                 }
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }

         return orderDetails;
     }


	
	
}

