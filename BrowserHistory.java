// Page : url, title, constructor(), toString()
// BrowserHistory : Stack<Page>, visit(url,title), back(), current(), printHistory()
// ask interviewer for back or front to use Stack ] .if needed Queue (use add , remove, peek)
import java.util.*;

// Page
class Page {
    String url;
    String title;

    public Page(String url, String title) {
        this.url = url;
        this.title = title;
    }

    @Override
    public String toString() {
        return title + " (" + url + ")";
    }
}

// BrowserHistory
class BrowserHistory {
    Stack<Page> history = new Stack<>();

    public BrowserHistory(String homeUrl, String homeTitle) {
        history.push(new Page(homeUrl, homeTitle));
    }

    // Visit a new page
    public void visit(String url, String title) {
        history.push(new Page(url, title));
        System.out.println("Visited: " + current());
    }

    // Go back to previous page
    public void back() {
        if (history.size() > 1) {
            history.pop(); // remove current
            System.out.println("Back -> " + current());
        } else {
            System.out.println("No previous page!");
        }
    }

    public String current() {
        return history.peek().toString();
    }

    public void printHistory() {
        System.out.println("=== Browser History ===");
        for (int i = history.size() - 1; i >= 0; i--) {
            System.out.println(history.get(i));
        }
        System.out.println();
    }
}

// Demo
public class BrowserHistoryDemo {
    public static void main(String[] args) {
        BrowserHistory bh = new BrowserHistory("https://example.com", "Home");

        bh.visit("https://news.com", "News");
        bh.visit("https://news.com/world", "World News");
        bh.visit("https://docs.com/java", "Java Docs");

        bh.printHistory();

        bh.back();  // Java Docs -> World News
        bh.back();  // World News -> News

        bh.printHistory();
    }
}
