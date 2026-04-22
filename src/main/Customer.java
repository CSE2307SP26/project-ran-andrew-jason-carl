package main;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Customer extends User {
    private List<BankAccount> accounts;
    private List<String> categories;
    private Map<String, Double> categoryLimits;

    public Customer(String username) {
        super(username, "password");
        this.accounts = new ArrayList<>();
        this.categoryLimits = new HashMap<>();
        initializeCategories();
    }

    public Customer(String username, String password) {
        super(username, password);
        this.accounts = new ArrayList<>();
        this.categoryLimits = new HashMap<>();
        initializeCategories();
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }

    public boolean removeAccount(BankAccount account) {
        return accounts.remove(account);
    }

    public void showAccounts() {
        for (BankAccount account : accounts) {
            System.out.println(account.getAccountName() + ": " + account.getBalance());
        }
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