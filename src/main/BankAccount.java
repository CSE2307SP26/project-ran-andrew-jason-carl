package main;

import java.util.ArrayList;

public class BankAccount {

    private double balance;
    private ArrayList<String> transactionHistory = new ArrayList<>();

    public BankAccount() {
        this.balance = 0;
    }

    public void deposit(double amount) {
        if(amount > 0) {
            this.balance += amount;
            
            // add transaction to history 
            this.transactionHistory.add("Deposit: " + amount);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public double getBalance() {
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
    public void transferTo(BankAccount to, double amount){
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
        }
        else{
          throw new IllegalArgumentException();
        }
    }
   
    public void withdraw(double amount){
        double curr = this.balance;
        
        if(amount > 0 && amount <= curr){
            this.balance -= amount;
        }
        else{
            throw new IllegalArgumentException();
        }
    }
}
