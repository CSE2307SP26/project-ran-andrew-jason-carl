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
}
