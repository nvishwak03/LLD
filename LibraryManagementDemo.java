import java.util.*;

// ----- Book -----
class Book {
    int id;
    String title;
    String author;
    int copies;

    public Book(int id, String title, String author, int copies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.copies = copies;
    }

    @Override
    public String toString() {
        return id + " \"" + title + "\" by " + author + " (copies=" + copies + ")";
    }
}

// ----- Member -----
class Member {
    int id;
    String name;
    Set<Integer> borrowed = new HashSet<>();

    public Member(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return id + " " + name + " borrowed=" + borrowed;
    }
}

// ----- Library -----
class Library {
    Map<Integer, Book> books = new HashMap<>();
    Map<Integer, Member> members = new HashMap<>();
    int nextBookId = 1;
    int nextMemberId = 1;

    public int addBook(String title, String author, int copies) {
        if (copies < 1) copies = 1;
        Book b = new Book(nextBookId++, title, author, copies);
        books.put(b.id, b);
        System.out.println("Added: " + b);
        return b.id;
    }

    public int addMember(String name) {
        Member m = new Member(nextMemberId++, name);
        members.put(m.id, m);
        System.out.println("New member: " + m.id + " " + m.name);
        return m.id;
    }

    public List<Book> searchTitle(String keyword) {
        List<Book> res = new ArrayList<>();
        if (keyword == null) return res;
        String k = keyword.toLowerCase();
        for (Book b : books.values()) {
            if (b.title.toLowerCase().contains(k)) res.add(b);
        }
        return res;
    }

    public List<Book> searchAuthor(String keyword) {
        List<Book> res = new ArrayList<>();
        if (keyword == null) return res;
        String k = keyword.toLowerCase();
        for (Book b : books.values()) {
            if (b.author.toLowerCase().contains(k)) res.add(b);
        }
        return res;
    }

    public boolean borrow(int bookId, int memberId) {
        Book b = books.get(bookId);
        Member m = members.get(memberId);
        if (b == null) { System.out.println("Book not found"); return false; }
        if (m == null) { System.out.println("Member not found"); return false; }
        if (b.copies <= 0) { System.out.println("No copies available"); return false; }
        if (m.borrowed.contains(bookId)) { System.out.println("Already borrowed"); return false; }

        b.copies -= 1;
        m.borrowed.add(bookId);
        System.out.println(m.name + " borrowed \"" + b.title + "\"");
        return true;
    }

    public boolean returnBook(int bookId, int memberId) {
        Book b = books.get(bookId);
        Member m = members.get(memberId);
        if (b == null || m == null) { System.out.println("Invalid book/member"); return false; }
        if (!m.borrowed.contains(bookId)) { System.out.println("Member doesn't have this book"); return false; }

        m.borrowed.remove(bookId);
        b.copies += 1;
        System.out.println(m.name + " returned \"" + b.title + "\"");
        return true;
    }

    public void printBooks() {
        System.out.println("=== BOOKS ===");
        for (Book b : books.values()) {
            System.out.println(b);
        }
        System.out.println();
    }

    public void printMembers() {
        System.out.println("=== MEMBERS ===");
        for (Member m : members.values()) {
            System.out.println(m);
        }
        System.out.println();
    }
}

// ----- Demo -----
public class LibraryManagementDemo {
    public static void main(String[] args) {
        Library lib = new Library();

        int b1 = lib.addBook("Clean Code", "Robert C. Martin", 3);
        int b2 = lib.addBook("Effective Java", "Joshua Bloch", 2);
        int b3 = lib.addBook("Design Patterns", "GoF", 1);

        int m1 = lib.addMember("Alice");
        int m2 = lib.addMember("Bob");

        System.out.println("\nSearch by title 'Java':");
        for (Book b : lib.searchTitle("Java")) {
            System.out.println(b);
        }

        System.out.println("\nBorrow/Return flow:");
        lib.borrow(b2, m1);  
        lib.borrow(b2, m2);  
        lib.returnBook(b2, m1);  
        lib.borrow(b2, m2);  

        System.out.println();
        lib.printBooks();
        lib.printMembers();
    }
}
