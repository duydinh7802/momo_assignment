package test;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("** AccountServiceTest **");
        AccountServiceTest.main(args);
        System.out.println();

        System.out.println("** BillServiceTest **");
        BillServiceTest.main(args);
        System.out.println();

        System.out.println("** PaymentServiceTest **");
        PaymentServiceTest.main(args);
        System.out.println();
    }
}
