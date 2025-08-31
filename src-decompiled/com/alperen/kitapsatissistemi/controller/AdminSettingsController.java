/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.config.RateLimitingFilter
 *  com.alperen.kitapsatissistemi.controller.AdminSettingsController
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.security.access.prepost.PreAuthorize
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.servlet.mvc.support.RedirectAttributes
 */
package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.config.RateLimitingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/admin/settings"})
@PreAuthorize(value="hasRole('ADMIN')")
public class AdminSettingsController {
    @Autowired
    private RateLimitingFilter rateLimitingFilter;

    @GetMapping
    public String showSettings(Model model) {
        model.addAttribute("rateLimitEnabled", (Object)this.rateLimitingFilter.isEnabled());
        model.addAttribute("maxRequestsPerMinute", (Object)this.rateLimitingFilter.getMaxRequestsPerMinute());
        model.addAttribute("blockDurationMinutes", (Object)this.rateLimitingFilter.getBlockDurationMinutes());
        return "admin/settings";
    }

    @PostMapping(value={"/rate-limit"})
    public String updateRateLimit(@RequestParam(value="enabled") boolean enabled, @RequestParam(value="maxRequestsPerMinute") int maxRequestsPerMinute, @RequestParam(value="blockDurationMinutes") int blockDurationMinutes, RedirectAttributes redirectAttributes) {
        try {
            this.rateLimitingFilter.setEnabled(enabled);
            this.rateLimitingFilter.setMaxRequestsPerMinute(maxRequestsPerMinute);
            this.rateLimitingFilter.setBlockDurationMinutes(blockDurationMinutes);
            redirectAttributes.addFlashAttribute("success", (Object)"Rate limiting ayarlar\u0131 ba\u015far\u0131yla g\u00fcncellendi.");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", (Object)("Ayarlar g\u00fcncellenirken bir hata olu\u015ftu: " + e.getMessage()));
        }
        return "redirect:/admin/settings";
    }

    @PostMapping(value={"/disable-protection"})
    public String disableProtection(RedirectAttributes redirectAttributes) {
        try {
            this.rateLimitingFilter.setEnabled(false);
            redirectAttributes.addFlashAttribute("warning", (Object)"G\u00fcvenlik korumas\u0131 ge\u00e7ici olarak devre d\u0131\u015f\u0131 b\u0131rak\u0131ld\u0131. Geli\u015ftirme tamamland\u0131ktan sonra tekrar etkinle\u015ftirin.");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", (Object)("Koruma devre d\u0131\u015f\u0131 b\u0131rak\u0131l\u0131rken bir hata olu\u015ftu: " + e.getMessage()));
        }
        return "redirect:/admin/settings";
    }

    @PostMapping(value={"/clear-stats"})
    public String clearStats(RedirectAttributes redirectAttributes) {
        try {
            this.rateLimitingFilter.clearStats();
            redirectAttributes.addFlashAttribute("success", (Object)"Rate limiting istatistikleri temizlendi.");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", (Object)("\u0130statistikler temizlenirken bir hata olu\u015ftu: " + e.getMessage()));
        }
        return "redirect:/admin/settings";
    }
}

