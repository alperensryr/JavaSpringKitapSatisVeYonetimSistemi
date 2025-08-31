/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Kullanici
 *  com.alperen.kitapsatissistemi.service.KullaniciService
 *  com.alperen.kitapsatissistemi.util.SessionUtil
 *  javax.servlet.http.HttpSession
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Component
 */
package com.alperen.kitapsatissistemi.util;

import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.service.KullaniciService;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SessionUtil {
    @Autowired
    private KullaniciService kullaniciService;

    public boolean isUserLoggedIn(HttpSession session) {
        Boolean isLoggedIn = (Boolean)session.getAttribute("IsLoggedIn");
        String kullaniciEmail = (String)session.getAttribute("KullaniciEmail");
        Long kullaniciId = (Long)session.getAttribute("KullaniciId");
        return isLoggedIn != null && isLoggedIn != false && kullaniciEmail != null && kullaniciId != null;
    }

    public boolean isAdmin(HttpSession session) {
        if (!this.isUserLoggedIn(session)) {
            return false;
        }
        String kullaniciEmail = (String)session.getAttribute("KullaniciEmail");
        String kullaniciRol = (String)session.getAttribute("KullaniciRol");
        if ("Admin".equals(kullaniciRol)) {
            return true;
        }
        try {
            Optional kullaniciOpt = this.kullaniciService.getKullaniciByEmail(kullaniciEmail);
            if (kullaniciOpt.isPresent()) {
                Kullanici kullanici = (Kullanici)kullaniciOpt.get();
                session.setAttribute("KullaniciRol", (Object)kullanici.getRol());
                session.setAttribute("IsAdmin", (Object)"Admin".equals(kullanici.getRol()));
                return "Admin".equals(kullanici.getRol());
            }
        }
        catch (Exception e) {
            return false;
        }
        return false;
    }

    public Kullanici getCurrentUser(HttpSession session) {
        if (!this.isUserLoggedIn(session)) {
            return null;
        }
        String kullaniciEmail = (String)session.getAttribute("KullaniciEmail");
        try {
            Optional kullaniciOpt = this.kullaniciService.getKullaniciByEmail(kullaniciEmail);
            return kullaniciOpt.orElse(null);
        }
        catch (Exception e) {
            return null;
        }
    }

    public Long getCurrentUserId(HttpSession session) {
        return (Long)session.getAttribute("KullaniciId");
    }

    public String getCurrentUserEmail(HttpSession session) {
        return (String)session.getAttribute("KullaniciEmail");
    }

    public String getCurrentUserName(HttpSession session) {
        return (String)session.getAttribute("KullaniciAd");
    }

    public void updateUserSession(HttpSession session, Kullanici kullanici) {
        session.setAttribute("KullaniciId", (Object)kullanici.getId());
        session.setAttribute("KullaniciEmail", (Object)kullanici.getEmail());
        session.setAttribute("KullaniciAd", (Object)kullanici.getAdSoyad());
        session.setAttribute("KullaniciRol", (Object)kullanici.getRol());
        session.setAttribute("IsLoggedIn", (Object)true);
        session.setAttribute("IsAdmin", (Object)"Admin".equals(kullanici.getRol()));
    }

    public void clearUserSession(HttpSession session) {
        session.removeAttribute("KullaniciId");
        session.removeAttribute("KullaniciEmail");
        session.removeAttribute("KullaniciAd");
        session.removeAttribute("KullaniciRol");
        session.removeAttribute("IsLoggedIn");
        session.removeAttribute("IsAdmin");
        session.removeAttribute("sepetCount");
    }

    public String checkAdminAccess(HttpSession session) {
        if (!this.isAdmin(session)) {
            return "redirect:/admin/login";
        }
        return null;
    }

    public String checkUserAccess(HttpSession session) {
        if (!this.isUserLoggedIn(session)) {
            return "redirect:/kullanici/login";
        }
        return null;
    }
}

