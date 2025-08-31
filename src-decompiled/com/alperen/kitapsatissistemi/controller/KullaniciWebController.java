/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.KullaniciWebController
 *  com.alperen.kitapsatissistemi.dto.ProfilDuzenleRequest
 *  com.alperen.kitapsatissistemi.dto.RegisterRequest
 *  com.alperen.kitapsatissistemi.entity.Kullanici
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.service.FavoriService
 *  com.alperen.kitapsatissistemi.service.KullaniciService
 *  com.alperen.kitapsatissistemi.service.SecurityAuditService
 *  com.alperen.kitapsatissistemi.service.SiparisService
 *  com.alperen.kitapsatissistemi.util.InputSanitizer
 *  com.alperen.kitapsatissistemi.util.SessionUtil
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpSession
 *  javax.validation.Valid
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.security.authentication.UsernamePasswordAuthenticationToken
 *  org.springframework.security.core.Authentication
 *  org.springframework.security.core.authority.SimpleGrantedAuthority
 *  org.springframework.security.core.context.SecurityContextHolder
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.validation.BindingResult
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.ModelAttribute
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.servlet.mvc.support.RedirectAttributes
 */
package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.dto.ProfilDuzenleRequest;
import com.alperen.kitapsatissistemi.dto.RegisterRequest;
import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.service.FavoriService;
import com.alperen.kitapsatissistemi.service.KullaniciService;
import com.alperen.kitapsatissistemi.service.SecurityAuditService;
import com.alperen.kitapsatissistemi.service.SiparisService;
import com.alperen.kitapsatissistemi.util.InputSanitizer;
import com.alperen.kitapsatissistemi.util.SessionUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/kullanici"})
public class KullaniciWebController {
    @Autowired
    private KullaniciService kullaniciService;
    @Autowired
    private SiparisService siparisService;
    @Autowired
    private FavoriService favoriService;
    @Autowired
    private SecurityAuditService securityAuditService;
    @Autowired
    private InputSanitizer inputSanitizer;
    @Autowired
    private SessionUtil sessionUtil;

    @GetMapping(value={"/login"})
    public String login(Model model) {
        model.addAttribute("title", (Object)"Kullan\u0131c\u0131 Giri\u015fi");
        return "kullanici/login";
    }

    @PostMapping(value={"/login"})
    public String login(@RequestParam String email, @RequestParam String sifre, HttpSession session, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        block6: {
            Kullanici kullanici;
            String userAgent;
            String ipAddress;
            block7: {
                block5: {
                    ipAddress = this.securityAuditService.getClientIpAddress(request);
                    userAgent = this.securityAuditService.getUserAgent(request);
                    try {
                        if (this.inputSanitizer.isSafeInput(email) && this.inputSanitizer.isSafeInput(sifre)) break block5;
                        this.securityAuditService.logSuspiciousInput(ipAddress, email, "/kullanici/login", userAgent);
                        redirectAttributes.addFlashAttribute("errorMessage", (Object)"Ge\u00e7ersiz giri\u015f verisi.");
                        return "redirect:/kullanici/login";
                    }
                    catch (Exception e) {
                        redirectAttributes.addFlashAttribute("errorMessage", (Object)("Giri\u015f s\u0131ras\u0131nda bir hata olu\u015ftu: " + e.getMessage()));
                        return "redirect:/kullanici/login";
                    }
                }
                Optional kullaniciOpt = this.kullaniciService.authenticateKullanici(email, sifre);
                if (!kullaniciOpt.isPresent()) break block6;
                kullanici = (Kullanici)kullaniciOpt.get();
                if (!"Admin".equals(kullanici.getRol())) break block7;
                redirectAttributes.addFlashAttribute("errorMessage", (Object)"Admin kullan\u0131c\u0131lar\u0131 i\u00e7in /admin/login sayfas\u0131n\u0131 kullan\u0131n.");
                return "redirect:/kullanici/login";
            }
            this.sessionUtil.updateUserSession(session, kullanici);
            List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken((Object)email, null, authorities);
            SecurityContextHolder.getContext().setAuthentication((Authentication)auth);
            this.securityAuditService.logSuccessfulLogin(email, ipAddress, userAgent);
            return "redirect:/kullanici/dashboard";
        }
        redirectAttributes.addFlashAttribute("errorMessage", (Object)"E-posta veya \u015fifre hatal\u0131.");
        return "redirect:/kullanici/login";
    }

    @GetMapping(value={"/register"})
    public String register(Model model) {
        model.addAttribute("registerRequest", (Object)new RegisterRequest());
        model.addAttribute("title", (Object)"Kullan\u0131c\u0131 Kayd\u0131");
        return "kullanici/register";
    }

