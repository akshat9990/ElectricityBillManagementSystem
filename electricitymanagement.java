import java.sql.*;
import java.util.*;
import java.sql.Date;
public class electricitymanagement {
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/electricity";
    static final String JDBC_USER = "root";
    static final String JDBC_PASSWORD = "1234";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            
            // Create tables if not exist (customers and bills)
            createTables(connection);
            
            Scanner scanner = new Scanner(System.in);
            int choice;
            do {
                displayMenu();
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addNewCustomer(connection, scanner);
                        break;
                    case 2:
                        modifyCustomer(connection, scanner);
                        break;
                    case 3:
                        deleteCustomer(connection, scanner);
                        break;
                    case 4:
                        showAllCustomers(connection);
                        break;
                    case 5:
                        generateNewBill(connection, scanner);
                        break;
                    case 6:
                        modifyBill(connection, scanner);
                        break;
                    case 7:
                        deleteBill(connection, scanner);
                        break;
                    case 8:
                        showAllBills(connection);
                        break;
                    case 9:
                        showParticularBill(connection, scanner);
                        break;
                    case 0:
                        System.out.println("Exiting the program.");
                        break;
                    default:
                        System.out.println("Invalid choice. Please choose a valid option.");
                }
            } while (choice != 0);

            scanner.close();

            // Implement your menu system here
            // You can use switch or if-else to handle different options
            
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTables(Connection connection) throws SQLException {
        // Create customers table
        String createCustomersTable = "CREATE TABLE IF NOT EXISTS customers (" +
                                     "id INT AUTO_INCREMENT PRIMARY KEY," +
                                     "name VARCHAR(100)," +
                                     "address VARCHAR(100),"+
                                     "phone VARCHAR(20)" +
                                     ")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(createCustomersTable);

        // Create bills table
        String createBillsTable = "CREATE TABLE IF NOT EXISTS bills (" +
                                  "id INT AUTO_INCREMENT PRIMARY KEY," +
                                  "customer_id INT," +
                                  "amount DECIMAL(10, 2)," +
                                  "date DATE" +
                                  ")";
        statement.executeUpdate(createBillsTable);

        statement.close();
    }
    
    public static void displayMenu() {
        System.out.println("Electricity Management System Menu");
        System.out.println("1. Add New Customer");
        System.out.println("2. Modify Customer");
        System.out.println("3. Delete Customer");
        System.out.println("4. Show All Customers");
        System.out.println("5. Generate New Bill");
        System.out.println("6. Modify Bill");
        System.out.println("7. Delete Bill");
        System.out.println("8. Show All Bills");
        System.out.println("9. Show Particular Bill");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }
    
    // Implement other methods for various functionalities (new customer, modify customer, etc.)

    public static void addNewCustomer(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Adding a New Customer");
        
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();

        System.out.print("Enter customer address: ");
        String address = scanner.nextLine();

        System.out.print("Enter customer phone: ");
        String phone = scanner.nextLine();

        String insertCustomerQuery = "INSERT INTO customers (name, address, phone) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertCustomerQuery);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, address);
        preparedStatement.setString(3, phone);
        
        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Customer added successfully!");
        } else {
            System.out.println("Failed to add customer.");
        }
        
        preparedStatement.close();
    }
    
    public static void modifyCustomer(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Modifying Customer");
        
        System.out.print("Enter customer ID to modify: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new customer name: ");
        String newName = scanner.nextLine();

        System.out.print("Enter new customer address: ");
        String newAddress = scanner.nextLine();

        System.out.print("Enter new customer phone: ");
        String newPhone = scanner.nextLine();

        String updateCustomerQuery = "UPDATE customers SET name=?, address=?, phone=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateCustomerQuery);
        preparedStatement.setString(1, newName);
        preparedStatement.setString(2, newAddress);
        preparedStatement.setString(3, newPhone);
        preparedStatement.setInt(4, customerId);
        
        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Customer updated successfully!");
        } else {
            System.out.println("Failed to update customer.");
        }
        
        preparedStatement.close();
    }

    public static void deleteCustomer(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Deleting Customer");
        
        System.out.print("Enter customer ID to delete: ");
        int customerId = scanner.nextInt();

        String deleteCustomerQuery = "DELETE FROM customers WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteCustomerQuery);
        preparedStatement.setInt(1, customerId);
        
        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Customer deleted successfully!");
        } else {
            System.out.println("Failed to delete customer.");
        }
        
        preparedStatement.close();
    }

    public static void showAllCustomers(Connection connection) throws SQLException {
        System.out.println("List of All Customers");

        String selectAllCustomersQuery = "SELECT * FROM customers";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectAllCustomersQuery);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String address = resultSet.getString("address");
            String phone = resultSet.getString("phone");
            System.out.println("Customer ID: " + id + ", Name: " + name + ", Address: " + address + ", Phone: " + phone);
        }

        resultSet.close();
        statement.close();
    }
    
    public static void generateNewBill(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Generating New Bill");

        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter bill amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter bill date (YYYY-MM-DD): ");
        String dateString = scanner.nextLine();
        Date date = Date.valueOf(dateString);

        String insertBillQuery = "INSERT INTO bills (customer_id, amount, date) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertBillQuery);
        preparedStatement.setInt(1, customerId);
        preparedStatement.setDouble(2, amount);
        preparedStatement.setDate(3, date);

        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Bill generated successfully!");
        } else {
            System.out.println("Failed to generate bill.");
        }

        preparedStatement.close();
    }

    public static void modifyBill(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Modifying Bill");

        System.out.print("Enter bill ID to modify: ");
        int billId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter new bill amount: ");
        double newAmount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        String updateBillQuery = "UPDATE bills SET amount=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateBillQuery);
        preparedStatement.setDouble(1, newAmount);
        preparedStatement.setInt(2, billId);

        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Bill updated successfully!");
        } else {
            System.out.println("Failed to update bill.");
        }

        preparedStatement.close();
    }

    public static void deleteBill(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Deleting Bill");

        System.out.print("Enter bill ID to delete: ");
        int billId = scanner.nextInt();

        String deleteBillQuery = "DELETE FROM bills WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteBillQuery);
        preparedStatement.setInt(1, billId);

        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Bill deleted successfully!");
        } else {
            System.out.println("Failed to delete bill.");
        }

        preparedStatement.close();
    }

    public static void showAllBills(Connection connection) throws SQLException {
        System.out.println("List of All Bills");

        String selectAllBillsQuery = "SELECT * FROM bills";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectAllBillsQuery);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            int customerId = resultSet.getInt("customer_id");
            double amount = resultSet.getDouble("amount");
            Date date = resultSet.getDate("date");
            System.out.println("Bill ID: " + id + ", Customer ID: " + customerId + ", Amount: " + amount + ", Date: " + date);
        }

        resultSet.close();
        statement.close();
    }

    public static void showParticularBill(Connection connection, Scanner scanner) throws SQLException {
        System.out.println("Showing Particular Bill");

        System.out.print("Enter bill ID: ");
        int billId = scanner.nextInt();

        String selectBillQuery = "SELECT * FROM bills WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectBillQuery);
        preparedStatement.setInt(1, billId);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            int customerId = resultSet.getInt("customer_id");
            double amount = resultSet.getDouble("amount");
            Date date = resultSet.getDate("date");
            System.out.println("Bill ID: " + billId + ", Customer ID: " + customerId + ", Amount: " + amount + ", Date: " + date);
        } else {
            System.out.println("Bill not found.");
        }

        resultSet.close();
        preparedStatement.close();
    }    

}
