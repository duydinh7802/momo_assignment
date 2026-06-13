package service;

import constant.EErrorCode;
import exception.BusinessException;

public class AccountService {
    private long balance;

    public AccountService() {
        this.balance = 0;
    }

    public void cashIn(long amount) {
        if (amount <= 0) throw new BusinessException(EErrorCode.INVALID_AMOUNT);
        balance += amount;
    }

    public boolean checkSufficientFunds(long amount) {
        return balance >= amount;
    }

    public void deduct(long amount) {
        if (amount > balance) throw new BusinessException(EErrorCode.INSUFFICIENT_FUNDS);
        balance -= amount;
    }

    public long getBalance() {
        return balance;
    }
}
