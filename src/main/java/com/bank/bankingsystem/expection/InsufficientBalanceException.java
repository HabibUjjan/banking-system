package com.bank.bankingsystem.expection;

// InsufficientBalanceException.java


public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}