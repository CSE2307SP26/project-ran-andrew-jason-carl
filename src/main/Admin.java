package main;

public class Admin extends User {
    private String adminId;

    public Admin(String username, String adminId) {
        super(username);
        this.adminId = adminId;
    }

    public void collectFee(BankAccount account, double fee) {
        
    }

    public void addInterest(BankAccount account, double rate) {
        
    }
}