package main;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Admin extends User {
    private String adminId;
    private List<String> auditLog = new ArrayList<>();
    private static final DateTimeFormatter TIMESTAMP_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Admin(String username, String adminId) {
        super(username);
        this.adminId = adminId;
    }

    public String getAdminId() {
        return this.adminId;
    }

    public void logAdminAction(String action) {
        logAction(action);
    }

    private void logAction(String action) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FMT);
        auditLog.add("[" + timestamp + "] " + action);
    }

    public List<String> getAuditLog() {
        return auditLog;
    }

    public void viewAuditLog() {
        if (auditLog.isEmpty()) {
            System.out.println("No admin actions have been logged yet.\n");
            return;
        }
        System.out.println("\n=== Admin Audit Log ===");
        for (String entry : auditLog) {
            System.out.println(entry);
        }
        System.out.println("=======================\n");
    }

    // Freeze an account (blocks all deposits, withdrawals, transfers)
    public void freezeAccount(BankAccount account) {
        if (account.isFrozen()) {
            System.out.println("Account is already frozen.");
            return;
        }
        account.freeze();
        logAction("FREEZE: Account \"" + account.getAccountName() + "\"");
        System.out.println("Account \"" + account.getAccountName() + "\" has been frozen.");
    }

    public void unfreezeAccount(BankAccount account) {
        if (!account.isFrozen()) {
            System.out.println("Account is not frozen.");
            return;
        }
        account.unfreeze();
        logAction("UNFREEZE: Account \"" + account.getAccountName() + "\"");
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