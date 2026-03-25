package test;

import main.BankAccount;
import main.Customer;

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
    public void testGetBalance(){
        BankAccount testAccount = new BankAccount();
        assertEquals(0, testAccount.getBalance(), 0.01);
    }

    @Test 
    public void testGetBalanceAfterDeposit(){
        BankAccount testAccount = new BankAccount();
        testAccount.deposit(100);
        assertEquals(100, testAccount.getBalance(), 0.01);
    }

    @Test
    public void testStartWithNoAccounts(){
        Customer customer = new Customer("test");
        assertEquals(0, customer.getAccounts().size());
    }

    @Test
    public void testMultipleAccounts(){
        Customer customer = new Customer("test");
        customer.addAccount(new BankAccount());
        customer.addAccount(new BankAccount());
        assertEquals(2, customer.getAccounts().size());
    }


}
