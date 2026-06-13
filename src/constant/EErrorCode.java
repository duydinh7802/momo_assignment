package constant;

public enum EErrorCode {
    // system error
    SUCCESS(200, "Success"),
    INVALID_COMMAND(404, "Invalid command"),

    // usecase error
    BILL_NOT_FOUND(404001, "Bill not found"),
    INSUFFICIENT_FUNDS(404002, "Insufficient funds"),
    BILL_ALREADY_PAID(404003, "Bill already paid"),
    BILL_ALREADY_SCHEDULED(404004, "Bill already scheduled"),
    INVALID_AMOUNT(404005, "Amount must be positive");
    

    private final int code;
    private final String message;

    EErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }
}
