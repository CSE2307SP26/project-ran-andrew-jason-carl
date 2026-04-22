package main;

import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class BankAccount {

    private double balance;
    private String accountName;
    private ArrayList<String> transactionHistory = new ArrayList<>();
    private boolean accountClosed;
    private boolean accountFrozen;
    private double interestRate;
    private Map<String, Double> categorySpending = new HashMap<>();

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
            categorySpending.merge(category, amount, Double::sum);
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

    public Path generateBankStatement(String forUser) {
        String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        StringBuilder statementName = new StringBuilder();
        statementName.append("bank_statement_");
        statementName.append(datePart);
        statementName.append("_");
        statementName.append(forUser);
        statementName.append("_");
        statementName.append(accountName);
        statementName.append(".txt");

        Path outputPath = Paths.get(System.getProperty("user.dir"), statementName.toString());

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
            writer.write("Bank Statement");
            writer.newLine();
            writer.write("Date: " + LocalDate.now());
            writer.newLine();
            writer.write("Account Name: " + ((this.accountName == null || this.accountName.isBlank()) ? "N/A" : this.accountName));
            writer.newLine();
            writer.write("Current Balance: " + this.balance);
            writer.newLine();
            writer.write("Transaction History:");
            writer.newLine();

            if (this.transactionHistory.isEmpty()) {
                writer.write("There are no transactions yet.");
                writer.newLine();
            } else {
                for (String transaction : this.transactionHistory) {
                    writer.write(transaction);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save bank statement.", e);
        }

        return outputPath;
    }

    public boolean wouldExceedLimit(Customer customer, double amount, String category) {
        Double limit = customer.getCategoryLimit(category);
        if (limit == null) {
            return false; 
        }
        double currentSpending = categorySpending.getOrDefault(category, 0.0);
        return (currentSpending + amount) > limit;
    }

    public double getCategorySpending(String category) {
        return categorySpending.getOrDefault(category, 0.0);
    }

    public String getLargestTransaction(){
        if(transactionHistory.isEmpty()){
            return "There are no transactions yet.\n\n";
        }
        String largest = null;
        double largestAmount = -1;
        for (String transaction: transactionHistory){
            double amount = parseTransactionAmount(transaction);
            if (amount > largestAmount) {
                largestAmount = amount;
                largest = transaction;
            }
        }
        return largest;
    }

    public String getLargestTransactionByCategory(String category){
        String largest = null;
        double largestAmount = -1;
        for (String transaction: transactionHistory){
            if (transaction.contains("Category: " + category)) {
                double amount = parseTransactionAmount(transaction);
                if (amount > largestAmount) {
                    largestAmount = amount;
                    largest = transaction;
                }
            }
        }
        return largest == null ? "There are no transactions in the category: " + category : largest;
    }

    private double parseTransactionAmount(String transaction) {
        try{
            String[] parts = transaction.split(":");
            String amountPart = parts[1].split(" ")[0];
            return Double.parseDouble(amountPart);
        } catch (Exception e) {
            return 0;
        }
    }

    public List<String> getTopFiveTransactions(){
        if (transactionHistory.isEmpty()){
            return new ArrayList<>();
        }
        List<String> sorted = new ArrayList<>(transactionHistory);
        sorted.sort((a,b) -> Double.compare(parseTransactionAmount(b), parseTransactionAmount(a)));
        return sorted.subList(0, Math.min(5, sorted.size()));
    }
    

}