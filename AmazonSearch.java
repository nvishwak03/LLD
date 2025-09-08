// Product : id, name, category, price, constructor(), toString()
// SearchBar : products, addProduct(), search(keyword, category, minPrice, maxPrice), printResults(list)

import java.util.*;

// Product
class Product {
    int id;
    String name;
    String category;
    double price;

    public Product(int id, String name, String category, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    @Override
    public String toString() {
        return id + " " + name + " [" + category + "] $" + price;
    }
}

// SearchBar
class SearchBar {
    List<Product> products = new ArrayList<>();

    public SearchBar addProduct(Product p) {
        products.add(p);
        return this;
    }

    // Simple search by keyword, category, price range
    public List<Product> search(String keyword, String category, Double minPrice, Double maxPrice) {
        List<Product> results = new ArrayList<>();
        for (Product p : products) {
            if (keyword != null && !p.name.toLowerCase().contains(keyword.toLowerCase())) continue;
            if (category != null && !category.equalsIgnoreCase(p.category)) continue;
            if (minPrice != null && p.price < minPrice) continue;
            if (maxPrice != null && p.price > maxPrice) continue;
            results.add(p);
        }
        return results;
    }

    public void printResults(List<Product> list) {
        if (list.isEmpty()) {
            System.out.println("No results found.");
        } else {
            for (Product p : list) {
                System.out.println(p);
            }
        }
        System.out.println();
    }
}

// Demo
public class AmazonSearch{
    public static void main(String[] args) {
        SearchBar engine = new SearchBar()
            .addProduct(new Product(1, "iPhone 15", "Electronics", 999))
            .addProduct(new Product(2, "Samsung Galaxy S23", "Electronics", 899))
            .addProduct(new Product(3, "Nike Running Shoes", "Sportswear", 120))
            .addProduct(new Product(4, "Adidas Hoodie", "Sportswear", 80))
            .addProduct(new Product(5, "Sony Headphones", "Electronics", 199));

        // Example 1: keyword search
        List<Product> r1 = engine.search("iphone", null, null, null);
        System.out.println("Search results for 'iphone':");
        engine.printResults(r1);

        // Example 2: filter by category + price
        List<Product> r2 = engine.search(null, "Electronics", 200.0, 1000.0);
        System.out.println("Electronics priced between 200 and 1000:");
        engine.printResults(r2);

        // Example 3: keyword + category
        List<Product> r3 = engine.search("hoodie", "Sportswear", null, null);
        System.out.println("Search for 'hoodie' in Sportswear:");
        engine.printResults(r3);
    }
}
