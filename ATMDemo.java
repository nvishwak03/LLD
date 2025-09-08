// Card : cardNumber, pin, accountId, constructor()
// Account : id, holderName, balance, constructor(), deposit(amount), withdraw(amount)->boolean, toString()
// ATM : cashBalance, currentAccount, isAuthenticated, accounts(map<id,Account>), cards(map<cardNumber,Card>),
// constructor(initialCash), addAccount(account), addCard(card), insertCard(cardNumber,pin)->boolean,
// checkBalance(), deposit(amount), withdraw(amount)->boolean, eject(), status()
// Demo : simple flow — insert card, check balance, deposit, withdraw, eject

import java.util.*;

// ----- Card -----
class Card {
    String cardNumber;
    String pin;
    int accountId;

    public Card(String cardNumber, String pin, int accountId) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.accountId = accountId;
    }
}

// ----- Account -----
class Account {
    int id;
    String holderName;
    double balance;

    public Account(int id, String holderName, double balance) {
        this.id = id;
        this.holderName = holderName;
        this.balance = balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) return;
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) return false;
        if (balance < amount) return false;
        balance -= amount;
        return true;
    }

    @Override
    public String toString() {
        return id + " " + holderName + " $" + balance;
    }
}

// ----- ATM -----
class ATM {
    double cashBalance;
    Map<Integer, Account> accounts = new HashMap<>();
    Map<String, Card> cards = new HashMap<>();

    Account currentAccount = null;
    boolean isAuthenticated = false;

    public ATM(double initialCash) {
        this.cashBalance = initialCash;
    }

    public ATM addAccount(Account a) { accounts.put(a.id, a); return this; }
    public ATM addCard(Card c) { cards.put(c.cardNumber, c); return this; }

    // Insert card + PIN -> set current session
    public boolean insertCard(String cardNumber, String pin) {
        if (currentAccount != null) { System.out.println("A session is already active"); return false; }
        Card c = cards.get(cardNumber);
        if (c == null) { System.out.println("Card not recognized"); return false; }
        if (!c.pin.equals(pin)) { System.out.println("Invalid PIN"); return false; }
        Account a = accounts.get(c.accountId);
        if (a == null) { System.out.println("Linked account not found"); return false; }
        currentAccount = a; isAuthenticated = true;
        System.out.println("Welcome, " + a.holderName);
        return true;
    }

    public void eject() {
        if (currentAccount != null) System.out.println("Goodbye, " + currentAccount.holderName);
        currentAccount = null; isAuthenticated = false;
    }

    public Double checkBalance() {
        if (!isAuthenticated) { System.out.println("Please authenticate"); return null; }
        System.out.println("Balance: $" + currentAccount.balance);
        return currentAccount.balance;
    }

    public boolean deposit(double amount) {
        if (!isAuthenticated) { System.out.println("Please authenticate"); return false; }
        if (amount <= 0) { System.out.println("Invalid amount"); return false; }
        currentAccount.deposit(amount);
        cashBalance += amount;
        System.out.println("Deposited $" + amount + " | New balance: $" + currentAccount.balance);
        return true;
    }

    public boolean withdraw(double amount) {
        if (!isAuthenticated) { System.out.println("Please authenticate"); return false; }
        if (amount <= 0) { System.out.println("Invalid amount"); return false; }
        if (cashBalance < amount) { System.out.println("ATM has insufficient cash"); return false; }
        boolean ok = currentAccount.withdraw(amount);
        if (!ok) { System.out.println("Insufficient funds"); return false; }
        cashBalance -= amount;
        System.out.println("Dispensed $" + amount + " | New balance: $" + currentAccount.balance);
        return true;
    }

    public String status() {
        return "ATM cash $" + cashBalance + " | Session " + (isAuthenticated ? "ACTIVE" : "NONE");
    }
}

// ----- Demo -----
public class ATMDemo {
    public static void main(String[] args) {
        ATM atm = new ATM(2000.0);
        // bank setup
        atm.addAccount(new Account(1, "Alice", 500.0))
           .addAccount(new Account(2, "Bob", 1200.0))
           .addCard(new Card("1111-2222-3333-4444", "1234", 1))
           .addCard(new Card("5555-6666-7777-8888", "4321", 2));

        System.out.println(atm.status());

        // Session 1
        atm.insertCard("1111-2222-3333-4444", "1234");
        atm.checkBalance();
        atm.deposit(200.0);
        atm.withdraw(300.0);
        atm.checkBalance();
        atm.eject();
        System.out.println(atm.status());

        // Session 2 (wrong PIN then correct)
        atm.insertCard("5555-6666-7777-8888", "1111"); // wrong
        atm.insertCard("5555-6666-7777-8888", "4321"); // ok
        atm.withdraw(500.0);
        atm.checkBalance();
        atm.eject();

        System.out.println(atm.status());
    }
}
