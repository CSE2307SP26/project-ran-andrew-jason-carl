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
    //test if withdraw works
    @Test
    public void testWithdraw(){
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
        testAccount.withdraw(30);
        assertEquals(20, testAccount.getBalance(), 0.01);
    }
    //test if it catches negative withdraw
    @Test
    public void testInvalidWithdraw_one(){
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
         try {
            testAccount.withdraw(-50);
            fail();
        } catch (IllegalArgumentException e) {
            //do nothing, test passes
        }
    }
    //test if it catches user withdrawing more than their deposit
    @Test
    public void testInvalidWithdraw_two(){
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(50);
         try {
            testAccount.withdraw(60);
            fail();
        } catch (IllegalArgumentException e) {
            //do nothing, test passes
        }
    }
}
