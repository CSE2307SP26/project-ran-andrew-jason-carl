package main;

import java.util.List;

public class Admin extends User {
    private String adminId;

    public Admin(String username, String adminId) {
        super(username);
        this.adminId = adminId;
    }

    public String getAdminId() {
        return this.adminId;
    }

    public void collectFee(BankAccount account, double fee) {
        account.withdraw(fee);
    }

    public void addInterest(BankAccount account, double rate) {
        if (rate < 0 || rate > 1) {
            throw new IllegalArgumentException("Interest rate must be between 0 and 1");
        }
        double interest = account.getBalance() * rate;
        account.deposit(interest);
    }

    // Freeze an account (blocks all deposits, withdrawals, transfers)
    public void freezeAccount(BankAccount account) {
        if (account.isFrozen()) {
            System.out.println("Account is already frozen.");
            return;
        }
        account.freeze();
        System.out.println("Account \"" + account.getAccountName() + "\" has been frozen.");
    }

    public void unfreezeAccount(BankAccount account) {
        if (!account.isFrozen()) {
            System.out.println("Account is not frozen.");
            return;
        }
        account.unfreeze();
        System.out.println("Account \"" + account.getAccountName() + "\" has been unfrozen.");
    }

    public void viewAllAccounts(List<Customer> users) {
        if (users == null || users.isEmpty()) {
            System.out.println("No customers in the system.");
            return;
        }
        System.out.println("\n");
        System.out.println("===All Customer Accounts===");
        for (Customer customer : users) {
            System.out.println("Customer: " + customer.getUsername());
            List<BankAccount> accounts = customer.getAccounts();
            if (accounts.isEmpty()) {
                System.out.println("  (no accounts)");
            } else {
                for (BankAccount account : accounts) {
                    String status = account.isFrozen() ? " [FROZEN]" : "";
                    System.out.println("  - " + account.getAccountName()
                            + ": $" + account.getBalance() + status);
                }
            }
        }
        System.out.println("=============================\n");
    }
}