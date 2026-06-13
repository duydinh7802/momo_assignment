package test;

import service.AccountService;

public class AccountServiceTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testCashInIncreasesBalance();
        testCashInMultipleTimes();
        testCashInZeroThrows();
        testCashInNegativeThrows();
        printSummary("AccountServiceTest");
    }

    static void testCashInIncreasesBalance() {
        AccountService svc = new AccountService();
        svc.cashIn(500);
        assertEqual("cashIn increases balance", 500L, svc.getBalance());
    }

    static void testCashInMultipleTimes() {
        AccountService svc = new AccountService();
        svc.cashIn(300);
        svc.cashIn(200);
        assertEqual("cashIn multiple times", 500L, svc.getBalance());
    }

    static void testCashInZeroThrows() {
        AccountService svc = new AccountService();
        assertThrows("cashIn zero throws", () -> svc.cashIn(0));
    }

    static void testCashInNegativeThrows() {
        AccountService svc = new AccountService();
        assertThrows("cashIn negative throws", () -> svc.cashIn(-100));
    }

    static void assertEqual(String name, long expected, long actual) {
        if (expected == actual) {
            System.out.println("PASS: " + name);
            passed++;
        } else {
            System.out.println("FAILED: " + name + " — expected " + expected + " but got " + actual);
            failed++;
        }
    }

    static void assertTrue(String name, boolean condition) {
        if (condition) {
            System.out.println("PASS: " + name);
            passed++;
        } else {
            System.out.println("FAILED: " + name);
            failed++;
        }
    }

    static void assertFalse(String name, boolean condition) {
        assertTrue(name, !condition);
    }

    static void assertThrows(String name, Runnable action) {
        try {
            action.run();
            System.out.println("FAILED: " + name + " — expected exception not thrown");
            failed++;
        } catch (Exception e) {
            System.out.println("PASS: " + name);
            passed++;
        }
    }

    static void printSummary(String suiteName) {
        System.out.println(suiteName + ": " + passed + " passed, " + failed + " failed.");
    }
}
