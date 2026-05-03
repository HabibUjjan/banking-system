package com.bank.bankingsystem.service;

// NotificationService.java

import com.bank.bankingsystem.entity.Transaction;
import com.bank.bankingsystem.entity.User;

public interface NotificationService {
    void sendTransactionNotification(User user, Transaction transaction);
    void sendLoginAlert(User user, String ipAddress);
    void sendRegistrationConfirmation(User user);
    void sendFraudAlert(User user, String message);
}