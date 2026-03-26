package main;

import java.util.Scanner;

public class MainMenu {

    private static final int EXIT_SELECTION = 7;
	private static final int MAX_SELECTION = 7;

	private Customer user;
    private Scanner keyboardInput;

    public MainMenu() {
        this.user = new Customer("default");

        // make a new account for the customer 
        this.user.addAccount(new BankAccount());
        
        this.keyboardInput = new Scanner(System.in);
    }

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!");
        
        System.out.println("1. Make a deposit");
        System.out.println("2. View account balance");
        System.out.println("3. Create a new account");
        System.out.println("4. Make a transfer");
        System.out.println("5. Make a withdraw");
        System.out.println("6. View transaction history");
        System.out.println("7. Exit the app");
    }

    public int getUserSelection(int max) {
        int selection = -1;
        while(selection < 1 || selection > max) {
            System.out.print("Please make a selection: ");
            selection = keyboardInput.nextInt();
        }
        return selection;
    }

    public void processInput(int selection) {
        switch (selection) {
            case 1:
                performDeposit();
                break;
            case 2:
                performCheckBalance();
                break;
            case 3:
                performCreateAccount();
                break;
            case 4:
                performTransfer();
                break;
            case 5:
                performWithdraw();
                break;
            case 6:
                performViewTransactionHistory();
                break;
        }
    }


    public void performDeposit() {
        double depositAmount = -1;
        while(depositAmount < 0) {
            System.out.print("How much would you like to deposit: ");
            depositAmount = keyboardInput.nextInt();
        }

        int account = selectAccount();

        user.getAccounts().get(account).deposit(depositAmount);
    }

    public void performCheckBalance() {
        int account = selectAccount();
        System.out.println("Your current balance is: $ " + user.getAccounts().get(account).getBalance());
    }   

    public void performCreateAccount(){
        System.out.println("Enter name for the new account");
        String accountName = keyboardInput.next();
        user.addAccount(new BankAccount(accountName));
        System.out.println("New account created with the name: " + accountName);
    }

    public void performWithdraw() {
        double withdrawAmount = -1;
        while(withdrawAmount < 0 || withdrawAmount>user.getAccounts().get(0).getBalance()) {
            System.out.print("How much would you like to withdraw: ");
            withdrawAmount = keyboardInput.nextInt();
        }
        int account = selectAccount();
        user.getAccounts().get(account).withdraw(withdrawAmount); // withdraw it from the selected account only for now.
    }

    public void performTransfer() {
        double transferAmount = -1;
        while(transferAmount < 0 || transferAmount > user.getAccounts().get(0).getBalance()) {
            System.out.print("How much would you like to transfer: ");
            transferAmount = keyboardInput.nextInt();
        }
        int fromAccount = selectAccount();
        int toAccount = selectAccount();
        user.getAccounts().get(fromAccount).transferTo(user.getAccounts().get(toAccount), transferAmount);
    }

    public void performViewTransactionHistory() {
        int account = selectAccount();
        System.out.println(user.getAccounts().get(account).getTransactionHistory());
    }
  
    public void performCloseAccount() {
        int account = selectAccount();
        if(user.getAccounts().get(account).closeAccount()){
            System.out.println("Account closed successfully.");
        } else {
            System.out.println("Account could not be closed. Please make sure your balance is 0.");
        }
    }

    private int selectAccount() {
        System.out.println("Select an account:");

        for(int i = 0; i < user.getAccounts().size(); i++) {
            System.out.println((i + 1) + ". " + user.getAccounts().get(i).getAccountName());
        }

        int accountSelection = -1;
        while(accountSelection < 1 || accountSelection > user.getAccounts().size()) {
            System.out.print("Please select an account: ");
            accountSelection = keyboardInput.nextInt();
        }

        return accountSelection - 1;
    }

    public void run() {
        int selection = -1;
        while(selection != EXIT_SELECTION) {
            displayOptions();
            selection = getUserSelection(MAX_SELECTION);
            processInput(selection);
        }
    }

    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }
}
