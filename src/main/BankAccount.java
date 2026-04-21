package main;

import java.util.ArrayList;

public class BankAccount {

    private double balance;
    private String accountName;
    private ArrayList<String> transactionHistory = new ArrayList<>();
    private boolean accountClosed;
    private boolean accountFrozen;
    private double interestRate;

    public static final double DEFAULT_INTEREST_RATE = 0.05;

    public BankAccount() {
        this.balance = 0;
        this.accountName = "default";
        this.accountClosed = false;
        this.accountFrozen = false;
        this.interestRate = DEFAULT_INTEREST_RATE;
    }

    public BankAccount(String accountName) {
        this.balance = 0;
        this.accountName = accountName;
        this.accountClosed = false;
        this.accountFrozen = false;
        this.interestRate = DEFAULT_INTEREST_RATE;
    }

    public BankAccount(String accountName, double interestRate) {
        this.balance = 0;
        this.accountName = accountName;
        this.accountClosed = false;
        this.accountFrozen = false;
        setInterestRate(interestRate);
    }

    public double getInterestRate() {
        return this.interestRate;
    }

    public void setInterestRate(double rate) {
        if (rate < 0 || rate > 1) {
            throw new IllegalArgumentException("Interest rate must be between 0 and 1");
        }
        this.interestRate = rate;
    }

    // Applies the account's stored interest rate to the current balance
    public void applyInterest() {
        double interest = this.balance * this.interestRate;
        deposit(interest);
    }

    public String getAccountName(){
        return this.accountName;
    }

    public boolean isFrozen() {
        return this.accountFrozen;
    }

    public void freeze() {
        this.accountFrozen = true;
    }

    public void unfreeze() {
        this.accountFrozen = false;
    }

    public void deposit(double amount){
        deposit (amount, "No category");
    }

    public void deposit(double amount, String category) {
        if(accountClosed) {
            System.out.println("Account is closed. No transactions can be made.");
            return;
        }
        if(accountFrozen) {
            System.out.println("Account is frozen. No transactions can be made.");
            return;
        }
        if(amount > 0) {
            this.balance += amount;
            this.transactionHistory.add("Deposit: " + amount + " | Category: " + category);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public double getBalance() {
        if(accountClosed) {
            System.out.println("Account is closed. No transactions can be made.");
            return 0;
        }
        return this.balance;
    }

    public String getTransactionHistory() {
        if(this.transactionHistory.isEmpty()) {
            return "There are no transactions yet.\n\n";
        }
        StringBuilder history = new StringBuilder();
        for(String transaction : this.transactionHistory) {
            history.append(transaction).append("\n");
        }
        return history.toString();
    }

    public boolean closeAccount() {
        if(this.balance == 0) {
            balance = 0;
            accountClosed = true;
            return true;
        } else {
            return false;
        }
    }

    public void transferTo(BankAccount to, double amount){
        if(accountClosed) {
            System.out.println("Account is closed. No transactions can be made.");
            return;
        }
        if(accountFrozen) {
            System.out.println("Account is frozen. No transactions can be made.");
            return;
        }
        if(to == null){
            throw new IllegalArgumentException();
        }
        if (this == to) {
            throw new IllegalArgumentException();
        }
        double fromAmount = this.balance;
        if(fromAmount >= amount && amount > 0){
            this.balance -= amount;
            to.balance += amount;
            this.transactionHistory.add("Transfer to account " + to.getAccountName() + ": " + amount);
            to.transactionHistory.add("Transfer from account " + this.getAccountName() + ": " + amount);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void withdraw(double amount){
        withdraw(amount, "No category");
    }

    public void withdraw(double amount, String category){
        double curr = this.balance;
        if(accountClosed) {
            System.out.println("Account is closed. No transactions can be made.");
            return;
        }
        if(accountFrozen) {
            System.out.println("Account is frozen. No transactions can be made.");
            return;
        }
        if(amount > 0 && amount <= curr){
            this.balance -= amount;
            this.transactionHistory.add("Withdrawal: " + amount + " | Category: " + category);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setAccountName (String newName){
        if (newName == null || newName.isEmpty()){
            throw new IllegalArgumentException();
        } else {
            this.accountName = newName;
        }
    }
}