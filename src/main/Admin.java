package main;

public class Admin extends User {
    private String adminId;

    public Admin(String username, String adminId) {
        super(username);
        this.adminId = adminId;
    }

    public void collectFee(BankAccount account, double fee) {
        account.withdraw(fee);
    }

    public void addInterest(BankAccount account, double rate) {
        if (rate < 0 || rate > 1) {
            throw new IllegalArgumentException("Interest rate must be between 0 and 1");
        }
        double interest = account.getBalance() * rate;
        account.deposit(interest);
    }
}