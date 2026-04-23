package test;

import main.BankAccount;
import main.Customer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class CustomerQuickAccessTest {

    @Test
    public void testMarkFavoriteAccount() {
        Customer customer = new Customer("test");
        BankAccount checking = new BankAccount("Checking");

        customer.addAccount(checking);
        customer.markFavoriteAccount(checking);

        assertEquals(true, customer.isFavoriteAccount(checking));
        assertEquals(1, customer.getFavoriteAccounts().size());
    }

    @Test
    public void testMarkFavoriteAccountOnlyAddsAccountOnce() {
        Customer customer = new Customer("test");
        BankAccount checking = new BankAccount("Checking");

        customer.addAccount(checking);
        customer.markFavoriteAccount(checking);
        customer.markFavoriteAccount(checking);

        assertEquals(1, customer.getFavoriteAccounts().size());
    }

    @Test
    public void testSetPrimaryAccount() {
        Customer customer = new Customer("test");
        BankAccount checking = new BankAccount("Checking");

        customer.addAccount(checking);
        customer.setPrimaryAccount(checking);

        assertEquals(checking, customer.getPrimaryAccount());
        assertEquals(true, customer.isPrimaryAccount(checking));
    }

    @Test
    public void testQuickAccessAccountsShowsPrimaryFirst() {
        Customer customer = new Customer("test");
        BankAccount checking = new BankAccount("Checking");
        BankAccount savings = new BankAccount("Savings");

        customer.addAccount(checking);
        customer.addAccount(savings);

        customer.markFavoriteAccount(checking);
        customer.markFavoriteAccount(savings);
        customer.setPrimaryAccount(savings);

        assertEquals(savings, customer.getQuickAccessAccounts().get(0));
        assertEquals(checking, customer.getQuickAccessAccounts().get(1));
    }

    @Test
    public void testRemoveAccountClearsQuickAccessSettings() {
        Customer customer = new Customer("test");
        BankAccount checking = new BankAccount("Checking");

        customer.addAccount(checking);
        customer.markFavoriteAccount(checking);
        customer.setPrimaryAccount(checking);

        customer.removeAccount(checking);

        assertEquals(false, customer.isFavoriteAccount(checking));
        assertEquals(null, customer.getPrimaryAccount());
        assertEquals(0, customer.getQuickAccessAccounts().size());
    }

    @Test
    public void testCannotMarkAnotherCustomersAccountAsQuickAccess() {
        Customer customer = new Customer("test");
        BankAccount otherAccount = new BankAccount("Other");

        try {
            customer.markFavoriteAccount(otherAccount);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }

        try {
            customer.setPrimaryAccount(otherAccount);
            fail();
        } catch (IllegalArgumentException e) {
            // do nothing, test passes
        }
    }
}
