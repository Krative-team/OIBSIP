import java.util.HashMap;
import java.util.Map;

public class Bank {
    private Map<String, Account> accounts = new HashMap<>();

    public Bank() {
        // sample accounts
        accounts.put("user1", new Account("user1", "1234", 5000));
        accounts.put("user2", new Account("user2", "2345", 3000));
        accounts.put("admin", new Account("admin", "0000", 10000));
    }

    public Account getAccount(String userId) {
        return accounts.get(userId);
    }

    public boolean transfer(String fromId, String toId, double amount) {
        Account from = accounts.get(fromId);
        Account to = accounts.get(toId);
        if (from == null || to == null) return false;
        if (from.getBalance() < amount) return false;

        from.withdraw(amount);
        to.deposit(amount);

        // Override last auto logs with transfer logs for clarity
        from.getHistory().remove(from.getHistory().size()-1);
        to.getHistory().remove(to.getHistory().size()-1);

        from.addTransaction(new Transaction("TRANSFER-OUT", amount, "To " + toId + ". Bal: $" + from.getBalance()));
        to.addTransaction(new Transaction("TRANSFER-IN", amount, "From " + fromId + ". Bal: $" + to.getBalance()));
        return true;
    }
}
