// Contact : id, name, phone, email, city, constructor(), toString()
// Filter : nameContains, city, phonePrefix, constructor(), matches(contact)
// PhoneDirectory : contacts, nextId, addContact(), removeContact(id), updatePhone(id,newPhone), getById(id), search(filter), searchByNamePrefix(prefix), searchByPhone(phone), print(list)

import java.util.*;

// Contact
class Contact {
    int id;
    String name;
    String phone;
    String email;
    String city;

    public Contact(int id, String name, String phone, String email, String city) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.city = city;
    }

    @Override
    public String toString() {
        return id + " " + name + " " + phone + " " + email + " " + city;
    }
}

// Filter
class Filter {
    String nameContains; // case-insensitive substring
    String city;         // equals (case-insensitive)
    String phonePrefix;  // startsWith

    public Filter(String nameContains, String city, String phonePrefix) {
        this.nameContains = nameContains;
        this.city = city;
        this.phonePrefix = phonePrefix;
    }

    public boolean matches(Contact c) {
        if (nameContains != null && !c.name.toLowerCase().contains(nameContains.toLowerCase())) return false;
        if (city != null && !city.equalsIgnoreCase(c.city)) return false;
        if (phonePrefix != null && !c.phone.startsWith(phonePrefix)) return false;
        return true;
    }
}

// PhoneDirectory
class PhoneDirectory {
    List<Contact> contacts = new ArrayList<>();
    int nextId = 1;

    // Create & add a contact; returns assigned id
    public int addContact(String name, String phone, String email, String city) {
        Contact c = new Contact(nextId++, name, phone, email, city);
        contacts.add(c);
        System.out.println("Added: " + c);
        return c.id;
    }

    // Remove by id
    public boolean removeContact(int id) {
        Iterator<Contact> it = contacts.iterator();
        while (it.hasNext()) {
            Contact c = it.next();
            if (c.id == id) {
                it.remove();
                System.out.println("Removed: " + c);
                return true;
            }
        }
        System.out.println("Contact id " + id + " not found");
        return false;
    }

    // Update phone by id
    public boolean updatePhone(int id, String newPhone) {
        Contact c = getById(id);
        if (c == null) { System.out.println("Contact id " + id + " not found"); return false; }
        c.phone = newPhone;
        System.out.println("Updated phone: " + c);
        return true;
    }

    // Get a contact by id
    public Contact getById(int id) {
        for (Contact c : contacts) if (c.id == id) return c;
        return null;
    }

    // Generic search with Filter (any field can be null to skip)
    public List<Contact> search(Filter f) {
        List<Contact> out = new ArrayList<>();
        for (Contact c : contacts) if (f == null || f.matches(c)) out.add(c);
        return out;
    }

    // Convenience: search by name prefix (case-insensitive)
    public List<Contact> searchByNamePrefix(String prefix) {
        if (prefix == null) return Collections.emptyList();
        String p = prefix.toLowerCase();
        List<Contact> out = new ArrayList<>();
        for (Contact c : contacts) if (c.name.toLowerCase().startsWith(p)) out.add(c);
        return out;
    }

    // Convenience: search exact phone
    public Contact searchByPhone(String phone) {
        for (Contact c : contacts) if (c.phone.equals(phone)) return c;
        return null;
    }

    // Optional: sort results by name ascending (basic comparator)
    public void sortByName(List<Contact> list) {
        list.sort((c1, c2) -> c1.name.compareToIgnoreCase(c2.name));
    }

    public void print(List<Contact> list) {
        if (list == null || list.isEmpty()) { System.out.println("(no contacts)\n"); return; }
        for (Contact c : list) System.out.println(c);
        System.out.println();
    }

    // Print all contacts (unsorted)
    public void printAll() { print(contacts); }
}

// Demo
public class PhoneDirectory {
    public static void main(String[] args) {
        PhoneDirectory dir = new PhoneDirectory();
        int a = dir.addContact("Alice Johnson", "555-1010", "alice@example.com", "Seattle");
        int b = dir.addContact("Bob Smith", "555-2020", "bob@example.com", "Austin");
        int c = dir.addContact("Carol Baker", "555-3030", "carol@example.com", "Seattle");
        dir.addContact("Frank Wright", "555-9090", "frank@example.com", "NYC");

        System.out.println("\nAll contacts:");
        dir.printAll();

        System.out.println("Search: city=Seattle");
        List<Contact> seattle = dir.search(new Filter(null, "Seattle", null));
        dir.sortByName(seattle);
        dir.print(seattle);

        System.out.println("Search: name starts with 'Ca'");
        dir.print(dir.searchByNamePrefix("Ca"));

        System.out.println("Search: phone 555-2020");
        System.out.println(dir.searchByPhone("555-2020"));
        System.out.println();

        System.out.println("Update Bob's phone and remove Alice:");
        dir.updatePhone(b, "555-2222");
        dir.removeContact(a);

        System.out.println("\nAll contacts after updates:");
        dir.printAll();
    }
}
