package test;

import main.BankAccount;
import main.Customer;
import main.Admin;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class AdminTest {

    private Admin admin;
    private BankAccount account;

    @BeforeEach
    public void setUp() {
        admin = new Admin("admin_test", "1");
        account = new BankAccount("Test Account");
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

    // AdminCollectFeeTest
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

    // freeze tests
    @Test
    public void testFreezeAccountBlocksWithdraw() {
        account.deposit(500);
        admin.freezeAccount(account);
        // withdraw should silently return on a frozen account
        account.withdraw(100);
        assertEquals(500, account.getBalance(), 0.01);
    }
 
    @Test
    public void testFreezeAccountBlocksDeposit() {
        account.deposit(500);
        admin.freezeAccount(account);
        account.deposit(100);
        assertEquals(500, account.getBalance(), 0.01);
    }
 
    @Test
    public void testFreezeAccountBlocksTransfer() {
        account.deposit(500);
        BankAccount other = new BankAccount("Other Account");
        admin.freezeAccount(account);
        account.transferTo(other, 100);
        assertEquals(500, account.getBalance(), 0.01);
        assertEquals(0, other.getBalance(), 0.01);
    }
 
    @Test
    public void testFreezeAlreadyFrozenAccount() {
        admin.freezeAccount(account);
        assertTrue(account.isFrozen());
        // freezing again should not throw, just print a message
        assertDoesNotThrow(() -> admin.freezeAccount(account));
    }
 
    @Test
    public void testIsFrozenAfterFreeze() {
        assertFalse(account.isFrozen());
        admin.freezeAccount(account);
        assertTrue(account.isFrozen());
    }

    // unfreeze tests
    @Test
    public void testUnfreezeRestoresWithdraw() {
        account.deposit(500);
        admin.freezeAccount(account);
        admin.unfreezeAccount(account);
        account.withdraw(100);
        assertEquals(400, account.getBalance(), 0.01);
    }
 
    @Test
    public void testUnfreezeRestoresDeposit() {
        account.deposit(500);
        admin.freezeAccount(account);
        admin.unfreezeAccount(account);
        account.deposit(100);
        assertEquals(600, account.getBalance(), 0.01);
    }
 
    @Test
    public void testUnfreezeNotFrozenAccount() {
        // unfreezing an account that isn't frozen should not throw
        assertDoesNotThrow(() -> admin.unfreezeAccount(account));
    }
 
    @Test
    public void testIsFrozenAfterUnfreeze() {
        admin.freezeAccount(account);
        admin.unfreezeAccount(account);
        assertFalse(account.isFrozen());
    }

    // viewAllAccounts tests
    @Test
    public void testViewAllAccountsEmptyList() {
        // should not throw on empty list
        assertDoesNotThrow(() -> admin.viewAllAccounts(new ArrayList<>()));
    }
 
    @Test
    public void testViewAllAccountsNullList() {
        assertDoesNotThrow(() -> admin.viewAllAccounts(null));
    }

    @Test
    public void testViewAllAccountsWithCustomers() {
        Customer alice = new Customer("test1", "password");
        alice.addAccount(new BankAccount("test1 Checking"));
        alice.getAccounts().get(0).deposit(1000);
 
        Customer bob = new Customer("test2", "password");
        bob.addAccount(new BankAccount("test2 Savings"));
        bob.getAccounts().get(0).deposit(250);
 
        List<Customer> users = new ArrayList<>();
        users.add(alice);
        users.add(bob);
 
        // should print without errors
        assertDoesNotThrow(() -> admin.viewAllAccounts(users));
    }
 
    @Test
    public void testViewAllAccountsShowsFrozenStatus() {
        Customer customer = new Customer("test1", "password");
        BankAccount frozenAcc = new BankAccount("test1 Frozen");
        frozenAcc.deposit(300);
        admin.freezeAccount(frozenAcc);
        customer.addAccount(frozenAcc);
 
        List<Customer> users = new ArrayList<>();
        users.add(customer);
 
        assertDoesNotThrow(() -> admin.viewAllAccounts(users));
        assertTrue(frozenAcc.isFrozen());
    }


}