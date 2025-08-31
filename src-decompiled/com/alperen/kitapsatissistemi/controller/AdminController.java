/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.AdminController
 *  com.alperen.kitapsatissistemi.entity.Kullanici
 *  com.alperen.kitapsatissistemi.service.KategoriService
 *  com.alperen.kitapsatissistemi.service.KitapService
 *  com.alperen.kitapsatissistemi.service.KullaniciService
 *  com.alperen.kitapsatissistemi.service.SettingsService
 *  com.alperen.kitapsatissistemi.service.SiparisService
 *  com.alperen.kitapsatissistemi.util.SessionUtil
 *  javax.servlet.http.HttpSession
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.ModelAttribute
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.servlet.mvc.support.RedirectAttributes
 */
package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.service.KategoriService;
import com.alperen.kitapsatissistemi.service.KitapService;
import com.alperen.kitapsatissistemi.service.KullaniciService;
import com.alperen.kitapsatissistemi.service.SettingsService;
import com.alperen.kitapsatissistemi.service.SiparisService;
import com.alperen.kitapsatissistemi.util.SessionUtil;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/admin"})
public class AdminController {
    @Autowired
    private KullaniciService kullaniciService;
    @Autowired
    private KitapService kitapService;
    @Autowired
    private KategoriService kategoriService;
    @Autowired
    private SiparisService siparisService;
    @Autowired
    private SessionUtil sessionUtil;
    @Autowired
    private SettingsService settingsService;

    @GetMapping(value={"/login"})
    public String loginPage(Model model) {
        model.addAttribute("title", (Object)"Admin Giri\u015fi");
        return "admin/login";
    }

    @GetMapping(value={"/logout"})
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        this.sessionUtil.clearUserSession(session);
        redirectAttributes.addFlashAttribute("message", (Object)"Ba\u015far\u0131yla \u00e7\u0131k\u0131\u015f yap\u0131ld\u0131!");
        return "redirect:/admin/login";
    }

    @GetMapping(value={"/ayarlar"})
    public String ayarlarPage(Model model, HttpSession session) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        model.addAttribute("title", (Object)"G\u00fcvenlik Ayarlar\u0131");
        model.addAttribute("loginRateLimitEnabled", (Object)this.settingsService.isLoginRateLimitEnabled());
        model.addAttribute("registerRateLimitEnabled", (Object)this.settingsService.isRegisterRateLimitEnabled());
        return "admin/ayarlar";
    }

    @PostMapping(value={"/ayarlar/update"})
    public String updateAyarlar(@RequestParam(value="loginRateLimit", required=false) Boolean loginRateLimit, @RequestParam(value="registerRateLimit", required=false) Boolean registerRateLimit, HttpSession session, RedirectAttributes redirectAttributes) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        try {
            this.settingsService.setLoginRateLimitEnabled(loginRateLimit != null && loginRateLimit != false);
            this.settingsService.setRegisterRateLimitEnabled(registerRateLimit != null && registerRateLimit != false);
            redirectAttributes.addFlashAttribute("successMessage", (Object)"G\u00fcvenlik ayarlar\u0131 ba\u015far\u0131yla g\u00fcncellendi.");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)("Ayarlar g\u00fcncellenirken hata olu\u015ftu: " + e.getMessage()));
        }
        return "redirect:/admin/ayarlar";
    }

    @ModelAttribute(value="adminUser")
    public Kullanici getAdminUser(HttpSession session) {
        return this.sessionUtil.getCurrentUser(session);
    }
}

