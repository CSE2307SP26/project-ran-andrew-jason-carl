package test;

import main.BankAccount;
import main.Admin;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class AdminAddInterestTest {

    private Admin admin;
    private BankAccount account;

    @BeforeEach
    public void setUp() {
        admin = new Admin("admin_test", "1");
        account = new BankAccount();
    }

    @Test
    public void testAddInterestIncreasesBalance() {
        account.deposit(1000);
        admin.addInterest(account, 0.05);
        assertEquals(1050, account.getBalance(), 0.01);
    }

    @Test
    public void testAddInterestNegativeRate() {
        account.deposit(1000);
        assertThrows(IllegalArgumentException.class, () -> {
            admin.addInterest(account, -0.05);
        });
    }

    @Test
    public void testAddInterestRateAboveOne() {
        account.deposit(1000);
        assertThrows(IllegalArgumentException.class, () -> {
            admin.addInterest(account, 1.5);
        });
    }

    @Test
    public void testAddInterestMultipleTimes() {
        account.deposit(1000);
        admin.addInterest(account, 0.05);
        admin.addInterest(account, 0.05);
        assertEquals(1102.50, account.getBalance(), 0.01);
    }

    @Test
    public void testBalanceUnchangedAfterInvalidRate() {
        account.deposit(1000);
        assertThrows(IllegalArgumentException.class, () -> {
            admin.addInterest(account, -0.05);
        });
        assertEquals(1000, account.getBalance(), 0.01);
    }
}