package main;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private List<BankAccount> accounts;

    public Customer(String username) {
        super(username, "password");
        this.accounts = new ArrayList<>();
    }

    public Customer(String username, String password) {
        super(username, password);
        this.accounts = new ArrayList<>();
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }

    public boolean removeAccount(BankAccount account) {
        return accounts.remove(account);
    }

    public List<BankAccount> getAccounts() {
        return accounts;
    }
}