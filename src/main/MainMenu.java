package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private static final int EXIT_SELECTION = 11;
    private static final int MAX_SELECTION = 11;
    private static final int ACCOUNT_OPTIONS_MAX_SELECTION = 4;  // added admin login option
    private static final int ADMIN_OPTIONS_MAX_SELECTION = 5;

    private ArrayList<Customer> users = new ArrayList<>();
    private int currentUserIndex = -1;
    private Admin admin;
    private boolean isAdminLoggedIn = false;
    private Scanner keyboardInput;
    private boolean isLoggedIn;

    public MainMenu() {
        this.keyboardInput = new Scanner(System.in);

        // pre populate with some other users
        Customer userMary = new Customer("Mary", "mary");
        userMary.addAccount(new BankAccount("Mary's account"));
        userMary.getAccounts().get(0).deposit(200); // prepopulate with some money
        users.add(userMary);

        Customer userCharles = new Customer("Charles", "charles");
        userCharles.addAccount(new BankAccount("Charles' account"));
        userCharles.addAccount(new BankAccount("Charles' savings account", AccountType.SAVINGS));
        userCharles.getAccounts().get(0).deposit(40);
        userCharles.getAccounts().get(1).deposit(10000);
        users.add(userCharles);

        isLoggedIn = false;

        // hardcoded admin account
        admin = new Admin("admin", "admin");
    }

    // this is the method that displays the dashboard for the login page 
    public void displayAccountOptions() {
        System.out.println("Welcome to the 237 Bank App!");

        System.out.println("1. Log in");
        System.out.println("2. Create an account");
        System.out.println("3. Admin login");
        System.out.println("4. Exit the app");
    }

    // the original dashboard to be displayed once the user logs in 
    public void displayOptions() {
        System.out.println("Welcome to the 237 Bank App!");

        System.out.println("Current user: " + users.get(currentUserIndex).getUsername());
        System.out.println("1. Make a deposit");
        System.out.println("2. View account balance");
        System.out.println("3. Create a new account");
        System.out.println("4. Make a transfer");
        System.out.println("5. Make a withdraw (password required)");
        System.out.println("6. View transaction history");
        System.out.println("7. Switch accounts");
        System.out.println("8. Change password");
        System.out.println("9. Show Accounts");
        System.out.println("10. Change account name");
        System.out.println("11. Exit the app");
        System.out.println();
    }

    public int getUserSelection(int max) {
        int selection = -1;
        while (selection < 1 || selection > max) {
            System.out.print("Please make a selection: ");
            selection = keyboardInput.nextInt();
        }
        return selection;
    }

    // this is the process input for the login page 
    public void processAccountInput(int selection) {
        switch (selection) {
            case 1:
                performLogin();
                break;
            case 2:
                performCreateAccount();
                break;
            case 3:
                performAdminLogin();
                break;
            case 4:
                System.out.println("Thank you for using the 237 Bank App!");
                System.exit(0);
                break;
        }
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
                performCreateBankAccount();
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
            case 7:
                performSwitchAccounts();
                break;
            case 8:
                performChangePassword();
                break;
            case 9:
                performShowAccounts();
                break;
            
            case 10: 
              performRenameAccount();
              break;
            
            case 11:
                System.out.println("Thank you for using the 237 Bank App!");
                System.exit(0);
                break;
        }
    }

    // change password method 
    public void performChangePassword() {
        System.out.print("Enter your current password: ");
        String currentPassword = keyboardInput.next();

        if (users.get(currentUserIndex).comparePasswords(currentPassword)) {
            System.out.print("Enter your new password: ");
            String newPassword = keyboardInput.next();
            users.get(currentUserIndex).setNewPassword(newPassword);
            System.out.println("Password changed successfully!\n\n");
        } else {
            System.out.println("Incorrect password. Please try again.\n\n");
        }
    }

    public void performLogin() {
        System.out.print("Enter username: ");
        String username = keyboardInput.next();

        System.out.print("Enter password: ");
        String password = keyboardInput.next();

        // check password against all users to determine which user to log in 
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i) != null && users.get(i).getUsername().equals(username)
                    && users.get(i).comparePasswords(password)) {
                currentUserIndex = i;
                isLoggedIn = true;
                System.out.println("Login successful!\n\n");
                return;
            }
        }
        System.out.println("Login failed. Please check your username and password and try again.\n\n");
    }

    public void performDeposit() {
        double depositAmount = -1;
        while (depositAmount < 0) {
            System.out.print("How much would you like to deposit: ");
            depositAmount = keyboardInput.nextInt();
        }

        int account = selectAccount();
        String category = selectCategory();

        users.get(currentUserIndex).getAccounts().get(account).deposit(depositAmount, category);
        System.out.println("Deposit successful. Deposited $ " + depositAmount + " | Category: " + category + "\n");
    }

    // switch accounts, logs out the current user and returns them to the login page 
    public void performSwitchAccounts() {
        currentUserIndex = -1;
        isLoggedIn = false;
        System.out.println("You have been logged out. Please log in to switch accounts.\n");
    }

    public void performCheckBalance() {
        int account = selectAccount();
        System.out.println(
                "Your current balance is: $ " + users.get(currentUserIndex).getAccounts().get(account).getBalance());

        System.out.println();
    }

    // this method creates a new user account with a default bank account
    public void performCreateAccount() {
        System.out.println("Enter name for the new account");
        String accountName = keyboardInput.next();

        System.out.println("Enter password for the new account: ");
        String accountPassword = keyboardInput.next();

        // create a new user
        Customer newUser = new Customer(accountName, accountPassword);

        // add a bank account for them
        // creates a default checking account 
        newUser.addAccount(new BankAccount(accountName));

        users.add(newUser);

        System.out.println("New account created with the name: " + accountName);
        System.out.println("Account created! Please log in.\n\n");
    }

    // this method creates a bank account inside a specific user account
    public void performCreateBankAccount() {
        System.out.println("Enter name for the new account");
        String accountName = keyboardInput.next();

        System.out.println("Checking or Savings? [1/2]");
        int accountTypeSelection = getUserSelection(2);
        AccountType accountType = accountTypeSelection == 1 ? AccountType.CHECKING : AccountType.SAVINGS;

        users.get(currentUserIndex).addAccount(new BankAccount(accountName, accountType));

        System.out.println("New account created with the name: " + accountName + "\n");
    }

    public void performWithdraw() {
        int account = selectAccount();
        BankAccount selectedAccount = users.get(currentUserIndex).getAccounts().get(account);

        System.out.print("Enter your password to confirm withdrawal: ");
        String password = keyboardInput.next();

        if (!users.get(currentUserIndex).comparePasswords(password)) {
            System.out.println("Incorrect password. Withdrawal cancelled.\n\n");
            return;
        }

        double withdrawAmount = -1;
        while (withdrawAmount <= 0 || withdrawAmount > selectedAccount.getBalance()) {
            System.out.print("How much would you like to withdraw: ");
            withdrawAmount = keyboardInput.nextInt();
        }
        String category = selectCategory();
        boolean withdrawResult = users.get(currentUserIndex).getAccounts().get(account).withdraw(withdrawAmount, category); // withdraw it from the selected account only for now
        if (withdrawResult) {
            System.out.println("Withdrawal successful. Withdrew $ " + withdrawAmount + " | Category: " + category + "\n");
        } else {
            System.out.println("Withdrawal failed.\n");
        }
    }

    public void performTransfer() {
        // make user pick account first
        int fromAccountIndex = selectAccount();
        BankAccount fromAccount = users.get(currentUserIndex).getAccounts().get(fromAccountIndex);

        double transferAmount = -1;
        while (transferAmount < 0 || transferAmount > users.get(currentUserIndex).getAccounts().get(0).getBalance()) {
            System.out.print("How much would you like to transfer: ");
            transferAmount = keyboardInput.nextInt();
        }

        // make them select an account to transfer to 
        BankAccount toAccount = selectAnyAccount();

        if (toAccount == fromAccount) {
            System.out.println("Cannot transfer to the same account.\n\n");
            return;
        }

        fromAccount.transferTo(toAccount, transferAmount);
        System.out.println("Transfer successful!\n\n");
    }

    public void performViewTransactionHistory() {
        int account = selectAccount();
        System.out.println(users.get(currentUserIndex).getAccounts().get(account).getTransactionHistory());
    }

    public void performCloseAccount() {
        int account = selectAccount();
        if (users.get(currentUserIndex).getAccounts().get(account).closeAccount()) {
            System.out.println("Account closed successfully.");
        } else {
            System.out.println("Account could not be closed. Please make sure your balance is 0.");
        }
    }

    public void performShowAccounts(){
        users.get(currentUserIndex).showAccounts();
        System.out.println();
    }

    // for all accounts in system
    private BankAccount selectAnyAccount() {
        System.out.println("Select destination account:");

        int count = 1;
        ArrayList<BankAccount> allAccounts = new ArrayList<>();

        // loop over and display accounts available 
        for (Customer user : users) {
            for (BankAccount account : user.getAccounts()) {
                System.out.println(count + ". " + account.getAccountName() + " (User: " + user.getUsername() + ")");
                allAccounts.add(account);
                count++;
            }
        }

        int accountSelection = -1;
        while (accountSelection < 1 || accountSelection > allAccounts.size()) {
            System.out.print("Please select an account: ");
            accountSelection = keyboardInput.nextInt();
        }

        return allAccounts.get(accountSelection - 1);
    }

    // for one's own accounts
    private int selectAccount() {
        System.out.println("Select an account: ");

        for (int i = 0; i < users.get(currentUserIndex).getAccounts().size(); i++) {
            System.out.println((i + 1) + ". " + users.get(currentUserIndex).getAccounts().get(i).getAccountName());
        }

        System.out.println();

        int accountSelection = -1;
        while (accountSelection < 1 || accountSelection > users.get(currentUserIndex).getAccounts().size()) {
            System.out.print("Please select an account: ");
            accountSelection = keyboardInput.nextInt();
        }

        return accountSelection - 1;
    }

    

    public void displayAdminOptions() {
        System.out.println("=== Admin Dashboard ===");
        System.out.println("1. View all accounts");
        System.out.println("2. Freeze an account");
        System.out.println("3. Unfreeze an account");
        System.out.println("4. Add interest to an account");
        System.out.println("5. Collect fee from an account");
        System.out.println("6. Logout");
        System.out.println();
    }

    public void processAdminInput(int selection) {
        switch (selection) {
            case 1:
                admin.viewAllAccounts(users);
                break;
            case 2:
                performAdminFreezeAccount();
                break;
            case 3:
                performAdminUnfreezeAccount();
                break;
            case 4:
                performAdminAddInterest();
                break;
            case 5:
                performAdminCollectFee();
                break;
            case 6:
                isAdminLoggedIn = false;
                System.out.println("Admin logged out.\n");
                break;
        }
    }

    public void performAdminLogin() {
        System.out.print("Enter admin username: ");
        String username = keyboardInput.next();
        System.out.print("Enter admin ID: ");
        String adminId = keyboardInput.next();

        if (admin.getUsername().equals(username) && admin.getAdminId().equals(adminId)) {
            isAdminLoggedIn = true;
            System.out.println("Admin login successful!\n");
        } else {
            System.out.println("Invalid admin credentials.\n");
        }
    }

    public void performAdminFreezeAccount() {
        BankAccount account = selectAnyAccount();
        admin.freezeAccount(account);
    }

    public void performAdminUnfreezeAccount() {
        BankAccount account = selectAnyAccount();
        admin.unfreezeAccount(account);
    }

    public void performAdminAddInterest() {
        BankAccount account = selectAnyAccount();
        account.applyInterest();
        System.out.printf("Interest applied at rate %.1f%%.%n%n", account.getInterestRate() * 100);
    }

    public void performAdminCollectFee() {
        BankAccount account = selectAnyAccount();
        System.out.print("Enter fee amount: ");
        double fee = keyboardInput.nextDouble();
        account.withdraw(fee);
        System.out.println("Fee collected.\n");
    }

    public void run() {
        int selection = -1;
        while (true) {
            if (isAdminLoggedIn) {
                displayAdminOptions();
                selection = getUserSelection(6);
                processAdminInput(selection);
            } else if (isLoggedIn) {
                displayOptions();
                selection = getUserSelection(MAX_SELECTION);
                processInput(selection);
            } else {
                displayAccountOptions();
                selection = getUserSelection(ACCOUNT_OPTIONS_MAX_SELECTION);
                processAccountInput(selection);
            }
        }
    }

    public void performRenameAccount() {
        int account = selectAccount();
        keyboardInput.nextLine();
        System.out.print("Enter new name for the account: ");
        String newName = keyboardInput.nextLine();
        if (newName == null || newName.isEmpty()) {
            System.out.println("Invalid account name. Please try again.");
            return;
        }
        users.get(currentUserIndex).getAccounts().get(account).setAccountName(newName);
        System.out.println("Account renamed successfully! New name: " + newName);
    }

    public void displayCategories(){
        System.out.println("Select a category: ");
        List<String> categories = users.get(currentUserIndex).getCategories();
        for(int i = 0; i < users.get(currentUserIndex).getCategories().size(); i++){
            System.out.println((i + 1) + ". " + users.get(currentUserIndex).getCategories().get(i));
        }
        System.out.println((categories.size() + 1) + ". Add new category");
        System.out.println((categories.size() + 2) + ". Skip");
    }

    public String selectCategory(){
        displayCategories();
        List<String> categories = users.get(currentUserIndex).getCategories();
        int categorySelection = -1;
        while (categorySelection < 1 || categorySelection > users.get(currentUserIndex).getCategories().size()+2) {
            System.out.print("Please select a category: ");
            categorySelection = keyboardInput.nextInt();
        }
        if (categorySelection == categories.size()+2){
            return "No category";
        }
        if (categorySelection == categories.size()+1){
            return addCategory();
        }
        return categories.get(categorySelection - 1);
    }

    public String addCategory(){
        keyboardInput.nextLine();
        System.out.print("Enter name for new category: ");
        String newCategory = keyboardInput.nextLine();
        if (newCategory == null || newCategory.isEmpty()) {
            System.out.println("Invalid category name. Please try again.");
            return selectCategory();
        }
        users.get(currentUserIndex).addCategory(newCategory);
        return newCategory;
    }

    public static void main(String[] args) {
        MainMenu bankApp = new MainMenu();
        bankApp.run();
    }
}