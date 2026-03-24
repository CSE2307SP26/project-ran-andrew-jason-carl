package main;

public class BankAccount {

    private double balance;
    private boolean accountClosed;

    public BankAccount() {
        this.balance = 0;
        this.accountClosed = false;
    }

    public void deposit(double amount) {
        if(accountClosed) {
            System.out.println("Account is closed. No transactions can be made.");
            return;
        }

        if(amount > 0) {
            this.balance += amount;
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

    // close account 
    public boolean closeAccount() {
        if(this.balance == 0) {
            balance = 0;
            accountClosed = true;
            return true;
        }
        else {
            return false;
        }
    }
}
