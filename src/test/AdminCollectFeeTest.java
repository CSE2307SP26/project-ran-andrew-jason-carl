package test;

import main.BankAccount;
import main.Admin;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


public class AdminCollectFeeTest {

    private Admin admin;
    private BankAccount account;

    @BeforeEach
    public void setUp() {
        admin = new Admin("admin_test", "1");
        account = new BankAccount();
    }

    @Test
    public void testCollectFeeReducesBalance() {
        account.deposit(200);
        admin.collectFee(account, 25);
        assertEquals(175, account.getBalance(), 0.01);
    }

    @Test
    public void testCollectFeeExceedsBalance() {
        account.deposit(30);
        assertThrows(IllegalArgumentException.class, () -> {
            admin.collectFee(account, 50);
        });
    }

    @Test
    public void testCollectFeeOnEmptyAccount() {
        assertThrows(IllegalArgumentException.class, () -> {
            admin.collectFee(account, 25);
        });
    }

    @Test
    public void testCollectNegativeFee() {
        account.deposit(100);
        assertThrows(IllegalArgumentException.class, () -> {
            admin.collectFee(account, -10);
        });
    }

    @Test
    public void testCollectFeeMultipleTimes() {
        account.deposit(300);
        admin.collectFee(account, 25);
        admin.collectFee(account, 25);
        admin.collectFee(account, 25);
        assertEquals(225, account.getBalance(), 0.01);
    }

    @Test
    public void testBalanceUnchangedAfterFailedFee() {
        account.deposit(30);
        assertThrows(IllegalArgumentException.class, () -> {
            admin.collectFee(account, 50);
        });
        assertEquals(30, account.getBalance(), 0.01);
    }
}