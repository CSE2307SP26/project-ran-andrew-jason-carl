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
    //test transferTo works successfully
    @Test
    public void testTransferTo() {
        BankAccount accountA = new BankAccount();
        BankAccount accountB = new BankAccount();
        accountA.deposit(100);
        accountA.transferTo(accountB, 40);
        assertEquals(60, accountA.getBalance(), 0.01);
        assertEquals(40, accountB.getBalance(), 0.01);
    }
    //test if it catches transfering more money than the owner has 
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
    //test if it catches transferring a invalid amount
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
    //test if it catches transferring to an invalid account
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
    //test if it catches transferring to the same bank account
    @Test
    public void testInvalidTransferTo_four(){
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
}
