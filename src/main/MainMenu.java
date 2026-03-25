package main;

import java.util.Scanner;

public class MainMenu {

    private static final int EXIT_SELECTION = 4;
	private static final int MAX_SELECTION = 4;

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
        System.out.println("4. Exit the app");
        
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
        }
    }

    public void performDeposit() {
        double depositAmount = -1;
        while(depositAmount < 0) {
            System.out.print("How much would you like to deposit: ");
            depositAmount = keyboardInput.nextInt();
        }
        user.getAccounts().get(0).deposit(depositAmount); // deposit it into the first account only for now.
    }

    public void performCheckBalance() {
        System.out.println("Your current balance is: $ " + user.getAccounts().get(0).getBalance());
    }   

    public void run() {
        int selection = -1;
        while(selection != EXIT_SELECTION) {
            displayOptions();
            selection = getUserSelection(MAX_SELECTION);
            processInput(selection);
        }
    }

    public void performCreateAccount(){
        System.out.println("Enter name for the new account");
        String accountName = keyboardInput.next();
        user.addAccount(new BankAccount(accountName));
        System.out.println("New account created with the name: " + accountName);
    }

    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }

}
