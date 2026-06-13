package model;

import constant.EPaymentState;
import java.time.LocalDate;

public class Payment {
    private static int counter = 1;

    private final int id;
    private final long amount;
    private final LocalDate paymentDate;
    private EPaymentState state;
    private final int billId;

    public Payment(long amount, LocalDate paymentDate, EPaymentState state, int billId) {
        this.id = counter++;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.state = state;
        this.billId = billId;
    }

    public static void resetCounter() {
        counter = 1;
    }

    public int getId() { return id; }
    public long getAmount() { return amount; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public EPaymentState getState() { return state; }
    public int getBillId() { return billId; }

    public void setState(EPaymentState state) { this.state = state; }
}
