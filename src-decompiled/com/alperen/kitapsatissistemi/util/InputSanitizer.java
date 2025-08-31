/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.util.InputSanitizer
 *  org.springframework.stereotype.Component
 *  org.springframework.web.util.HtmlUtils
 */
package com.alperen.kitapsatissistemi.util;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

@Component
public class InputSanitizer {
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile("(?i).*(union|select|insert|update|delete|drop|create|alter|exec|execute|script|javascript|vbscript|onload|onerror|onclick).*");
    private static final Pattern XSS_PATTERN = Pattern.compile("(?i).*(<script|</script|javascript:|vbscript:|onload=|onerror=|onclick=|onmouseover=|onfocus=|onblur=).*");

    public String sanitizeInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }
        String sanitized = HtmlUtils.htmlEscape((String)input);
        sanitized = sanitized.replaceAll("[<>\"'%;()&+]", "");
        return sanitized.trim();
    }

    public boolean containsSqlInjection(String input) {
        if (input == null) {
            return false;
        }
        return SQL_INJECTION_PATTERN.matcher(input).matches();
    }

    public boolean containsXss(String input) {
        if (input == null) {
            return false;
        }
        return XSS_PATTERN.matcher(input).matches();
    }

    public boolean isSafeInput(String input) {
        return !this.containsSqlInjection(input) && !this.containsXss(input);
    }

    public String sanitizeEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return email;
        }
        return email.replaceAll("[^a-zA-Z0-9@._\\-]", "").toLowerCase().trim();
    }

    public String sanitizePhoneNumber(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return phone;
        }
        return phone.replaceAll("[^0-9+\\-() ]", "").trim();
    }

    public String sanitizeAlphanumeric(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }
        return input.replaceAll("[^a-zA-Z0-9\u00e7\u011f\u0131\u00f6\u015f\u00fc\u00c7\u011eI\u0130\u00d6\u015e\u00dc ]", "").trim();
    }

    public String sanitizePrice(String price) {
        if (price == null || price.trim().isEmpty()) {
            return price;
        }
        return price.replaceAll("[^0-9.]", "").trim();
    }
}