    @PostMapping(value={"/register"})
    public String register(@Valid @ModelAttribute RegisterRequest registerRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model, HttpServletRequest request) {
        String userAgent;
        String ipAddress;
        block7: {
            block6: {
                ipAddress = this.securityAuditService.getClientIpAddress(request);
                userAgent = this.securityAuditService.getUserAgent(request);
                if (bindingResult.hasErrors()) {
                    model.addAttribute("title", (Object)"Kullan\u0131c\u0131 Kayd\u0131");
                    return "kullanici/register";
                }
                if (!(this.inputSanitizer.isSafeInput(registerRequest.getAdSoyad()) && this.inputSanitizer.isSafeInput(registerRequest.getEmail()) && this.inputSanitizer.isSafeInput(registerRequest.getSifre()))) {
                    this.securityAuditService.logSuspiciousInput(ipAddress, registerRequest.getEmail(), "/kullanici/register", userAgent);
                    model.addAttribute("errorMessage", (Object)"Ge\u00e7ersiz kay\u0131t verisi.");
                    model.addAttribute("title", (Object)"Kullan\u0131c\u0131 Kayd\u0131");
                    return "kullanici/register";
                }
                try {
                    if (registerRequest.isPasswordMatching()) break block6;
                    model.addAttribute("errorMessage", (Object)"\u015eifreler e\u015fle\u015fmiyor.");
                    model.addAttribute("title", (Object)"Kullan\u0131c\u0131 Kayd\u0131");
                    return "kullanici/register";
                }
                catch (Exception e) {
                    model.addAttribute("errorMessage", (Object)("Kay\u0131t s\u0131ras\u0131nda bir hata olu\u015ftu: " + e.getMessage()));
                    model.addAttribute("title", (Object)"Kullan\u0131c\u0131 Kayd\u0131");
                    return "kullanici/register";
                }
            }
            if (!this.kullaniciService.existsByEmail(registerRequest.getEmail())) break block7;
            model.addAttribute("errorMessage", (Object)"Bu email adresi zaten kay\u0131tl\u0131.");
            model.addAttribute("title", (Object)"Kullan\u0131c\u0131 Kayd\u0131");
            return "kullanici/register";
        }
        Kullanici kullanici = new Kullanici();
        kullanici.setAdSoyad(this.inputSanitizer.sanitizeAlphanumeric(registerRequest.getAdSoyad()));
        kullanici.setEmail(this.inputSanitizer.sanitizeEmail(registerRequest.getEmail()));
        kullanici.setRol("User");
        this.kullaniciService.registerKullanici(kullanici, registerRequest.getSifre());
        this.securityAuditService.logUserRegistration(registerRequest.getEmail(), ipAddress, userAgent);
        redirectAttributes.addFlashAttribute("successMessage", (Object)"Kay\u0131t ba\u015far\u0131l\u0131! \u015eimdi giri\u015f yapabilirsiniz.");
        return "redirect:/kullanici/login";
    }

    @GetMapping(value={"/profil"})
    public String profil(Model model, HttpSession session) {
        block4: {
            String accessCheck = this.sessionUtil.checkUserAccess(session);
            if (accessCheck != null) {
                return accessCheck;
            }
            Long kullaniciId = this.sessionUtil.getCurrentUserId(session);
            try {
                Optional kullaniciOpt = this.kullaniciService.getKullaniciById(kullaniciId);
                if (kullaniciOpt.isPresent()) {
                    model.addAttribute("kullanici", kullaniciOpt.get());
                    model.addAttribute("title", (Object)"Profilim");
                    break block4;
                }
                return "redirect:/kullanici/login";
            }
            catch (Exception e) {
                model.addAttribute("errorMessage", (Object)("Profil bilgileri y\u00fcklenirken hata olu\u015ftu: " + e.getMessage()));
            }
        }
        return "kullanici/profil";
    }

    @GetMapping(value={"/profil-duzenle"})
    public String profilDuzenleForm(Model model, HttpSession session) {
        String accessCheck = this.sessionUtil.checkUserAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        Long kullaniciId = this.sessionUtil.getCurrentUserId(session);
        try {
            Optional kullaniciOpt = this.kullaniciService.getKullaniciById(kullaniciId);
            if (kullaniciOpt.isPresent()) {
                Kullanici kullanici = (Kullanici)kullaniciOpt.get();
                ProfilDuzenleRequest profilRequest = new ProfilDuzenleRequest(kullanici.getAdSoyad(), kullanici.getEmail());
                model.addAttribute("profilRequest", (Object)profilRequest);
                model.addAttribute("title", (Object)"Profil D\u00fczenle");
                return "kullanici/profil-duzenle";
            }
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Profil bilgileri y\u00fcklenirken hata olu\u015ftu: " + e.getMessage()));
        }
        return "redirect:/kullanici/profil";
    }

