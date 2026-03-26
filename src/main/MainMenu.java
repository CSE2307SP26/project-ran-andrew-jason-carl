package main;

import java.util.Scanner;

public class MainMenu {

    private static final int EXIT_SELECTION = 2;
	private static final int MAX_SELECTION = 2;

	private Customer user;
    private Scanner keyboardInput;

    public MainMenu() {
        this.user = new Customer("default");

        // make a new account for the customer 
        this.user.addAccount(new BankAccount());
        //create second account for transfer.
        this.user.addAccount(new BankAccount());

        this.keyboardInput = new Scanner(System.in);
    }

    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!");
        
        System.out.println("1. Make a deposit");
        System.out.println("2. Make a transfer");
        System.out.println("3. Make a withdraw");
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
            case 2:
                performTransfer();
            case 3:
                performWithdraw();
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
    public void performWithdraw() {
        double withdrawAmount = -1;
        while(withdrawAmount < 0 || withdrawAmount>user.getAccounts().get(0).getBalance()) {
            System.out.print("How much would you like to withdraw: ");
            withdrawAmount = keyboardInput.nextInt();
        }
        user.getAccounts().get(0).withdraw(withdrawAmount); // withdraw it from the first account only for now.
    }

    public void performTransfer() {
        double transferAmount = -1;
        while(transferAmount < 0 || transferAmount > user.getAccounts().get(0).getBalance()) {
            System.out.print("How much would you like to transfer: ");
            transferAmount = keyboardInput.nextInt();
        }
        user.getAccounts().get(0).transferTo(user.getAccounts().get(1),transferAmount);
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
