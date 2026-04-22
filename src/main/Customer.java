package main;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private List<BankAccount> accounts;
    private List<String> categories;

    public Customer(String username) {
        super(username, "password");
        this.accounts = new ArrayList<>();
        initalizeCategories();
    }

    public Customer(String username, String password) {
        super(username, password);
        this.accounts = new ArrayList<>();
        initalizeCategories();
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }

    public boolean removeAccount(BankAccount account) {
        return accounts.remove(account);
    }

    public void showAccounts() {
        for (BankAccount account : accounts) {
            System.out.println(account.getAccountName() + ": " + account.getBalance() + " | TYPE: " + account.getAccountType());
        }
    }
    
    public List<BankAccount> getAccounts() {
        return accounts;
    }

    private void initalizeCategories() {
        categories = new ArrayList<>();
        categories.add("Food");
        categories.add("Entertainment");
        categories.add("Transportation");
        categories.add("Utilities");
        categories.add("Other");
    }

    public void addCategory(String category) {
        if(category == null || category.isEmpty()) {
            throw new IllegalArgumentException();
        }
        categories.add(category);
    }

    public List<String> getCategories() {
        return categories;
    }
}