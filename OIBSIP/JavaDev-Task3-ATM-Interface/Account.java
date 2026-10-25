import java.util.ArrayList;

public class Account {
    private String userId;
    private String pin;
    private double balance;
    private ArrayList<Transaction> history;

    public Account(String userId, String pin, double balance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = balance;
        this.history = new ArrayList<>();
    }

    public String getUserId() { return userId; }
    public boolean checkPin(String p) { return pin.equals(p); }
    public double getBalance() { return balance; }
    public ArrayList<Transaction> getHistory() { return history; }

    public void deposit(double amount) {
        balance += amount;
        history.add(new Transaction("DEPOSIT", amount, "Deposited to " + userId + ". New Bal: $" + balance));
    }

    public boolean withdraw(double amount) {
        if (amount > balance) return false;
        balance -= amount;
        history.add(new Transaction("WITHDRAW", amount, "Withdrawn from " + userId + ". New Bal: $" + balance));
        return true;
    }

    public void addTransaction(Transaction t) {
        history.add(t);
    }
}
