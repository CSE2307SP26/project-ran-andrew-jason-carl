package test;

import main.Customer;

import org.junit.Test;

import main.BankAccount;

public class TestAccountPasswords {
    @Test
    public void testHashPassword() {
        Customer testCustomer = new Customer("testUser", "testPassword");
        String hashedPassword = testCustomer.getPassword();
        String[] passwordParts = hashedPassword.split(":");
        String salt = passwordParts[0];
        String hash = passwordParts[1];

        // check that the salt and hash are not the same as the raw password
        assert(!salt.equals("testPassword"));
        assert(!hash.equals("testPassword"));
    }

    @Test 
    public void testCorrectPassword() {
        Customer t1 = new Customer("edward", "edward");
        assert(t1.comparePasswords("edward"));
    }

    @Test 
    public void testIncorrectPassword() {
        Customer t1 = new Customer("elizabeth", "elizabeth");
        assert(!t1.comparePasswords("eve"));
    }

    @Test 
    public void testComparePassword() {
        testCorrectPassword();
        testIncorrectPassword();
    }

    @Test
    public void testNewPassword(){
        Customer t1 = new Customer("frank", "frank");
        assert(t1.comparePasswords("frank"));

        // change frank's password
        t1.setNewPassword("frank123");
        assert(t1.comparePasswords("frank123"));
    }
}
