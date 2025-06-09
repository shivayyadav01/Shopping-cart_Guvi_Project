import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product p) {
        products.add(p);
    }

    public void removeProduct(int id) {
        boolean removed = products.removeIf(p -> p.getId() == id);
        if (!removed) {
            System.out.println("Product with ID " + id + " not found in cart.");
        } else {
            System.out.println("Product removed successfully.");
        }
    }

    public double getTotal() {
        return products.stream().mapToDouble(Product::getPrice).sum();
    }

    public void showCart() {
        if (products.isEmpty()) {
            System.out.println("Cart is empty.");
        } else {
            for (Product p : products) {
                System.out.println(p.toString());
            }
            System.out.println("Total: Rs." + getTotal());
        }
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products); // Defensive copy
    }

    public void clear() {
        products.clear();
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }
}
