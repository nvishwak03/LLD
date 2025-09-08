// Topping : name, price, constructor()
// Coupon : code, percentOff, flatOff, minSubtotal, constructor(), discountFor(subtotal)
// Menu : static maps for base prices - small, medium, large, crust charges - thin, handtossed, topping prices, tax rate, static methods() to get prices
// Pizza : size, crust, list of toppings, constructor(), addTopping(), price()
// Bill : subtotal, discount, tax, total, constructor(), print()
// Order : list of pizzas, coupon, addPizza(), applyCoupon(), checkout()

import java.util.*;

// -------- Core Value Objects --------
class Topping {
    String name; double price;
    public Topping(String name, double price) { this.name = name; this.price = price; }
}

class Coupon {
    String code; double percentOff; double minSubtotal;
    public Coupon(String code, double percentOff, double minSubtotal) {
        this.code = code; this.percentOff = percentOff; this.minSubtotal = minSubtotal;
    }
    public double discountFor(double subtotal) {
        if (subtotal < minSubtotal) return 0.0;
        return subtotal * (percentOff / 100.0);
    }
}

// -------- Menu / Price Table --------
class Menu {
    static Map<String, Double> basePrice = new HashMap<>();
    static Map<String, Double> crustUpcharge = new HashMap<>();
    static Map<String, Double> toppingPrice = new HashMap<>();
    static double taxRate = 0.08; // 8%

    static {
        basePrice.put("SMALL", 6.0); basePrice.put("MEDIUM", 8.0); basePrice.put("LARGE", 10.0);
        crustUpcharge.put("THIN", 0.0); crustUpcharge.put("PAN", 1.0); crustUpcharge.put("STUFFED", 2.0);
        toppingPrice.put("PEPPERONI", 1.5); toppingPrice.put("MUSHROOM", 1.0);
        toppingPrice.put("ONION", 0.7); toppingPrice.put("OLIVE", 0.8); toppingPrice.put("CHICKEN", 2.0);
    }

    static double base(String size)      { return basePrice.getOrDefault(size, 0.0); }
    static double crustAdd(String crust) { return crustUpcharge.getOrDefault(crust, 0.0); }
    static double topping(String name)   { return toppingPrice.getOrDefault(name, 0.0); }
}

// -------- Domain Objects --------
class Pizza {
    String size; String crust;
    List<Topping> toppings = new ArrayList<>();

    public Pizza(String size, String crust) { this.size = size; this.crust = crust; }
    public Pizza addTopping(String name) {
        toppings.add(new Topping(name, Menu.topping(name)));
        return this;
    }
    public double price() {
        double sum = Menu.base(size) + Menu.crustAdd(crust);
        for (Topping t : toppings) sum += t.price;
        return sum;
    }
}

class Bill {
    double subtotal, discount, tax, total;
    public Bill(double subtotal, double discount) {
        this.subtotal = round2(subtotal);
        this.discount = round2(discount);
        this.tax = round2((subtotal - discount) * Menu.taxRate);
        this.total = round2(subtotal - discount + tax);
    }
    static double round2(double x) { return Math.round(x * 100.0) / 100.0; }
    public void print() {
        System.out.println("---- BILL ----");
        System.out.println("Subtotal : $" + subtotal);
        System.out.println("Discount : -$" + discount);
        System.out.println("Tax      : $" + tax);
        System.out.println("TOTAL    : $" + total);
    }
}

class Order {
    List<Pizza> pizzas = new ArrayList<>();
    Coupon coupon;

    public Order addPizza(Pizza p) { pizzas.add(p); return this; }
    public Order applyCoupon(Coupon c) { this.coupon = c; return this; }

    public Bill checkout() {
        double subtotal = 0.0;
        for (Pizza p : pizzas) subtotal += p.price();
        double discount = (coupon == null) ? 0.0 : coupon.discountFor(subtotal);
        return new Bill(subtotal, discount);
    }

    public void printItems() {
        int i = 1;
        for (Pizza p : pizzas) {
            System.out.print("Pizza " + (i++) + " - " + p.size + " " + p.crust + " : $" + Bill.round2(p.price()) + " [");
            for (int j = 0; j < p.toppings.size(); j++) {
                System.out.print(p.toppings.get(j).name);
                if (j < p.toppings.size() - 1) System.out.print(", ");
            }
            System.out.println("]");
        }
    }
}

// -------- Demo --------
public class PizzaPriceCalculator {
    public static void main(String[] args) {
        Order order = new Order()
            .addPizza(new Pizza("MEDIUM", "PAN").addTopping("PEPPERONI").addTopping("MUSHROOM"))
            .addPizza(new Pizza("SMALL", "THIN").addTopping("ONION").addTopping("OLIVE"))
            .applyCoupon(new Coupon("SAVE10", 10.0, 10.0)); // 10% off if subtotal >= $10

        order.printItems();
        Bill bill = order.checkout();
        bill.print();
    }
}
