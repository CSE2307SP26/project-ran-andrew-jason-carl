package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import main.BankAccount;
import main.Customer;
import org.junit.Test;

public class ShowAccountTest {
    @Test
    public void testShowAccountsPrintsEachAccountOnItsOwnLine() {
        Customer customer = new Customer("test-user");

        BankAccount checking = new BankAccount("Checking");
        checking.deposit(125.50);
        customer.addAccount(checking);

        BankAccount savings = new BankAccount("Savings");
        savings.deposit(900);
        customer.addAccount(savings);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output));

        try {
            customer.showAccounts();
        } finally {
            System.setOut(originalOut);
        }

        assertEquals("Checking: 125.5 | TYPE: CHECKING\n\nSavings: 900.0 | TYPE: CHECKING\n\n", output.toString());
    }

    @Test
    public void testShowAccountsWithNoAccountsPrintsNothing() {
        Customer customer = new Customer("empty-user");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output));

        try {
            customer.showAccounts();
        } finally {
            System.setOut(originalOut);
        }

        assertEquals("", output.toString());
    }
}
