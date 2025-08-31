/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.config.RateLimitingFilter
 *  com.alperen.kitapsatissistemi.config.RateLimitingFilter$RequestCounter
 *  javax.servlet.FilterChain
 *  javax.servlet.ServletException
 *  javax.servlet.ServletRequest
 *  javax.servlet.ServletResponse
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpServletResponse
 *  org.springframework.stereotype.Component
 *  org.springframework.web.filter.OncePerRequestFilter
 */
package com.alperen.kitapsatissistemi.config;

import com.alperen.kitapsatissistemi.config.RateLimitingFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RateLimitingFilter
extends OncePerRequestFilter {
    private volatile boolean enabled = true;
    private volatile int maxRequestsPerMinute = 60;
    private volatile int blockDurationMinutes = 5;
    private final ConcurrentHashMap<String, RequestCounter> requestCounters = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, LocalDateTime> blockedIPs = new ConcurrentHashMap();

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!this.enabled) {
            filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
            return;
        }
        String clientIP = this.getClientIP(request);
        if (request.getRequestURI().startsWith("/admin") || request.getRequestURI().startsWith("/api")) {
            if (this.isBlocked(clientIP)) {
                response.setStatus(429);
                response.getWriter().write("Too many requests. Please try again later.");
                return;
            }
            if (!this.isRequestAllowed(clientIP)) {
                this.blockedIPs.put(clientIP, LocalDateTime.now().plus(this.blockDurationMinutes, ChronoUnit.MINUTES));
                response.setStatus(429);
                response.getWriter().write("Rate limit exceeded. IP blocked for " + this.blockDurationMinutes + " minutes.");
                return;
            }
        }
        filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }
        return request.getRemoteAddr();
    }

    private boolean isBlocked(String ip) {
        LocalDateTime blockTime = (LocalDateTime)this.blockedIPs.get(ip);
        if (blockTime == null) {
            return false;
        }
        if (LocalDateTime.now().isAfter(blockTime)) {
            this.blockedIPs.remove(ip);
            return false;
        }
        return true;
    }

    private boolean isRequestAllowed(String ip) {
        RequestCounter counter = this.requestCounters.computeIfAbsent(ip, k -> new RequestCounter(null));
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneMinuteAgo = now.minus(1L, ChronoUnit.MINUTES);
        counter.cleanup(oneMinuteAgo);
        counter.addRequest(now);
        return counter.getRequestCount() <= this.maxRequestsPerMinute;
    }

    public void clearStats() {
        this.requestCounters.clear();
        this.blockedIPs.clear();
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getMaxRequestsPerMinute() {
        return this.maxRequestsPerMinute;
    }

    public void setMaxRequestsPerMinute(int maxRequestsPerMinute) {
        this.maxRequestsPerMinute = maxRequestsPerMinute;
    }

    public int getBlockDurationMinutes() {
        return this.blockDurationMinutes;
    }

    public void setBlockDurationMinutes(int blockDurationMinutes) {
        this.blockDurationMinutes = blockDurationMinutes;
    }
}

