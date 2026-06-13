package test;

import exception.BusinessException;
import java.time.LocalDate;
import model.Bill;
import model.Payment;
import service.AccountService;
import service.BillService;
import service.PaymentService;

public class PaymentServiceTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        Bill.resetCounter(); Payment.resetCounter();
        testPaySingleSuccess();
        Bill.resetCounter(); Payment.resetCounter();
        testPayNotFoundSingleBill();
        Bill.resetCounter(); Payment.resetCounter();
        testPayInsufficientFundsSingle();
        printSummary("PaymentServiceTest");
    }

    static AccountService account() {
        AccountService a = new AccountService();
        a.cashIn(1000000);
        return a;
    }

    static void testPaySingleSuccess() {
        AccountService acc = account();
        BillService bills = new BillService();
        PaymentService svc = new PaymentService(acc, bills);
        Bill b = bills.createBill("ELECTRIC", 200000, LocalDate.of(2026, 06, 25), "EVN");
        String result = svc.paySingle(b.getId());
        assertTrue("paySingle success message", result.contains("Payment has been completed for Bill with id"));
        assertEqual("paySingle balance deducted", 800000L, acc.getBalance());
    }

    static void testPayNotFoundSingleBill() {
        PaymentService svc = new PaymentService(account(), new BillService());
        assertThrows("paySingle throws BusinessException on missing bill", () -> svc.paySingle(999));
    }

    static void testPayInsufficientFundsSingle() {
        AccountService acc = new AccountService();
        acc.cashIn(100);
        BillService bills = new BillService();
        bills.createBill("ELECTRIC", 200000, LocalDate.of(2026, 06, 25), "EVN");
        PaymentService svc = new PaymentService(acc, bills);
        assertThrows("paySingle throws BusinessException on insufficient funds", () -> svc.paySingle(1));
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
