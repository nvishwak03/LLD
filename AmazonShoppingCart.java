// Product : id, name, price, constructor(), toString()
// CartItem : product, quantity, constructor(), inc(q), set(q), eachItemTotal()
// Cart : items(map<productId, CartItem>), add(product, qty), remove(productId), setQuantity(productId, qty), subtotal(), tax(), total(), print()

import java.util.*;

// Product
class Product {
    int id;
    String name;
    double price;

    public Product(int id, String name, double price) {
        this.id = id; this.name = name; this.price = price;
    }

    @Override
    public String toString() {
        return id + " " + name + " $" + price;
    }
}

// CartItem
class CartItem {
    Product product;
    int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product; this.quantity = quantity;
    }

    public void inc(int q) {
        this.quantity += q;
        if (this.quantity < 1) this.quantity = 1;
    }

    public void set(int q) {
        this.quantity = Math.max(1, q);
    }

    public double eachItemTotal() {
        return product.price * quantity;
    }

    @Override
    public String toString() {
        return product.id + " " + product.name + " x" + quantity + " = $" + eachItemTotal();
    }
}

// Cart
class Cart {
    Map<Integer, CartItem> items = new LinkedHashMap<>();
    static final double TAX_RATE = 0.08;

    public void add(Product p, int qty) {
        if (qty <= 0) qty = 1;
        CartItem existing = items.get(p.id);
        if (existing == null) {
            items.put(p.id, new CartItem(p, qty));
        } else {
            existing.inc(qty);
        }
        System.out.println("Added: " + p.name + " x" + qty);
    }

    public void remove(int productId) {
        CartItem removed = items.remove(productId);
        System.out.println(removed == null ? "Item not in cart" : "Removed: " + removed.product.name);
    }

    public void setQuantity(int productId, int qty) {
        CartItem ci = items.get(productId);
        if (ci == null) {
            System.out.println("Item not in cart");
            return;
        }
        ci.set(qty);
        System.out.println("Set " + ci.product.name + " qty to " + ci.quantity);
    }

    public double subtotal() {
        double s = 0.0;
        for (CartItem ci : items.values()) s += ci.eachItemTotal();
        return s;
    }

    public double tax() {
        return subtotal() * TAX_RATE;
    }

    public double total() {
        return subtotal() + tax();
    }

    public void print() {
        System.out.println("=== CART ===");
        if (items.isEmpty()) {
            System.out.println("(empty)\n");
            return;
        }
        for (CartItem ci : items.values()) System.out.println(ci);
        System.out.println("Subtotal: $" + subtotal());
        System.out.println("Tax (8%): $" + tax());
        System.out.println("TOTAL   : $" + total());
        System.out.println();
    }
}

// Demo
public class AmazonShoppingCart {
    public static void main(String[] args) {
        Product iphone  = new Product(101, "iPhone 15", 999.00);
        Product galaxy  = new Product(102, "Galaxy S23", 899.00);
        Product earbuds = new Product(201, "Wireless Earbuds", 79.99);

        Cart cart = new Cart();
        cart.print();

        cart.add(iphone, 1);
        cart.add(earbuds, 2);
        cart.add(galaxy, 1);
        cart.print();

        cart.setQuantity(201, 3);
        cart.remove(102);
        cart.print();
    }
}
