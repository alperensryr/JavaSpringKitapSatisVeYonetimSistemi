/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.SiparislerWebController
 *  com.alperen.kitapsatissistemi.entity.Siparis
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.service.SiparisService
 *  javax.servlet.http.HttpSession
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.RequestMapping
 */
package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Siparis;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.service.SiparisService;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/siparisler"})
public class SiparislerWebController {
    @Autowired
    private SiparisService siparisService;

    @GetMapping
    public String index(Model model, HttpSession session) {
        Long kullaniciId = (Long)session.getAttribute("KullaniciId");
        if (kullaniciId == null) {
            return "redirect:/kullanici/login";
        }
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

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @GetMapping(value={"/{id}"})
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        Siparis siparis;
        block5: {
            Long kullaniciId = (Long)session.getAttribute("KullaniciId");
            if (kullaniciId == null) {
                return "redirect:/kullanici/login";
            }
            Optional siparisOpt = this.siparisService.getSiparisByIdWithDetails(id);
            if (!siparisOpt.isPresent()) return "redirect:/siparisler";
            siparis = (Siparis)siparisOpt.get();
            if (siparis.getKullaniciId().equals(kullaniciId)) break block5;
            return "redirect:/siparisler";
        }
        try {
            model.addAttribute("siparis", (Object)siparis);
            model.addAttribute("title", (Object)("Sipari\u015f Detay\u0131 - #" + id));
            return "siparisler/detail";
        }
        catch (BusinessException e) {
            model.addAttribute("errorMessage", (Object)e.getMessage());
            return "siparisler/index";
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Sipari\u015f detaylar\u0131 y\u00fcklenirken hata olu\u015ftu: " + e.getMessage()));
            return "siparisler/index";
        }
    }
}

