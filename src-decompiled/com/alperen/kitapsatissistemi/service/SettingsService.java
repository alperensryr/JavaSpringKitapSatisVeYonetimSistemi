/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.service.SettingsService
 *  org.springframework.stereotype.Service
 */
package com.alperen.kitapsatissistemi.service;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {
    private final ConcurrentHashMap<String, Boolean> settings = new ConcurrentHashMap();

    public SettingsService() {
        this.settings.put("loginRateLimit", true);
        this.settings.put("registerRateLimit", true);
    }

    public boolean isLoginRateLimitEnabled() {
        return this.settings.getOrDefault("loginRateLimit", true);
    }

    public void setLoginRateLimitEnabled(boolean enabled) {
        this.settings.put("loginRateLimit", enabled);
    }

    public boolean isRegisterRateLimitEnabled() {
        return this.settings.getOrDefault("registerRateLimit", true);
    }

    public void setRegisterRateLimitEnabled(boolean enabled) {
        this.settings.put("registerRateLimit", enabled);
    }
}