    @PostMapping(value={"/profil-duzenle"})
    public String profilDuzenleKaydet(@Valid @ModelAttribute(value="profilRequest") ProfilDuzenleRequest profilRequest, BindingResult bindingResult, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String accessCheck = this.sessionUtil.checkUserAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        Long kullaniciId = this.sessionUtil.getCurrentUserId(session);
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", (Object)"Profil D\u00fczenle");
            return "kullanici/profil-duzenle";
        }
        try {
            Kullanici detaylar = new Kullanici();
            detaylar.setAdSoyad(this.inputSanitizer.sanitizeAlphanumeric(profilRequest.getAdSoyad()));
            detaylar.setEmail(this.inputSanitizer.sanitizeEmail(profilRequest.getEmail()));
            this.kullaniciService.updateKullanici(kullaniciId, detaylar);
            Kullanici updatedKullanici = this.kullaniciService.getKullaniciById(kullaniciId).orElse(null);
            if (updatedKullanici != null) {
                this.sessionUtil.updateUserSession(session, updatedKullanici);
            }
            redirectAttributes.addFlashAttribute("successMessage", (Object)"Profil ba\u015far\u0131yla g\u00fcncellendi.");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)("Profil g\u00fcncellenirken hata olu\u015ftu: " + e.getMessage()));
        }
        return "redirect:/kullanici/profil";
    }

    @GetMapping(value={"/dashboard"})
    public String dashboard(Model model, HttpSession session) {
        String accessCheck = this.sessionUtil.checkUserAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        Kullanici kullanici = this.sessionUtil.getCurrentUser(session);
        if (kullanici == null) {
            return "redirect:/kullanici/login";
        }
        model.addAttribute("kullanici", (Object)kullanici);
        model.addAttribute("title", (Object)"Dashboard");
        List siparisler = new ArrayList();
        long toplamSiparis = 0L;
        long aktifSiparis = 0L;
        long tamamlananSiparis = 0L;
        long favoriSayisi = 0L;
        try {
            siparisler = this.siparisService.getSiparislerByKullaniciId(kullanici.getId());
            toplamSiparis = siparisler.size();
            aktifSiparis = siparisler.stream().filter(s -> !s.getDurum().equals("Teslim Edildi") && !s.getDurum().equals("\u0130ptal Edildi")).count();
            tamamlananSiparis = siparisler.stream().filter(s -> s.getDurum().equals("Teslim Edildi")).count();
            favoriSayisi = this.favoriService.getFavoriCountByKullaniciId(kullanici.getId());
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Dashboard y\u00fcklenirken hata olu\u015ftu: " + e.getMessage()));
        }
        model.addAttribute("siparisler", siparisler);
        model.addAttribute("toplamSiparis", (Object)toplamSiparis);
        model.addAttribute("aktifSiparis", (Object)aktifSiparis);
        model.addAttribute("tamamlananSiparis", (Object)tamamlananSiparis);
        model.addAttribute("favoriSayisi", (Object)favoriSayisi);
        return "kullanici/dashboard";
    }

    @GetMapping(value={"/logout"})
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        this.sessionUtil.clearUserSession(session);
        redirectAttributes.addFlashAttribute("successMessage", (Object)"Ba\u015far\u0131yla \u00e7\u0131k\u0131\u015f yapt\u0131n\u0131z.");
        return "redirect:/";
    }

    @GetMapping(value={"/siparislerim"})
    public String siparislerim(Model model, HttpSession session) {
        String accessCheck = this.sessionUtil.checkUserAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        Long kullaniciId = this.sessionUtil.getCurrentUserId(session);
        try {
            List siparisler = this.siparisService.getSiparislerByKullaniciIdWithDetails(kullaniciId);
            model.addAttribute("siparisler", (Object)siparisler);
            model.addAttribute("title", (Object)"Sipari\u015flerim");
        }
        catch (BusinessException e) {
            model.addAttribute("errorMessage", (Object)e.getMessage());
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Sipari\u015fler y\u00fcklenirken hata olu\u015ftu: " + e.getMessage()));
        }
        return "siparisler/index";
    }

    @GetMapping(value={"/siparisler"})
    public String siparisler(Model model, HttpSession session) {
        String accessCheck = this.sessionUtil.checkUserAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        Long kullaniciId = this.sessionUtil.getCurrentUserId(session);
        try {
            List siparisler = this.siparisService.getSiparislerByKullaniciIdWithDetails(kullaniciId);
            model.addAttribute("siparisler", (Object)siparisler);
            model.addAttribute("title", (Object)"Sipari\u015flerim");
        }
        catch (BusinessException e) {
            model.addAttribute("errorMessage", (Object)e.getMessage());
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Sipari\u015fler y\u00fcklenirken hata olu\u015ftu: " + e.getMessage()));
        }
        return "siparisler/index";
    }
}

