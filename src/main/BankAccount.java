package main;

public class BankAccount {

    private double balance;
    private String accountName;

    public BankAccount() {
        this.balance = 0;
        this.accountName = "default";
    }

    public BankAccount(String accountName) {
        this.balance = 0;
        this.accountName = accountName;
    }

    public String getAccountName(){
        return this.accountName;
    }

    public void deposit(double amount) {
        if(amount > 0) {
            this.balance += amount;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public double getBalance() {
        return this.balance;
    }
}
