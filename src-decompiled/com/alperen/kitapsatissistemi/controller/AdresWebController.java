/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.AdresWebController
 *  com.alperen.kitapsatissistemi.entity.Adres
 *  com.alperen.kitapsatissistemi.entity.Kullanici
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.service.AdresService
 *  com.alperen.kitapsatissistemi.service.KullaniciService
 *  javax.servlet.http.HttpSession
 *  javax.validation.Valid
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.validation.BindingResult
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.ModelAttribute
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.servlet.mvc.support.RedirectAttributes
 */
package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Adres;
import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.service.AdresService;
import com.alperen.kitapsatissistemi.service.KullaniciService;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/adres"})
public class AdresWebController {
    private final AdresService adresService;
    private final KullaniciService kullaniciService;

    @Autowired
    public AdresWebController(AdresService adresService, KullaniciService kullaniciService) {
        this.adresService = adresService;
        this.kullaniciService = kullaniciService;
    }

    @GetMapping(value={"/liste"})
    public String adresListesi(Model model, HttpSession session) {
        Long kullaniciId = (Long)session.getAttribute("KullaniciId");
        if (kullaniciId == null) {
            return "redirect:/kullanici/login";
        }
        try {
            List adresler = this.adresService.getAdreslerByKullaniciId(kullaniciId);
            model.addAttribute("adresler", (Object)adresler);
            Optional kullanici = this.kullaniciService.getKullaniciById(kullaniciId);
            if (kullanici.isPresent()) {
                model.addAttribute("kullanici", kullanici.get());
            }
            return "kullanici/adres-listesi";
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Adresler y\u00fcklenirken bir hata olu\u015ftu: " + e.getMessage()));
            return "kullanici/adres-listesi";
        }
    }

    @GetMapping(value={"/ekle"})
    public String adresEkleForm(Model model, HttpSession session) {
        Long kullaniciId = (Long)session.getAttribute("KullaniciId");
        if (kullaniciId == null) {
            return "redirect:/kullanici/login";
        }
        model.addAttribute("adres", (Object)new Adres());
        return "kullanici/adres-ekle";
    }

    @PostMapping(value={"/ekle"})
    public String adresEkle(@Valid @ModelAttribute(value="adres") Adres adres, BindingResult result, HttpSession session, RedirectAttributes redirectAttributes) {
        Long kullaniciId = (Long)session.getAttribute("KullaniciId");
        if (kullaniciId == null) {
            return "redirect:/kullanici/login";
        }
        if (result.hasErrors()) {
            return "kullanici/adres-ekle";
        }
        try {
            Kullanici kullanici = (Kullanici)this.kullaniciService.findById(kullaniciId).orElseThrow(() -> new BusinessException("Kullan\u0131c\u0131 bulunamad\u0131"));
            adres.setKullaniciId(kullaniciId);
            this.adresService.createAdres(kullanici, adres);
            redirectAttributes.addFlashAttribute("successMessage", (Object)"Adres ba\u015far\u0131yla eklendi.");
            return "redirect:/adres/liste";
        }
        catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)e.getMessage());
            return "redirect:/adres/ekle";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)("Adres eklenirken bir hata olu\u015ftu: " + e.getMessage()));
            return "redirect:/adres/ekle";
        }
    }

    @GetMapping(value={"/duzenle/{id}"})
    public String adresDuzenleForm(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        block4: {
            Long kullaniciId = (Long)session.getAttribute("KullaniciId");
            if (kullaniciId == null) {
                return "redirect:/kullanici/login";
            }
            try {
                Optional adres = this.adresService.getAdresById(id);
                if (!adres.isPresent() || !((Adres)adres.get()).getKullaniciId().equals(kullaniciId)) break block4;
                model.addAttribute("adres", adres.get());
                return "kullanici/adres-duzenle";
            }
            catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", (Object)("Adres y\u00fcklenirken bir hata olu\u015ftu: " + e.getMessage()));
                return "redirect:/adres/liste";
            }
        }
        redirectAttributes.addFlashAttribute("errorMessage", (Object)"Adres bulunamad\u0131 veya size ait de\u011fil.");
        return "redirect:/adres/liste";
    }

    @PostMapping(value={"/duzenle/{id}"})
    public String adresDuzenle(@PathVariable Long id, @Valid @ModelAttribute(value="adres") Adres adres, BindingResult result, HttpSession session, RedirectAttributes redirectAttributes) {
        Long kullaniciId = (Long)session.getAttribute("KullaniciId");
        if (kullaniciId == null) {
            return "redirect:/kullanici/login";
        }
        if (result.hasErrors()) {
            return "kullanici/adres-duzenle";
        }
        try {
            adres.setKullaniciId(kullaniciId);
            this.adresService.updateAdres(id, adres);
            redirectAttributes.addFlashAttribute("successMessage", (Object)"Adres ba\u015far\u0131yla g\u00fcncellendi.");
            return "redirect:/adres/liste";
        }
        catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)e.getMessage());
            return "redirect:/adres/duzenle/" + id;
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)("Adres g\u00fcncellenirken bir hata olu\u015ftu: " + e.getMessage()));
            return "redirect:/adres/duzenle/" + id;
        }
    }

    @PostMapping(value={"/sil/{id}"})
    public String adresSil(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Long kullaniciId = (Long)session.getAttribute("KullaniciId");
        if (kullaniciId == null) {
            return "redirect:/kullanici/login";
        }
        try {
            Optional adres = this.adresService.getAdresById(id);
            if (adres.isPresent() && ((Adres)adres.get()).getKullaniciId().equals(kullaniciId)) {
                this.adresService.deleteAdres(id);
                redirectAttributes.addFlashAttribute("successMessage", (Object)"Adres ba\u015far\u0131yla silindi.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", (Object)"Adres bulunamad\u0131 veya size ait de\u011fil.");
            }
            return "redirect:/adres/liste";
        }
        catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)e.getMessage());
            return "redirect:/adres/liste";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)("Adres silinirken bir hata olu\u015ftu: " + e.getMessage()));
            return "redirect:/adres/liste";
        }
    }

    @PostMapping(value={"/varsayilan/{id}"})
    public String varsayilanAdresAyarla(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Long kullaniciId = (Long)session.getAttribute("KullaniciId");
        if (kullaniciId == null) {
            return "redirect:/kullanici/login";
        }
        try {
            Optional adres = this.adresService.getAdresById(id);
            if (adres.isPresent() && ((Adres)adres.get()).getKullaniciId().equals(kullaniciId)) {
                this.adresService.setVarsayilanAdres(kullaniciId, id);
                redirectAttributes.addFlashAttribute("successMessage", (Object)"Varsay\u0131lan adres ba\u015far\u0131yla ayarland\u0131.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", (Object)"Adres bulunamad\u0131 veya size ait de\u011fil.");
            }
            return "redirect:/adres/liste";
        }
        catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)e.getMessage());
            return "redirect:/adres/liste";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)("Varsay\u0131lan adres ayarlan\u0131rken bir hata olu\u015ftu: " + e.getMessage()));
            return "redirect:/adres/liste";
        }
    }
}

