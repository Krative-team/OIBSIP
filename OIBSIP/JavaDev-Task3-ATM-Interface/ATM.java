import java.util.Scanner;

public class ATM {
    private Bank bank;
    private Account currentAccount;
    private Scanner sc;

    public ATM(Bank bank) {
        this.bank = bank;
        this.sc = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== WELCOME TO ATM ===");
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Enter User ID: ");
            String userId = sc.nextLine();
            System.out.print("Enter PIN: ");
            String pin = sc.nextLine();

            Account acc = bank.getAccount(userId);
            if (acc != null && acc.checkPin(pin)) {
                currentAccount = acc;
                System.out.println("\nLogin Successful! Welcome " + userId);
                showMenu();
                return;
            } else {
                attempts++;
                System.out.println("Invalid ID/PIN. Attempts left: " + (3 - attempts));
            }
        }
        System.out.println("Access Denied after 3 attempts. Card Blocked.");
    }

    private void showMenu() {
        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.print("Choose: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> showHistory();
                case "2" -> doWithdraw();
                case "3" -> doDeposit();
                case "4" -> doTransfer();
                case "5" -> {
                    System.out.println("Thank you! Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    private void showHistory() {
        System.out.println("\n--- Transaction History for " + currentAccount.getUserId() + " ---");
        if (currentAccount.getHistory().isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (Transaction t : currentAccount.getHistory()) {
                System.out.println(t);
            }
            System.out.println("Current Balance: $" + currentAccount.getBalance());
        }
    }

    private void doWithdraw() {
        System.out.print("Enter amount to withdraw: $");
        try {
            double amt = Double.parseDouble(sc.nextLine());
            if (amt <= 0) { System.out.println("Invalid amount"); return; }
            if (currentAccount.withdraw(amt)) {
                System.out.println("Withdraw Successful. New Balance: $" + currentAccount.getBalance());
            } else {
                System.out.println("Insufficient Funds! Balance: $" + currentAccount.getBalance());
            }
        } catch (NumberFormatException e) { System.out.println("Enter valid number"); }
    }

    private void doDeposit() {
        System.out.print("Enter amount to deposit: $");
        try {
            double amt = Double.parseDouble(sc.nextLine());
            if (amt <= 0) { System.out.println("Invalid amount"); return; }
            currentAccount.deposit(amt);
            System.out.println("Deposit Successful. New Balance: $" + currentAccount.getBalance());
        } catch (NumberFormatException e) { System.out.println("Enter valid number"); }
    }

    private void doTransfer() {
        System.out.print("Enter Recipient Account ID: ");
        String toId = sc.nextLine();
        if (bank.getAccount(toId) == null) { System.out.println("Recipient not found"); return; }
        if (toId.equals(currentAccount.getUserId())) { System.out.println("Cannot transfer to self"); return; }
        System.out.print("Enter amount to transfer: $");
        try {
            double amt = Double.parseDouble(sc.nextLine());
            if (amt <= 0) { System.out.println("Invalid amount"); return; }
            if (bank.transfer(currentAccount.getUserId(), toId, amt)) {
                System.out.println("Transfer Successful to " + toId + ". New Balance: $" + currentAccount.getBalance());
            } else {
                System.out.println("Insufficient Funds! Balance: $" + currentAccount.getBalance());
            }
        } catch (NumberFormatException e) { System.out.println("Enter valid number"); }
    }
}
