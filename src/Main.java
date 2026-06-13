import command.CommandHandler;
import exception.BusinessException;
import java.time.LocalDate;
import java.util.Scanner;
import service.AccountService;
import service.BillService;
import service.PaymentService;
import service.SchedulerService;

public class Main {
    public static void main(String[] args) {
        AccountService accountService = new AccountService();
        BillService billService = new BillService();
        PaymentService paymentService = new PaymentService(accountService, billService);
        SchedulerService schedulerService = new SchedulerService(paymentService);
        CommandHandler handler = new CommandHandler(accountService, billService, paymentService, schedulerService);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Momo Assigment - Payment Service. Type EXIT to quit.");

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            schedulerService.processScheduledPayments(LocalDate.now());
            
            try {
                String result = handler.handle(line);
                if ("EXIT".equals(result)) {
                    System.out.println("Application Stop");
                    break;
                }
                if (!result.isEmpty()) {
                    System.out.println(result);
                }
            } catch (BusinessException be) {
                System.out.println("Error - ErrorCode: " + be.getErrorCode().getCode() + " - ErrorMessage: " + be.getMessage());
            } catch (Exception e) {
                System.out.println("Error - ErrorCode: 500 - ErrorMessage: " + e.getMessage());
            }
            
        }

        scanner.close();
    }
}
