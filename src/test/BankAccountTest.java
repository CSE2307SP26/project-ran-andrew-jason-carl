package test;

import main.BankAccount;
import main.Customer;
import main.AccountType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.Test;

public class BankAccountTest {

    @Test
    public void testDeposit() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        assertEquals(50, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testInvalidDeposit() {
        BankAccount testAccount = new BankAccount();
        try {
            testAccount.deposit(-50);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }

        try {
            testAccount.deposit(0);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

    @Test
    public void testTransactionHistory() {
        String d = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        testAccount.deposit(25);
        assertEquals(d + " | Deposit: 50.0 | Category: No category\n" + d + " | Deposit: 25.0 | Category: No category\n",
                testAccount.getTransactionHistory());

        BankAccount account3 = new BankAccount();
        account3.deposit(100);
        account3.deposit(200);
        assertEquals(d + " | Deposit: 100.0 | Category: No category\n" + d + " | Deposit: 200.0 | Category: No category\n",
                account3.getTransactionHistory());

        // test empty account
        BankAccount account4 = new BankAccount();
        assertEquals("There are no transactions yet.\n\n", account4.getTransactionHistory());
    }

    @Test
    public void testCloseAccount() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        assertEquals(false, testAccount.closeAccount());

        BankAccount account2 = new BankAccount();
        assertEquals(true, account2.closeAccount());

        // test closed account
        assertEquals(0.0, account2.getBalance(), 0.01);
    }

    // test transferTo works successfully
    @Test
    public void testTransferTo() {
        BankAccount accountA = new BankAccount();
        BankAccount accountB = new BankAccount();
        accountA.deposit(100);
        accountA.transferTo(accountB, 40);
        assertEquals(60, accountA.getBalance(), 0.01);
        assertEquals(40, accountB.getBalance(), 0.01);
    }

    // test if it catches transfering more money than the owner has
    @Test
    public void testInvalidTransferTo_one() {
        BankAccount accountA = new BankAccount();
        BankAccount accountB = new BankAccount();

        accountA.deposit(50);

        try {
            // Trying to transfer more than the balance of 50
            accountA.transferTo(accountB, 60);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

    // test if it catches transferring a invalid amount
    @Test
    public void testInvalidTransferTo_two() {
        BankAccount accountA = new BankAccount();
        BankAccount accountB = new BankAccount();

        accountA.deposit(50);

        try {
            accountA.transferTo(accountB, -20);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

    // test if it catches transferring to an invalid account
    @Test
    public void testInvalidTransferTo_three() {
        BankAccount accountA = new BankAccount();
        accountA.deposit(50);

        try {
            // Passing null instead of a valid target account
            accountA.transferTo(null, 20);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

    // test if it catches transferring to the same bank account
    @Test
    public void testInvalidTransferTo_four() {
        BankAccount accountA = new BankAccount();
        accountA.deposit(50);

        try {
            // Trying to transfer to itself
            accountA.transferTo(accountA, 20);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

    // test if withdraw works
    @Test
    public void testWithdraw() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        testAccount.withdraw(30);
        assertEquals(20, testAccount.getBalance(), 0.01);
    }

    // test if it catches negative withdraw
    @Test
    public void testInvalidWithdraw_one() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        try {
            testAccount.withdraw(-50);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

    // test if it catches user withdrawing more than their deposit
    @Test
    public void testInvalidWithdraw_two() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        try {
            testAccount.withdraw(60);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

    @Test
    public void testGetBalance() {
        BankAccount testAccount = new BankAccount();
        assertEquals(0, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testGetBalanceAfterDeposit() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(100);
        assertEquals(100, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testStartWithNoAccounts() {
        Customer customer = new Customer("test");
        assertEquals(0, customer.getAccounts().size());
    }

    @Test
    public void testMultipleAccounts() {
        Customer customer = new Customer("test");
        customer.addAccount(new BankAccount());
        customer.addAccount(new BankAccount());
        assertEquals(2, customer.getAccounts().size());
    }

    @Test
    public void testRenameAccount() {
        BankAccount testAccount = new BankAccount("Old");
        testAccount.setAccountName("New Name");
        assertEquals("New Name", testAccount.getAccountName());
    }

    @Test
    public void testInvalieAccountName() {
        BankAccount testAccount = new BankAccount("Old");
        try {
            testAccount.setAccountName("");
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
        try {
            testAccount.setAccountName(null);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

    @Test
    // test that renaming account does not change balance
    public void testNewAccountBalance() {
        BankAccount testAccount = new BankAccount("Old");
        testAccount.deposit(50);
        testAccount.setAccountName("New Name");
        assertEquals(50, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testDepositWithNoCategory() {
        String d = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        assertEquals(d + " | Deposit: 50.0 | Category: No category\n", testAccount.getTransactionHistory());
    }

    @Test
    public void testDepositWithCategory() {
        String d = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50, "Food");
        assertEquals(d + " | Deposit: 50.0 | Category: Food\n", testAccount.getTransactionHistory());
    }

    @Test
    public void testWithdrawWithNoCategory() {
        String d = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        testAccount.withdraw(30);
        assertEquals(d + " | Deposit: 50.0 | Category: No category\n" + d + " | Withdrawal: 30.0 | Category: No category\n",
                testAccount.getTransactionHistory());
    }

    @Test
    public void testWithdrawWithCategory() {
        String d = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        testAccount.withdraw(30, "Entertainment");
        assertEquals(d + " | Deposit: 50.0 | Category: No category\n" + d + " | Withdrawal: 30.0 | Category: Entertainment\n",
                testAccount.getTransactionHistory());
    }

    @Test
    public void testAddCategory() {
        Customer testCustomer = new Customer("test");
        testCustomer.addCategory("Insurance");
        assertEquals(6, testCustomer.getCategories().size());
        assertEquals("Insurance", testCustomer.getCategories().get(5));
    }

    @Test
    public void testAddInvalidCategory() {
        Customer testCustomer = new Customer("test");
        try {
            testCustomer.addCategory("");
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
        try {
            testCustomer.addCategory(null);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }

    @Test
    public void testMakeCheckingsAccount() {
        BankAccount checkingAccount = new BankAccount("Checking Account", AccountType.CHECKING);
        assertEquals(AccountType.CHECKING, checkingAccount.getAccountType());
    }

    @Test
    public void testMakeSavingsAccount() {
        BankAccount savingsAccount = new BankAccount("Savings Account", AccountType.SAVINGS);
        assertEquals(AccountType.SAVINGS, savingsAccount.getAccountType());
    }

    // default bank account type should be checking
    @Test
    public void testCreateDefaultAccount() {
        BankAccount defaultAccount = new BankAccount();
        assertEquals(AccountType.CHECKING, defaultAccount.getAccountType());
    }

    @Test
    public void testSavingsAccountMinimumBalance() {
        BankAccount savingsAccount = new BankAccount("Savings Account", AccountType.SAVINGS);
        savingsAccount.deposit(1500);
        boolean withdrawResult = savingsAccount.withdraw(400);
        assertEquals(true, withdrawResult);
        assertEquals(1100, savingsAccount.getBalance(), 0.01);

        // then withdraw over the limit 
        withdrawResult = savingsAccount.withdraw(600);
        assertEquals(false, withdrawResult);
    }

    @Test
    public void testSavingsAccountWithdrawalFee() {
        BankAccount savingsAccount = new BankAccount("Savings Account", AccountType.SAVINGS);
        savingsAccount.deposit(1500);
        // make 6 withdrawals to exceed the fee free limit
        for (int i = 0; i < 6; i++) {
            savingsAccount.withdraw(10);
        }
        // should deduct 2.5 additional fee on top of withdrawal 
        boolean withdrawResult = savingsAccount.withdraw(10);
        assertEquals(true, withdrawResult);
        assertEquals(1427.5, savingsAccount.getBalance(), 0.01);
    }

    @Test
    public void testGenerateBankStatementExists() throws Exception {
        BankAccount testAccount = new BankAccount();
        Path statementPath = testAccount.generateBankStatement("testUser");
        assert (Files.exists(statementPath));
    }

    @Test
    public void testGenerateBankStatementBlank() throws Exception {
        BankAccount testAccount = new BankAccount();
        Path statementPath = testAccount.generateBankStatement("testUser");

        // get today's time
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formattedDate = today.format(formatter);

        // compile expected string with today's date
        StringBuilder expected = new StringBuilder();
        expected.append("Bank Statement\n");
        expected.append("Date: " + formattedDate + "\n");
        expected.append("Account Name: default\n");
        expected.append("Current Balance: 0.0\n");
        expected.append("Transaction History:\n");
        expected.append("There are no transactions yet.");
        expected.append("\n");

        assertEquals(expected.toString(), Files.readString(statementPath));
    }

    @Test
    public void testGenerateBankStatementwithTransactions() throws Exception {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(100, "Salary");
        testAccount.withdraw(30, "Groceries");
        Path statementPath = testAccount.generateBankStatement("testUser");

        // get today's time
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String formattedDate = today.format(formatter);

        // compile expected string with today's date and transactions
        StringBuilder expected = new StringBuilder();
        expected.append("Bank Statement\n");
        expected.append("Date: " + formattedDate + "\n");
        expected.append("Account Name: default\n");
        expected.append("Current Balance: 70.0\n");
        expected.append("Transaction History:\n");
        expected.append(formattedDate + " | Deposit: 100.0 | Category: Salary\n" + formattedDate + " | Withdrawal: 30.0 | Category: Groceries\n");

        // check file exists
        assert (Files.exists(statementPath));

        // check content
        assertEquals(expected.toString(), Files.readString(statementPath));
    }

    @Test
    public void testGenerateBankStatementFileName() throws Exception {
        BankAccount testAccount = new BankAccount();
        Path statementPath = testAccount.generateBankStatement("testUser");

        // get today's time
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String formattedDate = today.format(formatter);

        StringBuilder expectedFileName = new StringBuilder();
        expectedFileName.append("bank_statement_");
        expectedFileName.append(formattedDate);
        expectedFileName.append("_");
        expectedFileName.append("testUser");
        expectedFileName.append("_");
        expectedFileName.append(testAccount.getAccountName());
        expectedFileName.append(".txt");

        assertEquals(expectedFileName.toString(), statementPath.getFileName().toString());
    }

    @Test
    public void testGetTopFiveTransactions(){
        String d = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        BankAccount account = new BankAccount();
        account.deposit(100, "Food");
        account.deposit(30, "Utilities");
        account.deposit(20, "Other");
        account.withdraw(10,"Food");
        account.withdraw(5,"Utilities");
        List<String> topTransactions = account.getTopFiveTransactions();
        assertEquals(5, topTransactions.size());
        assertEquals(d + " | Deposit: 100.0 | Category: Food", topTransactions.get(0));
        assertEquals(d + " | Deposit: 30.0 | Category: Utilities", topTransactions.get(1));
        assertEquals(d + " | Deposit: 20.0 | Category: Other", topTransactions.get(2));
    }

    @Test
    public void testTopTransactionsLessThanFive(){
        String d = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        BankAccount account = new BankAccount();
        account.deposit(100, "Food");
        account.withdraw(10,"Other");
        List<String> topTransactions = account.getTopFiveTransactions();
        assertEquals(2, topTransactions.size());
        assertEquals(d + " | Deposit: 100.0 | Category: Food", topTransactions.get(0));
        assertEquals(d + " | Withdrawal: 10.0 | Category: Other", topTransactions.get(1));
    }

    @Test
    public void testGetTopFiveTransactionsByCategory(){
        String d = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        BankAccount account = new BankAccount();
        account.deposit(500);
        account.deposit(100,"Transportation");
        account.withdraw(30, "Food");
        assertEquals(d + " | Withdrawal: 30.0 | Category: Food", account.getLargestTransactionByCategory("Food"));
        assertEquals("There are no transactions in the category: Utilities", account.getLargestTransactionByCategory("Utilities"));
    }

    @Test
    public void testSpendingSummaryGroupsByCategory() {
        BankAccount account = new BankAccount();
        account.deposit(500);
        account.withdraw(50, "Food");
        account.withdraw(30, "Food");
        account.withdraw(100, "Entertainment");
        java.util.Map<String, Double> summary = account.getSpendingSummary(LocalDate.now().minusDays(7), LocalDate.now());
        assertEquals(80.0, summary.get("Food"), 0.01);
        assertEquals(100.0, summary.get("Entertainment"), 0.01);
    }

    @Test
    public void testSpendingSummaryEmptyWhenNoWithdrawals() {
        BankAccount account = new BankAccount();
        account.deposit(500);
        java.util.Map<String, Double> summary = account.getSpendingSummary(LocalDate.now().minusDays(7), LocalDate.now());
        assertEquals(true, summary.isEmpty());
    }

    @Test
    public void testSpendingSummaryExcludesDeposits() {
        BankAccount account = new BankAccount();
        account.deposit(500, "Food");
        java.util.Map<String, Double> summary = account.getSpendingSummary(LocalDate.now().minusDays(7), LocalDate.now());
        assertEquals(true, summary.isEmpty());
    }

    @Test
    public void testSpendingSummaryFutureDateRangeReturnsEmpty() {
        BankAccount account = new BankAccount();
        account.deposit(500);
        account.withdraw(100, "Food");
        LocalDate future = LocalDate.now().plusDays(10);
        java.util.Map<String, Double> summary = account.getSpendingSummary(future, future.plusDays(30));
        assertEquals(true, summary.isEmpty());
    }


}