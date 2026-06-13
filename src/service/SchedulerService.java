package service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SchedulerService {
    private final Map<Integer, LocalDate> schedules = new LinkedHashMap<>();

    private final PaymentService paymentService;

    public SchedulerService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public String schedulePayment(int billId, LocalDate scheduledDate) {
        String result = paymentService.schedule(billId, scheduledDate);
        if (result.startsWith("Payment for bill")) {
            schedules.put(billId, scheduledDate);
        }
        return result;
    }

    public void processScheduledPayments(LocalDate today) {
        paymentService.processScheduledPayments(today);

        schedules.entrySet().removeIf(e -> !e.getValue().isAfter(today));
    }

    public List<String> listSchedules() {
        List<String> result = new ArrayList<>();
        for (Map.Entry<Integer, LocalDate> e : schedules.entrySet()) {
            LocalDate d = e.getValue();
            result.add("Bill id " + e.getKey() + " scheduled on "
                    + String.format("%02d/%02d/%04d", d.getDayOfMonth(), d.getMonthValue(), d.getYear()));
        }
        return result;
    }
}
