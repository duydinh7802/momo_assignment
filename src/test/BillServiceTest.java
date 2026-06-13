package test;

import exception.BusinessException;
import java.time.LocalDate;
import java.util.Optional;
import model.Bill;
import service.BillService;

public class BillServiceTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        Bill.resetCounter();
        testCreateBillAddsToList();
        Bill.resetCounter();
        testFindByIdFound();
        Bill.resetCounter();
        testFindByIdNotFound();
        Bill.resetCounter();
        printSummary("BillServiceTest");
    }

    static void testCreateBillAddsToList() {
        BillService svc = new BillService();
        svc.createBill("ELECTRIC", 200000, LocalDate.of(2026, 06, 25), "EVN");
        assertEqual("createBill adds to list", 1, svc.listAllBills().size());
    }

    static void testFindByIdFound() {
        BillService svc = new BillService();
        Bill b = svc.createBill("WATER", 100000, LocalDate.of(2026, 06, 30), "LAVIE");
        Optional<Bill> found = svc.findById(b.getId());
        assertTrue("findById found", found.isPresent());
    }

    static void testFindByIdNotFound() {
        BillService svc = new BillService();
        Optional<Bill> found = svc.findById(999);
        assertFalse("findById not found", found.isPresent());
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

    static void assertEqual(String name, int expected, int actual) {
        assertEqual(name, (long) expected, (long) actual);
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
            System.out.println("FAILED: " + name + " — expected BusinessException not thrown");
            failed++;
        } catch (BusinessException e) {
            System.out.println("PASS: " + name + " [" + e.getErrorCode() + "]");
            passed++;
        } catch (Exception e) {
            System.out.println("FAILED: " + name + " — unexpected " + e.getClass().getSimpleName());
            failed++;
        }
    }

    static void printSummary(String suiteName) {
        System.out.println(suiteName + ": " + passed + " passed, " + failed + " failed.");
    }
}
