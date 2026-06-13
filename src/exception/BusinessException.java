package exception;

import constant.EErrorCode;

public class BusinessException extends RuntimeException {
    private final EErrorCode errorCode;

    public BusinessException(EErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(EErrorCode errorCode, String detail) {
        super(detail);
        this.errorCode = errorCode;
    }

    public EErrorCode getErrorCode() { return errorCode; }
}
