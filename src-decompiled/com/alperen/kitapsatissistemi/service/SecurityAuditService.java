/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.service.SecurityAuditService
 *  javax.servlet.http.HttpServletRequest
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.stereotype.Service
 */
package com.alperen.kitapsatissistemi.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SecurityAuditService {
    private static final Logger securityLogger = LoggerFactory.getLogger((String)"SECURITY");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void logSuccessfulLogin(String email, String ipAddress, String userAgent) {
        String message = String.format("[%s] SUCCESSFUL_LOGIN - Email: %s, IP: %s, UserAgent: %s", LocalDateTime.now().format(formatter), email, ipAddress, userAgent);
        securityLogger.info(message);
    }

    public void logFailedLogin(String email, String ipAddress, String userAgent, String reason) {
        String message = String.format("[%s] FAILED_LOGIN - Email: %s, IP: %s, UserAgent: %s, Reason: %s", LocalDateTime.now().format(formatter), email, ipAddress, userAgent, reason);
        securityLogger.warn(message);
    }

    public void logRateLimitViolation(String ipAddress, String endpoint, String userAgent) {
        String message = String.format("[%s] RATE_LIMIT_VIOLATION - IP: %s, Endpoint: %s, UserAgent: %s", LocalDateTime.now().format(formatter), ipAddress, endpoint, userAgent);
        securityLogger.warn(message);
    }

    public void logSuspiciousInput(String ipAddress, String input, String endpoint, String userAgent) {
        String message = String.format("[%s] SUSPICIOUS_INPUT - IP: %s, Input: %s, Endpoint: %s, UserAgent: %s", LocalDateTime.now().format(formatter), ipAddress, this.sanitizeForLog(input), endpoint, userAgent);
        securityLogger.warn(message);
    }

    public void logUnauthorizedAccess(String email, String ipAddress, String endpoint, String userAgent) {
        String message = String.format("[%s] UNAUTHORIZED_ACCESS - Email: %s, IP: %s, Endpoint: %s, UserAgent: %s", LocalDateTime.now().format(formatter), email, ipAddress, endpoint, userAgent);
        securityLogger.warn(message);
    }

    public void logUserRegistration(String email, String ipAddress, String userAgent) {
        String message = String.format("[%s] USER_REGISTRATION - Email: %s, IP: %s, UserAgent: %s", LocalDateTime.now().format(formatter), email, ipAddress, userAgent);
        securityLogger.info(message);
    }

    public void logPasswordChange(String email, String ipAddress, String userAgent) {
        String message = String.format("[%s] PASSWORD_CHANGE - Email: %s, IP: %s, UserAgent: %s", LocalDateTime.now().format(formatter), email, ipAddress, userAgent);
        securityLogger.info(message);
    }

    public void logLogout(String email, String ipAddress, String userAgent) {
        String message = String.format("[%s] LOGOUT - Email: %s, IP: %s, UserAgent: %s", LocalDateTime.now().format(formatter), email, ipAddress, userAgent);
        securityLogger.info(message);
    }

    public String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        return request.getRemoteAddr();
    }

    public String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null ? this.sanitizeForLog(userAgent) : "Unknown";
    }

    private String sanitizeForLog(String input) {
        if (input == null) {
            return "null";
        }
        return input.replaceAll("[\r\n\t]", "_").replaceAll("[<>\"']", "").substring(0, Math.min(input.length(), 200));
    }
}

