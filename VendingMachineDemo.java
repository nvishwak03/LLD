// Item : code, name, price, quantity, constructor(), toString()
// VendingMachine : inventory(map<code,Item>), insertedAmount, acceptedCoins(set), addItem(), restock(), insertCoin(amount), selectItem(code), refund(), printItems(), status()

import java.util.*;

// Item
class Item {
    String code;   // e.g., "A1", "B3"
    String name;   // e.g., "Chips"
    double price;  // e.g., 1.25
    int quantity;

    public Item(String code, String name, double price, int quantity) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return code + " " + name + " $" + price + " x" + quantity;
    }
}

// VendingMachine (basic)
class VendingMachine {
    Map<String, Item> inventory = new LinkedHashMap<>();
    double insertedAmount = 0.0;
    Set<Double> acceptedCoins = new HashSet<>(Arrays.asList(0.05, 0.10, 0.25, 1.00)); // 5c,10c,25c,$1

    public VendingMachine addItem(Item i) {
        inventory.put(i.code, i);
        return this;
    }

    public boolean restock(String code, int addQty) {
        Item it = inventory.get(code);
        if (it == null || addQty <= 0) return false;
        it.quantity += addQty;
        return true;
    }

    public boolean insertCoin(double amount) {
        if (!acceptedCoins.contains(amount)) {
            System.out.println("Coin rejected: $" + amount);
            return false;
        }
        insertedAmount += amount;
        System.out.println("Accepted $" + amount + " | Inserted total: $" + insertedAmount);
        return true;
    }

    public boolean selectItem(String code) {
        Item it = inventory.get(code);
        if (it == null) { System.out.println("Invalid selection: " + code); return false; }
        if (it.quantity <= 0) { System.out.println(it.name + " is SOLD OUT"); return false; }
        if (insertedAmount < it.price) {
            double need = it.price - insertedAmount;
            System.out.println("Insufficient funds. Need $" + need + " more.");
            return false;
        }
        // vend
        it.quantity -= 1;
        double change = insertedAmount - it.price;
        insertedAmount = 0.0;
        System.out.println("Dispensing: " + it.name + " (" + it.code + ")");
        if (change > 0) System.out.println("Change: $" + change);
        return true;
    }

    public void refund() {
        if (insertedAmount <= 0) {
            System.out.println("Nothing to refund.");
        } else {
            System.out.println("Refunded: $" + insertedAmount);
            insertedAmount = 0.0;
        }
    }

    public void printItems() {
        System.out.println("=== ITEMS ===");
        for (Item it : inventory.values()) System.out.println(it);
        System.out.println();
    }

    public void status() {
        System.out.println("Inserted amount: $" + insertedAmount);
    }
}

// Demo
public class VendingMachineDemo {
    public static void main(String[] args) {
        VendingMachine vm = new VendingMachine()
            .addItem(new Item("A1", "Chips", 1.25, 3))
            .addItem(new Item("A2", "Soda", 1.50, 2))
            .addItem(new Item("B1", "Candy", 0.95, 5));

        vm.printItems();

        // Insert coins and buy
        vm.insertCoin(1.00);
        vm.insertCoin(0.25);
        vm.selectItem("A1"); // should vend Chips, no change

        vm.status();
        vm.insertCoin(1.00);
        vm.selectItem("A2"); // vend Soda, change $0.50 if we add extra coin
        vm.insertCoin(0.25);
        vm.insertCoin(0.25);
        vm.selectItem("A2"); // should vend Soda, exact or with change depending on previous

        vm.printItems();

        // Try invalid coin and refund
        vm.insertCoin(0.30);  // rejected coin
        vm.insertCoin(0.25);
        vm.refund();
    }
}
