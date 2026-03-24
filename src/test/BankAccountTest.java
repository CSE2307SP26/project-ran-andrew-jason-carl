package test;

import main.BankAccount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

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
            //do nothing, test passes
        }

        try {
            testAccount.deposit(0);
            fail();
        } catch (IllegalArgumentException e) {
            //do nothing, test passes
        }
    }

    @Test
    public void testTransactionHistory() {
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        testAccount.deposit(25);
        assertEquals("Deposit: 50.0\nDeposit: 25.0\n", testAccount.getTransactionHistory());

        BankAccount account2 = new BankAccount();
        assertEquals("", account2.getTransactionHistory());

        BankAccount account3 = new BankAccount();
        account3.deposit(100);
        account3.deposit(200);
        assertEquals("Deposit: 100.0\nDeposit: 200.0\n", account3.getTransactionHistory());
    }
}
