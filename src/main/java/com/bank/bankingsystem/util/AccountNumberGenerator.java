package com.bank.bankingsystem.util;

// AccountNumberGenerator.java


import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccountNumberGenerator {
    private static final SecureRandom random = new SecureRandom();
    private static final String BANK_CODE = "100";

    public static String generateAccountNumber() {
        // Format: BANK_CODE(3) + TIMESTAMP(8) + RANDOM(5) = 16 digits
        StringBuilder accountNumber = new StringBuilder();

        // Bank code
        accountNumber.append(BANK_CODE);

        // Timestamp (YYMMDDHH)
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyMMddHH"));
        accountNumber.append(timestamp);

        // Random digits
        for (int i = 0; i < 5; i++) {
            accountNumber.append(random.nextInt(10));
        }

        // Add check digit (Luhn algorithm)
        accountNumber.append(calculateLuhnCheckDigit(accountNumber.toString()));

        return accountNumber.toString();
    }

    private static int calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = true;

        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(number.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        return (10 - (sum % 10)) % 10;
    }
}
