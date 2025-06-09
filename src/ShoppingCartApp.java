import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class ShoppingCartApp {
    private static Scanner sc = new Scanner(System.in);
    private static List<User> users = new ArrayList<>();
    private static List<Product> products = Arrays.asList(
            new Product(1, "Laptop", 45000, "Electronics"),
            new Product(2, "Smartphone", 25000, "Electronics"),
            new Product(3, "Gaming PC", 85000, "Electronics"),
            new Product(4, "Java Programming Book", 700, "Books"),
            new Product(5, "Perfume - Ocean Breeze", 1800, "Perfumes"),
            new Product(6, "Perfume - Rose Elegance", 2200, "Perfumes"),
            new Product(7, "Fiction Novel", 500, "Books"),
            new Product(8, "T-Shirt", 400, "Clothing"),
            new Product(9, "Jeans", 1200, "Clothing")
    );
    private static Cart cart = new Cart();
    private static User currentUser = null;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n1. Sign Up\n2. Login\n3. Exit");
            String choice = sc.nextLine();
            switch (choice) {
                case "1" -> signUp();
                case "2" -> {
                    if (login()) {
                        shoppingMenu();
                    }
                }
                case "3" -> {
                    System.out.println("Thank you for using Shopping Cart App.");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void signUp() {
        System.out.print("Enter username: ");
        String uname = sc.nextLine().trim();
        System.out.print("Enter password: ");
        String pwd = sc.nextLine().trim();

        if (uname.isEmpty() || pwd.isEmpty()) {
            System.out.println("Username or password cannot be empty.");
            return;
        }

        for (User u : users) {
            if (u.getUsername().equals(uname)) {
                System.out.println("Username already exists.");
                return;
            }
        }

        users.add(new User(uname, pwd));
        System.out.println("Sign-up successful.");
    }

    private static boolean login() {
        System.out.print("Enter username: ");
        String uname = sc.nextLine().trim();
        System.out.print("Enter password: ");
        String pwd = sc.nextLine().trim();

        for (User u : users) {
            if (u.getUsername().equals(uname) && u.getPassword().equals(pwd)) {
                System.out.println("Login successful.");
                currentUser = u;
                return true;
            }
        }
        System.out.println("Login failed. Invalid credentials.");
        return false;
    }

    private static void shoppingMenu() {
        while (true) {
            System.out.println("""
                    \n1. View Products
                    2. Add to Cart
                    3. Remove from Cart
                    4. View Cart
                    5. Checkout
                    6. Logout""");
            String choice = sc.nextLine();
            switch (choice) {
                case "1" -> products.forEach(System.out::println);
                case "2" -> addProductToCart();
                case "3" -> removeProductFromCart();
                case "4" -> cart.showCart();
                case "5" -> checkout();
                case "6" -> {
                    currentUser = null;
                    cart.clear();
                    System.out.println("Logged out successfully.");
                    return;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void addProductToCart() {
        System.out.print("Enter product ID to add: ");
        try {
            int id = Integer.parseInt(sc.nextLine());
            Optional<Product> productOpt = products.stream()
                    .filter(p -> p.getId() == id)
                    .findFirst();
            if (productOpt.isPresent()) {
                cart.addProduct(productOpt.get());
                System.out.println("Product added to cart.");
            } else {
                System.out.println("Invalid product ID.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid number.");
        }
    }

    private static void removeProductFromCart() {
        System.out.print("Enter product ID to remove: ");
        try {
            int id = Integer.parseInt(sc.nextLine());
            cart.removeProduct(id);
        } catch (NumberFormatException e) {
            System.out.println("Enter a valid number.");
        }
    }

    private static void checkout() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty. Add products before checkout.");
            return;
        }
        System.out.print("Enter shipping address: ");
        String address = sc.nextLine().trim();
        if (address.isEmpty()) {
            System.out.println("Address cannot be empty.");
            return;
        }

        cart.showCart();
        System.out.println("Order placed successfully.\nShipping to: " + address);
        saveOrder(address);
        cart.clear();
    }

    private static void saveOrder(String address) {
        String orderFile = currentUser.getUsername() + "_orders.txt";

        try (PrintWriter pw = new PrintWriter(new FileWriter(orderFile, true))) {
            pw.println("Order on " + new Date());
            pw.println("Shipping Address: " + address);
            pw.println("Items:");
            for (Product p : cart.getProducts()) {
                pw.println(" - " + p.toString());
            }
            pw.println("Total: Rs." + cart.getTotal());
            pw.println("-------------------------------");
        } catch (IOException e) {
            System.out.println("Error saving order.");
        }
    }
}
