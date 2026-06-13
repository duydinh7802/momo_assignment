package command;

import constant.EErrorCode;
import exception.BusinessException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import model.Bill;
import model.Payment;
import service.AccountService;
import service.BillService;
import service.PaymentService;
import service.SchedulerService;

public class CommandHandler {
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final AccountService accountService;
    private final BillService billService;
    private final PaymentService paymentService;
    private final SchedulerService schedulerService;

    public CommandHandler(AccountService accountService, BillService billService,
                          PaymentService paymentService, SchedulerService schedulerService) {
        this.accountService = accountService;
        this.billService = billService;
        this.paymentService = paymentService;
        this.schedulerService = schedulerService;
    }

    public String handle(String input) {
        if (input == null || input.trim().isEmpty()) return "";
        String[] parts = input.trim().split("\\s+");
        String cmd = parts[0].toUpperCase();

        switch (cmd) {
            case "CASH_IN":                  return cashIn(parts);
            case "CREATE_BILL":              return createBill(parts);
            case "UPDATE_BILL":              return updateBill(parts);
            case "DELETE_BILL":              return deleteBill(parts);
            case "LIST_BILL":                return listBill();
            case "PAY":                      return pay(parts);
            case "DUE_DATE":                 return dueDate();
            case "SCHEDULE":                 return schedule(parts);
            case "LIST_PAYMENT":             return listPayment();
            case "SEARCH_BILL_BY_PROVIDER":  return searchByProvider(parts);
            case "EXIT":                     return "EXIT";
            default:                         throw new BusinessException(EErrorCode.INVALID_COMMAND);
        }
    }

    private String cashIn(String[] parts) {
        if (parts.length < 2) return "Usage: CASH_IN <amount>";
        long amount = Long.parseLong(parts[1]);
        accountService.cashIn(amount);
        return "Your available balance: " + accountService.getBalance();
    }

    private String createBill(String[] parts) {
        if (parts.length < 5) return "Usage: CREATE_BILL <TYPE> <AMOUNT> <DD/MM/YYYY> <PROVIDER>";
        Bill bill = billService.createBill(
                    parts[1],
                    Long.parseLong(parts[2]),
                    LocalDate.parse(parts[3], DATE_FMT),
                    joinFrom(parts, 4));
        return "Bill created with id " + bill.getId() + ".";
    }

    private String updateBill(String[] parts) {
        if (parts.length < 6) return "Usage: UPDATE_BILL <id> <TYPE> <AMOUNT> <DD/MM/YYYY> <PROVIDER>";
        int id = Integer.parseInt(parts[1]);
        billService.updateBill(id,
                parts[2],
                Long.parseLong(parts[3]),
                LocalDate.parse(parts[4], DATE_FMT),
                joinFrom(parts, 5));
        return "Bill id " + id + " updated.";
    }

    private String deleteBill(String[] parts) {
        if (parts.length < 2) return "Usage: DELETE_BILL <id>";
        int id = Integer.parseInt(parts[1]);
        billService.deleteById(id);
        return "Bill id " + id + " deleted.";
    }

    private String listBill() {
        List<Bill> bills = billService.listAllBills();
        if (bills.isEmpty()) return "No bills found.";
        return formatBillTable(bills);
    }

    private String pay(String[] parts) {
        if (parts.length < 2) return "Usage: PAY <id> [id2 ...]";
        if (parts.length == 2) {
            return paymentService.paySingle(Integer.parseInt(parts[1]));
        } else {
            List<Integer> ids = new ArrayList<>();
            for (int i = 1; i < parts.length; i++) ids.add(Integer.parseInt(parts[i]));
            return paymentService.payMultiple(ids);
        }
    }

    private String dueDate() {
        List<Bill> bills = billService.getUnpaidBills();
        if (bills.isEmpty()) return "No unpaid bills.";
        return formatBillTable(bills);
    }

    private String schedule(String[] parts) {
        if (parts.length < 3) return "Usage: SCHEDULE <id> <DD/MM/YYYY>";
        int id = Integer.parseInt(parts[1]);
        LocalDate date = LocalDate.parse(parts[2], DATE_FMT);
        return schedulerService.schedulePayment(id, date);
    }

    private String listPayment() {
        List<Payment> payments = paymentService.listPayments();
        if (payments.isEmpty()) return "No payment records found.";
        return formatPaymentTable(payments);
    }

    private String searchByProvider(String[] parts) {
        if (parts.length < 2) return "Usage: SEARCH_BILL_BY_PROVIDER <PROVIDER>";
        String provider = joinFrom(parts, 1);
        List<Bill> bills = billService.findByProvider(provider);
        if (bills.isEmpty()) return "No bills found for provider: " + provider;
        return formatBillTable(bills);
    }

    private String joinFrom(String[] parts, int from) {
        StringBuilder sb = new StringBuilder();
        for (int i = from; i < parts.length; i++) {
            if (i > from) sb.append(' ');
            sb.append(parts[i]);
        }
        return sb.toString();
    }

    private String formatBillTable(List<Bill> bills) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-8s %-12s %-10s %-12s %-10s %s%n",
                "Bill No.", "Type", "Amount", "Due Date", "State", "PROVIDER"));
        int index = 1;
        for (Bill b : bills) {
            sb.append(String.format("%-8s %-12s %-10d %-12s %-10s %s%n",
                    index++ + ".", b.getType(), b.getAmount(),
                    formatDate(b.getDueDate()), b.getState(), b.getProvider()));
        }
        return sb.toString().trim();
    }

    private String formatPaymentTable(List<Payment> payments) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-6s %-10s %-14s %-12s %s%n",
                "No.", "Amount", "Payment Date", "State", "Bill Id"));
        int index = 1;
        for (Payment p : payments) {
            sb.append(String.format("%-6s %-10d %-14s %-12s %d%n",
                    index++ + ".", p.getAmount(),
                    formatDate(p.getPaymentDate()), p.getState(), p.getBillId()));
        }
        return sb.toString().trim();
    }

    private String formatDate(LocalDate date) {
        return String.format("%02d/%02d/%04d", date.getDayOfMonth(), date.getMonthValue(), date.getYear());
    }
}
