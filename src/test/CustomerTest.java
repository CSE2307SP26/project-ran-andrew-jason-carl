package test;

import main.Customer;
import main.BankAccount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import org.junit.Test;

public class CustomerTest {
    
    @Test 
    public void testSetCategoryLimit(){
        Customer customer = new Customer ("test-user", "password");
        customer.setCategoryLimit("Food", 100.0);
        assertEquals (100.0, customer.getCategoryLimit("Food"), 0.01);
    }

    @Test 
    public void testGetCategoryLimitNoLimit(){
        Customer customer = new Customer ("test", "password");
        assertNull(customer.getCategoryLimit("Food"));
    }

    @Test
    public void testSetNegativeLimit(){
        Customer customer = new Customer ("test", "password");
        try {
            customer.setCategoryLimit("Food", -50.0);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing. test passes
        }
    }

    @Test
    public void testSetInvalidCategoryLimit(){
        Customer customer = new Customer ("test", "password");
        try {
            customer.setCategoryLimit("InvalidCategory", 100.0);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing. test passes
        }
    }

    @Test 
    public void testWouldExceedLimit(){
        Customer customer = new Customer ("test", "password");
        BankAccount account = new BankAccount("Test Account");
        account.deposit(100);
        customer.setCategoryLimit("Food", 50.0);    
        account.withdraw(40,"Food");
        assertEquals(true,account.wouldExceedLimit(customer,20,"Food"));
        assertEquals(false,account.wouldExceedLimit(customer,10,"Food"));
    }

    @Test
    public void testCategorySpendingTracked(){
        BankAccount account = new BankAccount("Test Account");
        account.deposit(500);
        account.withdraw(30,"Food");
        account.withdraw(20,"Food");
        assertEquals(50.0, account.getCategorySpending("Food"), 0.01);
    }
}
