package test;

import main.BankAccount;
import main.Customer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        testAccount.deposit(25);
        assertEquals("Deposit: 50.0 | Category: No category\nDeposit: 25.0 | Category: No category\n",
                testAccount.getTransactionHistory());

        BankAccount account3 = new BankAccount();
        account3.deposit(100);
        account3.deposit(200);
        assertEquals("Deposit: 100.0 | Category: No category\nDeposit: 200.0 | Category: No category\n",
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
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        assertEquals("Deposit: 50.0 | Category: No category\n", testAccount.getTransactionHistory());
    }

    @Test
    public void testDepositWithCategory() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50, "Food");
        assertEquals("Deposit: 50.0 | Category: Food\n", testAccount.getTransactionHistory());
    }

    @Test
    public void testWithdrawWithNoCategory() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        testAccount.withdraw(30);
        assertEquals("Deposit: 50.0 | Category: No category\nWithdrawal: 30.0 | Category: No category\n",
                testAccount.getTransactionHistory());
    }

    @Test
    public void testWithdrawWithCategory() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        testAccount.withdraw(30, "Entertainment");
        assertEquals("Deposit: 50.0 | Category: No category\nWithdrawal: 30.0 | Category: Entertainment\n",
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
        expected.append("Deposit: 100.0 | Category: Salary\nWithdrawal: 30.0 | Category: Groceries\n");

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
}
