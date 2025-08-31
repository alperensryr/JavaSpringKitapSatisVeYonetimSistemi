/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.KategorilerWebController
 *  com.alperen.kitapsatissistemi.entity.Kategori
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.service.KategoriService
 *  com.alperen.kitapsatissistemi.service.KitapService
 *  javax.servlet.http.HttpSession
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.PageRequest
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.domain.Sort
 *  org.springframework.data.domain.Sort$Direction
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 */
package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kategori;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.service.KategoriService;
import com.alperen.kitapsatissistemi.service.KitapService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value={"/kategoriler"})
public class KategorilerWebController {
    @Autowired
    private KategoriService kategoriService;
    @Autowired
    private KitapService kitapService;

    @GetMapping
    public String index(Model model, HttpSession session) {
        Long kullaniciId = (Long)session.getAttribute("KullaniciId");
        String kullaniciAdi = (String)session.getAttribute("KullaniciAd");
        if (kullaniciId != null && kullaniciAdi != null) {
            model.addAttribute("isLoggedIn", (Object)true);
            model.addAttribute("kullaniciId", (Object)kullaniciId);
            model.addAttribute("kullaniciAdi", (Object)kullaniciAdi);
        } else {
            model.addAttribute("isLoggedIn", (Object)false);
        }
        try {
            List kategoriler = this.kategoriService.findAll();
            System.out.println("DEBUG: Toplam kategori say\u0131s\u0131: " + kategoriler.size());
            for (Kategori kategori : kategoriler) {
                long kitapSayisi = this.kitapService.countByKategoriId(kategori.getId());
                System.out.println("DEBUG: Kategori '" + kategori.getAd() + "' i\u00e7in kitap say\u0131s\u0131: " + kitapSayisi);
                kategori.setKitapSayisi(Integer.valueOf((int)kitapSayisi));
            }
            model.addAttribute("kategoriler", (Object)kategoriler);
            model.addAttribute("title", (Object)"Kategoriler");
            return "kategoriler/index";
        }
        catch (BusinessException e) {
            model.addAttribute("kategoriler", Collections.emptyList());
            model.addAttribute("errorMessage", (Object)e.getMessage());
            model.addAttribute("title", (Object)"Kategoriler");
            return "kategoriler/index";
        }
        catch (Exception e) {
            model.addAttribute("kategoriler", Collections.emptyList());
            model.addAttribute("errorMessage", (Object)("Kategoriler y\u00fcklenirken bir hata olu\u015ftu: " + e.getMessage()));
            model.addAttribute("title", (Object)"Kategoriler");
            return "kategoriler/index";
        }
    }

    @GetMapping(value={"/{id}"})
    public String detay(@PathVariable Long id, Model model, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="12") int size, @RequestParam(defaultValue="ad") String sortBy, @RequestParam(defaultValue="asc") String sortDir) {
        Optional kategoriOpt;
        block4: {
            kategoriOpt = this.kategoriService.findById(id);
            if (kategoriOpt.isPresent()) break block4;
            model.addAttribute("errorMessage", (Object)"Kategori bulunamad\u0131.");
            return "redirect:/kategoriler";
        }
        try {
            Kategori kategori = (Kategori)kategoriOpt.get();
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by((Sort.Direction)direction, (String[])new String[]{sortBy});
            PageRequest pageable = PageRequest.of((int)page, (int)size, (Sort)sort);
            Page kitapPage = this.kitapService.findByKategoriId(id, (Pageable)pageable);
            model.addAttribute("kategori", (Object)kategori);
            model.addAttribute("kitapPage", (Object)kitapPage);
            model.addAttribute("kitaplar", (Object)kitapPage.getContent());
            model.addAttribute("currentPage", (Object)page);
            model.addAttribute("totalPages", (Object)kitapPage.getTotalPages());
            model.addAttribute("totalElements", (Object)kitapPage.getTotalElements());
            model.addAttribute("sortBy", (Object)sortBy);
            model.addAttribute("sortDir", (Object)sortDir);
            model.addAttribute("title", (Object)(String.valueOf(kategori.getAd()) + " Kategorisi"));
            return "kategoriler/detay";
        }
        catch (BusinessException e) {
            model.addAttribute("errorMessage", (Object)e.getMessage());
            model.addAttribute("kategori", (Object)new Kategori());
            model.addAttribute("kitapPage", (Object)Page.empty());
            model.addAttribute("kitaplar", Collections.emptyList());
            model.addAttribute("currentPage", (Object)0);
            model.addAttribute("totalPages", (Object)0);
            model.addAttribute("totalElements", (Object)0);
            model.addAttribute("sortBy", (Object)sortBy);
            model.addAttribute("sortDir", (Object)sortDir);
            model.addAttribute("title", (Object)"Kategori Bulunamad\u0131");
            return "kategoriler/detay";
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Kategori detaylar\u0131 y\u00fcklenirken bir hata olu\u015ftu: " + e.getMessage()));
            model.addAttribute("kategori", (Object)new Kategori());
            model.addAttribute("kitapPage", (Object)Page.empty());
            model.addAttribute("kitaplar", Collections.emptyList());
            model.addAttribute("currentPage", (Object)0);
            model.addAttribute("totalPages", (Object)0);
            model.addAttribute("totalElements", (Object)0);
            model.addAttribute("sortBy", (Object)sortBy);
            model.addAttribute("sortDir", (Object)sortDir);
            model.addAttribute("title", (Object)"Kategori Bulunamad\u0131");
            return "kategoriler/detay";
        }
    }

    @GetMapping(value={"/{id}/kitaplar"})
    public String kategoriKitaplari(@PathVariable Long id, Model model, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="12") int size, @RequestParam(defaultValue="ad") String sortBy, @RequestParam(defaultValue="asc") String sortDir) {
        return this.detay(id, model, page, size, sortBy, sortDir);
    }
}

