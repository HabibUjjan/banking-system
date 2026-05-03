package com.bank.bankingsystem.service.impl;

// NotificationServiceImpl.java
import com.bank.bankingsystem.entity.Transaction;
import com.bank.bankingsystem.entity.User;
import com.bank.bankingsystem.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void sendTransactionNotification(User user, Transaction transaction) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Transaction Alert - Digital Banking");

            String emailContent = String.format(
                    "Dear %s %s,\n\n" +
                            "A transaction has been processed on your account.\n\n" +
                            "Transaction Details:\n" +
                            "- Type: %s\n" +
                            "- Amount: $%.2f\n" +
                            "- Reference: %s\n" +
                            "- Date: %s\n\n" +
                            "If you did not initiate this transaction, please contact us immediately.\n\n" +
                            "Best regards,\nDigital Banking Team",
                    user.getFirstName(),
                    user.getLastName(),
                    transaction.getTransactionType(),
                    transaction.getAmount(),
                    transaction.getTransactionId(),
                    transaction.getCreatedAt()
            );

            message.setText(emailContent);
            mailSender.send(message);

            log.info("Transaction notification sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send transaction notification: {}", e.getMessage());
        }
    }

    @Override
    @Async
    public void sendLoginAlert(User user, String ipAddress) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Login Alert - Digital Banking");

            String emailContent = String.format(
                    "Dear %s %s,\n\n" +
                            "A new login to your account was detected.\n\n" +
                            "Login Details:\n" +
                            "- IP Address: %s\n" +
                            "- Time: %s\n\n" +
                            "If this wasn't you, please change your password immediately.\n\n" +
                            "Best regards,\nDigital Banking Team",
                    user.getFirstName(),
                    user.getLastName(),
                    ipAddress,
                    java.time.LocalDateTime.now()
            );

            message.setText(emailContent);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send login alert: {}", e.getMessage());
        }
    }

    @Override
    @Async
    public void sendRegistrationConfirmation(User user) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Welcome to Digital Banking");

            String emailContent = String.format(
                    "Dear %s %s,\n\n" +
                            "Welcome to Digital Banking! Your account has been successfully created.\n\n" +
                            "Username: %s\n\n" +
                            "You can now login and start using our services:\n" +
                            "- Create multiple bank accounts\n" +
                            "- Transfer money\n" +
                            "- Pay bills\n" +
                            "- And much more!\n\n" +
                            "Best regards,\nDigital Banking Team",
                    user.getFirstName(),
                    user.getLastName(),
                    user.getUsername()
            );

            message.setText(emailContent);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send registration confirmation: {}", e.getMessage());
        }
    }

    @Override
    @Async
    public void sendFraudAlert(User user, String alertMessage) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("URGENT: Security Alert - Digital Banking");

            String emailContent = String.format(
                    "Dear %s %s,\n\n" +
                            "We detected suspicious activity on your account:\n\n" +
                            "%s\n\n" +
                            "Your account has been temporarily restricted for security purposes.\n" +
                            "Please contact our support team immediately.\n\n" +
                            "Best regards,\nDigital Banking Security Team",
                    user.getFirstName(),
                    user.getLastName(),
                    alertMessage
            );

            message.setText(emailContent);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send fraud alert: {}", e.getMessage());
        }
    }
}