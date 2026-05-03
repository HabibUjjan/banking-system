package com.bank.bankingsystem.util;

// AuditLogger.java

import com.bank.bankingsystem.entity.AuditLog;
import com.bank.bankingsystem.entity.User;
import com.bank.bankingsystem.repository.AuditLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuditLogger {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    public void logAction(User user, String action, String entityType,
                          Long entityId, Object oldValue, Object newValue,
                          HttpServletRequest request) {
        try {
            AuditLog auditLog = AuditLog.builder()
                    .user(user)
                    .action(action)
                    .entityType(entityType)
                    .entityId(entityId)
                    .oldValue(oldValue != null ?
                            objectMapper.writeValueAsString(oldValue) : null)
                    .newValue(newValue != null ?
                            objectMapper.writeValueAsString(newValue) : null)
                    .ipAddress(getClientIp(request))
                    .userAgent(request.getHeader("User-Agent"))
                    .build();

            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            // Log but don't throw to avoid disrupting business operations
            System.err.println("Failed to create audit log: " + e.getMessage());
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
