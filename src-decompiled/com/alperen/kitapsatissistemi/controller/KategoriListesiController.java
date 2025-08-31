/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.KategoriListesiController
 *  com.alperen.kitapsatissistemi.entity.Kategori
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.service.KategoriService
 *  com.alperen.kitapsatissistemi.service.KitapService
 *  javax.servlet.http.HttpSession
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.web.bind.annotation.GetMapping
 */
package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kategori;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.service.KategoriService;
import com.alperen.kitapsatissistemi.service.KitapService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KategoriListesiController {
    @Autowired
    private KategoriService kategoriService;
    @Autowired
    private KitapService kitapService;

    @GetMapping(value={"/kategori-listesi"})
    public String kategoriListesi(Model model, HttpSession session) {
        try {
            Long kullaniciId = (Long)session.getAttribute("KullaniciId");
            String kullaniciAdi = (String)session.getAttribute("KullaniciAd");
            if (kullaniciId != null && kullaniciAdi != null) {
                model.addAttribute("isLoggedIn", (Object)true);
                model.addAttribute("kullaniciAdi", (Object)kullaniciAdi);
            } else {
                model.addAttribute("isLoggedIn", (Object)false);
            }
            List kategoriler = this.kategoriService.getAllKategoriler();
            HashMap<Long, Long> kategoriKitapSayilari = new HashMap<Long, Long>();
            for (Kategori kategori : kategoriler) {
                long kitapSayisi = this.kitapService.countByKategoriId(kategori.getId());
                kategoriKitapSayilari.put(kategori.getId(), kitapSayisi);
            }
            model.addAttribute("kategoriler", (Object)kategoriler);
            model.addAttribute("kategoriKitapSayilari", kategoriKitapSayilari);
            model.addAttribute("title", (Object)"Kategoriler");
            return "kategoriler/index";
        }
        catch (BusinessException e) {
            model.addAttribute("errorMessage", (Object)e.getMessage());
            model.addAttribute("kategoriler", new ArrayList());
            model.addAttribute("kategoriKitapSayilari", new HashMap());
            return "kategoriler/index";
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Kategoriler y\u00fcklenirken bir hata olu\u015ftu: " + e.getMessage()));
            model.addAttribute("kategoriler", new ArrayList());
            model.addAttribute("kategoriKitapSayilari", new HashMap());
            return "kategoriler/index";
        }
    }
}

