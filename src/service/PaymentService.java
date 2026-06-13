package service;

import constant.EBillState;
import constant.EErrorCode;
import constant.EPaymentState;
import exception.BusinessException;
import model.Bill;
import model.Payment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class PaymentService {
    private final List<Payment> payments = new ArrayList<>();
    private final AccountService accountService;
    private final BillService billService;

    public PaymentService(AccountService accountService, BillService billService) {
        this.accountService = accountService;
        this.billService = billService;
    }

    public String paySingle(int billId) {
        Bill bill = billService.getById(billId);
        if (bill.getState() == EBillState.PAID)
            throw new BusinessException(EErrorCode.BILL_ALREADY_PAID, "Bill id " + billId + " is already paid.");
        if (!accountService.checkSufficientFunds(bill.getAmount()))
            throw new BusinessException(EErrorCode.INSUFFICIENT_FUNDS);

        accountService.deduct(bill.getAmount());
        bill.setState(EBillState.PAID);
        payments.add(new Payment(bill.getAmount(), bill.getDueDate(), EPaymentState.PROCESSED, billId));

        return "Payment has been completed for Bill with id " + billId + ".\nYour current balance is: " + accountService.getBalance();
    }

    public String payMultiple(List<Integer> billIds) {
        List<Bill> toBePaid = new ArrayList<>();
        for (int id : billIds) {
            Bill bill = billService.getById(id);
            if (bill.getState() == EBillState.PAID)
                throw new BusinessException(EErrorCode.BILL_ALREADY_PAID, "Bill id " + id + " is already paid.");
            toBePaid.add(bill);
        }

        toBePaid.sort(Comparator.comparing(Bill::getDueDate));

        long total = toBePaid.stream().mapToLong(Bill::getAmount).sum();
        if (!accountService.checkSufficientFunds(total))
            throw new BusinessException(EErrorCode.INSUFFICIENT_FUNDS);

        StringBuilder sb = new StringBuilder();
        for (Bill bill : toBePaid) {
            accountService.deduct(bill.getAmount());
            bill.setState(EBillState.PAID);
            payments.add(new Payment(bill.getAmount(), bill.getDueDate(), EPaymentState.PROCESSED, bill.getId()));
            sb.append("Payment has been completed for Bill with id ").append(bill.getId()).append(".\n");
        }
        sb.append("Your current balance is: ").append(accountService.getBalance());
        return sb.toString();
    }

    public String schedule(int billId, LocalDate scheduledDate) {
        Bill bill = billService.getById(billId);
        if (bill.getState() == EBillState.PAID)
            throw new BusinessException(EErrorCode.BILL_ALREADY_PAID, "Bill id " + billId + " is already paid.");

        boolean alreadyScheduled = payments.stream()
                .anyMatch(p -> p.getBillId() == billId && p.getState() == EPaymentState.PENDING);
        if (alreadyScheduled)
            throw new BusinessException(EErrorCode.BILL_ALREADY_SCHEDULED, "Bill id " + billId + " is already scheduled.");

        payments.add(new Payment(bill.getAmount(), scheduledDate, EPaymentState.PENDING, billId));
        return "Payment for bill id " + billId + " is scheduled on " + formatDate(scheduledDate);
    }

    public void processScheduledPayments(LocalDate today) {
        payments.stream()
                .filter(p -> p.getState() == EPaymentState.PENDING && !p.getPaymentDate().isAfter(today))
                .sorted(Comparator.comparing(Payment::getPaymentDate))
                .forEach(p -> {
                    Optional<Bill> opt = billService.findById(p.getBillId());
                    if (opt.isPresent()) {
                        Bill bill = opt.get();
                        if (bill.getState() == EBillState.NOT_PAID && accountService.checkSufficientFunds(bill.getAmount())) {
                            accountService.deduct(bill.getAmount());
                            bill.setState(EBillState.PAID);
                            p.setState(EPaymentState.PROCESSED);
                        }
                    }
                });
    }

    public List<Payment> listPayments() {
        return new ArrayList<>(payments);
    }

    private String formatDate(LocalDate date) {
        return String.format("%02d/%02d/%04d", date.getDayOfMonth(), date.getMonthValue(), date.getYear());
    }
}
