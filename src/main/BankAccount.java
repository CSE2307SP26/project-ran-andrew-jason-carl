package main;

import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private AccountType accountType;
    private int savingsWithdrawalCount = 0;

    private static final DateTimeFormatter TX_TIMESTAMP_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final double DEFAULT_INTEREST_RATE = 0.05;
    private static final double MIN_SAVINGS_BALANCE = 1000;
    private static final double SAVINGS_INTEREST_RATE = 0.1;
    private static final double SAVINGS_WITHDRAWAL_FEE = 2.5;
    private static final int FREE_SAVINGS_WITHDRAWALS = 6;

    // overloading constructors
    public BankAccount() {
        this.balance = 0;
        this.accountName = "default";
        this.accountClosed = false;
        this.accountFrozen = false;
        this.accountType = AccountType.CHECKING; // default checking account
        this.interestRate = DEFAULT_INTEREST_RATE;
    }

    public BankAccount(String accountName) {
        this.balance = 0;
        this.accountName = accountName;
        this.accountClosed = false;
        this.accountFrozen = false;
        this.accountType = AccountType.CHECKING; // default checking account
        this.interestRate = DEFAULT_INTEREST_RATE;
    }

    public BankAccount(String accountName, double interestRate) {
        this.balance = 0;
        this.accountName = accountName;
        this.accountClosed = false;
        this.accountFrozen = false;
        this.accountType = AccountType.CHECKING; // default checking account
        setInterestRate(interestRate);
    }

    public BankAccount(String accountName, AccountType accountType) {
        this.balance = 0;
        this.accountName = accountName;
        this.accountClosed = false;
        this.accountFrozen = false;
        this.accountType = accountType;
        if (accountType == AccountType.SAVINGS) {
            this.interestRate = SAVINGS_INTEREST_RATE;
        } else {
            this.interestRate = DEFAULT_INTEREST_RATE;
        }
    }

    public BankAccount(String accountName, double interestRate, AccountType accountType) {
        this.balance = 0;
        this.accountName = accountName;
        this.accountClosed = false;
        this.accountFrozen = false;
        setInterestRate(interestRate);
        this.accountType = accountType;
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

    public String getAccountName() {
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

    public AccountType getAccountType() {
        return this.accountType;
    }

    public void deposit(double amount) {
        deposit(amount, "No category");
    }

    public void deposit(double amount, String category) {
        if (this.accountClosed) {
            System.out.println("Account is closed. No transactions can be made.");
            return;
        }
        if (this.accountFrozen) {
            System.out.println("Account is frozen. No transactions can be made.");
            return;
        }
        if (amount > 0) {
            this.balance += amount;
            String date = LocalDate.now().format(TX_TIMESTAMP_FMT);
            this.transactionHistory.add(date + " | Deposit: " + amount + " | Category: " + category);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public double getBalance() {
        if (this.accountClosed) {
            System.out.println("Account is closed. No transactions can be made.");
            return 0;
        }
        return this.balance;
    }

    public String getTransactionHistory() {
        if (this.transactionHistory.isEmpty()) {
            return "There are no transactions yet.\n\n";
        }
        StringBuilder history = new StringBuilder();
        for (String transaction : this.transactionHistory) {
            history.append(transaction).append("\n");
        }
        return history.toString();
    }

    public boolean closeAccount() {
        if (this.balance == 0) {
            this.balance = 0;
            this.accountClosed = true;
            return true;
        } else {
            return false;
        }
    }

    public void transferTo(BankAccount to, double amount) {
        if (this.accountClosed) {
            System.out.println("Account is closed. No transactions can be made.");
            return;
        }
        if (this.accountFrozen) {
            System.out.println("Account is frozen. No transactions can be made.");
            return;
        }
        if (to == null) {
            throw new IllegalArgumentException();
        }
        if (this == to) {
            throw new IllegalArgumentException();
        }
        double fromAmount = this.balance;

        if (savingsWithdrawalCount >= FREE_SAVINGS_WITHDRAWALS && this.accountType == AccountType.SAVINGS) {
            System.out.println("You have ran out of free saving withdrawals, a fee of " + SAVINGS_WITHDRAWAL_FEE
                    + " will be applied to this transfer.");
            this.transactionHistory
                    .add("Withdrawal fee: " + SAVINGS_WITHDRAWAL_FEE + " | Category: SAVINGS ACCOUNT WITHDRAWAL FEE");
            fromAmount -= SAVINGS_WITHDRAWAL_FEE;
        } else {
            if (this.accountType == AccountType.SAVINGS) {
                savingsWithdrawalCount++;
            }
        }

        if (fromAmount >= amount && amount > 0) {
            this.balance -= amount;
            to.balance += amount;
            this.transactionHistory.add("Transfer to account " + to.getAccountName() + ": " + amount);
            to.transactionHistory.add("Transfer from account " + this.getAccountName() + ": " + amount);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public boolean withdraw(double amount) {
        return withdraw(amount, "No category");
    }

    public boolean withdraw(double amount, String category) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        if (this.accountClosed || this.accountFrozen) {
            System.out.println("Account is closed or frozen. No transactions can be made.");
            return false;
        }

        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        // savings minimum balance check
        if (accountType == AccountType.SAVINGS &&
                balance - amount < MIN_SAVINGS_BALANCE) {
            System.out.println("Withdrawal would put savings account below minimum balance of " + MIN_SAVINGS_BALANCE);
            return false;
        }

        // apply fee only if withdrawal is valid
        if (accountType == AccountType.SAVINGS) {
            if (savingsWithdrawalCount >= FREE_SAVINGS_WITHDRAWALS) {
                balance -= SAVINGS_WITHDRAWAL_FEE;
                transactionHistory.add("Withdrawal fee: " + SAVINGS_WITHDRAWAL_FEE +
                        " | Category: SAVINGS ACCOUNT WITHDRAWAL FEE");
                System.out.println("You have ran out of free saving withdrawals, a fee of " + SAVINGS_WITHDRAWAL_FEE
                        + " has been applied to this withdrawal.");
            }
            savingsWithdrawalCount++;
        }

        this.balance -= amount;
        categorySpending.merge(category,amount,Double::sum);
        String date = LocalDate.now().format(TX_TIMESTAMP_FMT);
        this.transactionHistory.add(date + " | Withdrawal: " + amount + " | Category: " + category);
        return true;
    }

    public void setAccountName(String newName) {
        if (newName == null || newName.isEmpty()) {
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
        try {
            // format: "yyyy-MM-dd | Type: amount | Category: X"
            // split on "| " to get the middle segment e.g. "Withdrawal: 50.0"
            String[] pipeParts = transaction.split(" \\| ");
            for (String part : pipeParts) {
                if (part.contains(":")) {
                    String afterColon = part.split(":")[1].trim();
                    // afterColon might be "50.0" or "50.0 " — take first token
                    String amountStr = afterColon.split(" ")[0].trim();
                    return Double.parseDouble(amountStr);
                }
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public Map<String, Double> getSpendingSummary(LocalDate from, LocalDate to) {
        Map<String, Double> summary = new HashMap<>();
        for (String transaction : transactionHistory) {
            // only count withdrawals (spending)
            if (!transaction.contains("Withdrawal:")) continue;
            try {
                String datePart = transaction.split(" \\| ")[0];
                LocalDate txDate = LocalDate.parse(datePart, TX_TIMESTAMP_FMT);
                if (txDate.isBefore(from) || txDate.isAfter(to)) continue;
                String categoryPart = transaction.split("Category: ")[1];
                double amount = parseTransactionAmount(transaction);
                summary.merge(categoryPart, amount, Double::sum);
            } catch (Exception e) {
                // skip malformed entries
            }
        }
        return summary;
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