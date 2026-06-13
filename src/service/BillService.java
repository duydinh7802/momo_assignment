package service;

import constant.EBillState;
import constant.EErrorCode;
import exception.BusinessException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import model.Bill;

public class BillService {
    private final List<Bill> bills = new ArrayList<>();

    public Bill createBill(String type, long amount, LocalDate dueDate, String provider) {
        Bill bill = new Bill(type, amount, dueDate, provider);
        bills.add(bill);
        return bill;
    }

    public Optional<Bill> findById(int id) {
        return bills.stream().filter(b -> b.getId() == id).findFirst();
    }

    public Bill getById(int id) {
        return findById(id).orElseThrow(() -> new BusinessException(EErrorCode.BILL_NOT_FOUND));
    }

    public void deleteById(int id) {
        boolean removed = bills.removeIf(b -> b.getId() == id);
        if (!removed) throw new BusinessException(EErrorCode.BILL_NOT_FOUND);
    }

    public void updateBill(int id, String type, long amount, LocalDate dueDate, String provider) {
        Bill bill = getById(id);
        if (type != null) bill.setType(type);
        if (amount > 0) bill.setAmount(amount);
        if (dueDate != null) bill.setDueDate(dueDate);
        if (provider != null) bill.setProvider(provider);
    }

    public List<Bill> listAllBills() {
        return new ArrayList<>(bills);
    }

    public List<Bill> findByProvider(String provider) {
        return bills.stream()
                .filter(b -> b.getProvider().equalsIgnoreCase(provider))
                .collect(Collectors.toList());
    }

    public List<Bill> getUnpaidBills() {
        return bills.stream()
                .filter(b -> b.getState() == EBillState.NOT_PAID)
                .sorted(Comparator.comparing(Bill::getDueDate))
                .collect(Collectors.toList());
    }
}
