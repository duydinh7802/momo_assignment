package model;

import constant.EBillState;
import java.time.LocalDate;

public class Bill {
    private static int counter = 1;

    private final int id;
    private String type;
    private long amount;
    private LocalDate dueDate;
    private EBillState state;
    private String provider;

    public Bill(String type, long amount, LocalDate dueDate, String provider) {
        this.id = counter++;
        this.type = type;
        this.amount = amount;
        this.dueDate = dueDate;
        this.state = EBillState.NOT_PAID;
        this.provider = provider;
    }

    public static void resetCounter() {
        counter = 1;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public long getAmount() { return amount; }
    public LocalDate getDueDate() { return dueDate; }
    public EBillState getState() { return state; }
    public String getProvider() { return provider; }

    public void setType(String type) { this.type = type; }
    public void setAmount(long amount) { this.amount = amount; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setState(EBillState state) { this.state = state; }
    public void setProvider(String provider) { this.provider = provider; }
}
