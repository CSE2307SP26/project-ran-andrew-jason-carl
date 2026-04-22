package main;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Customer extends User {
    private List<BankAccount> accounts;
    private List<BankAccount> favoriteAccounts;
    private BankAccount primaryAccount;
    private List<String> categories;
    private Map<String, Double> categoryLimits;

    public Customer(String username) {
        super(username, "password");
        this.accounts = new ArrayList<>();
        this.categoryLimits = new HashMap<>();
        initializeCategories();
        this.favoriteAccounts = new ArrayList<>();
        initalizeCategories();
    }

    public Customer(String username, String password) {
        super(username, password);
        this.accounts = new ArrayList<>();
        this.categoryLimits = new HashMap<>();
        initializeCategories();
        this.favoriteAccounts = new ArrayList<>();
        initalizeCategories();
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }

    public boolean removeAccount(BankAccount account) {
        boolean removed = accounts.remove(account);

        if (removed) {
            favoriteAccounts.remove(account);

            if (primaryAccount == account) {
                primaryAccount = null;
            }
        }

        return removed;
    }

    public void showAccounts() {
        for (BankAccount account : accounts) {
            double balance = account.getBalance();
            String lowBalanceStatus = balance < 5.00 ? " | LOW BALANCE" : "";

            System.out.println(account.getAccountName()
                    + ": $" + String.format("%.2f", balance)
                    + lowBalanceStatus
                    + getPrimaryStatus(account)
                    + getFavoriteStatus(account)
                    + " | TYPE: " + account.getAccountType()
                    + "\n");
        }
    }

    public void markFavoriteAccount(BankAccount account) {
        validateOwnedAccount(account);

        if (!favoriteAccounts.contains(account)) {
            favoriteAccounts.add(account);
        }
    }

    public void unmarkFavoriteAccount(BankAccount account) {
        favoriteAccounts.remove(account);
    }

    public boolean isFavoriteAccount(BankAccount account) {
        return favoriteAccounts.contains(account);
    }

    public List<BankAccount> getFavoriteAccounts() {
        return favoriteAccounts;
    }

    public void setPrimaryAccount(BankAccount account) {
        validateOwnedAccount(account);
        primaryAccount = account;
    }

    public BankAccount getPrimaryAccount() {
        return primaryAccount;
    }

    public boolean isPrimaryAccount(BankAccount account) {
        return primaryAccount == account;
    }

    public List<BankAccount> getQuickAccessAccounts() {
        List<BankAccount> quickAccessAccounts = new ArrayList<>();

        if (primaryAccount != null) {
            quickAccessAccounts.add(primaryAccount);
        }

        for (BankAccount account : favoriteAccounts) {
            if (account != primaryAccount) {
                quickAccessAccounts.add(account);
            }
        }

        return quickAccessAccounts;
    }

    private void validateOwnedAccount(BankAccount account) {
        if (account == null || !accounts.contains(account)) {
            throw new IllegalArgumentException();
        }
    }

    private String getPrimaryStatus(BankAccount account) {
        return isPrimaryAccount(account) ? " | PRIMARY" : "";
    }

    private String getFavoriteStatus(BankAccount account) {
        return isFavoriteAccount(account) ? " | FAVORITE" : "";
    }

    public List<BankAccount> getAccounts() {
        return accounts;
    }

    private void initializeCategories() {
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

    public void setCategoryLimit(String category, double limit) {
        if(category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        if(!categories.contains(category)) {
            throw new IllegalArgumentException("Category does not exist.");
        }
        if(limit <=0){
            throw new IllegalArgumentException("Limit must be greater than 0.");
        }
        categoryLimits.put(category, limit);
    }

    public Double getCategoryLimit(String category) {
        return categoryLimits.get(category);
    }

    public Map<String, Double> getAllCategoryLimits() {
        return categoryLimits;
    }
}
}
