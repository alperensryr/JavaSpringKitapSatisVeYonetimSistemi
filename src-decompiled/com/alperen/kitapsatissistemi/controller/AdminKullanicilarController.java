/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.AdminKullanicilarController
 *  com.alperen.kitapsatissistemi.entity.Kullanici
 *  com.alperen.kitapsatissistemi.service.KullaniciService
 *  com.alperen.kitapsatissistemi.util.SessionUtil
 *  javax.servlet.http.HttpSession
 *  javax.validation.Valid
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.PageRequest
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.domain.Sort
 *  org.springframework.data.domain.Sort$Direction
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.validation.BindingResult
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.ModelAttribute
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.servlet.mvc.support.RedirectAttributes
 */
package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.service.KullaniciService;
import com.alperen.kitapsatissistemi.util.SessionUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/admin/kullanicilar"})
public class AdminKullanicilarController {
    @Autowired
    private KullaniciService kullaniciService;
    @Autowired
    private SessionUtil sessionUtil;

    @GetMapping
    public String index(Model model, HttpSession session, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size, @RequestParam(defaultValue="id") String sortBy, @RequestParam(defaultValue="desc") String sortDir, @RequestParam(required=false) String search) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        try {
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by((Sort.Direction)direction, (String[])new String[]{sortBy});
            PageRequest pageable = PageRequest.of((int)page, (int)size, (Sort)sort);
            Page kullaniciPage = search != null && !search.trim().isEmpty() ? this.kullaniciService.findByAdSoyadContainingIgnoreCaseOrEmailContainingIgnoreCase(search.trim(), search.trim(), (Pageable)pageable) : this.kullaniciService.findAll((Pageable)pageable);
            model.addAttribute("kullanicilar", (Object)kullaniciPage.getContent());
            model.addAttribute("currentPage", (Object)page);
            model.addAttribute("totalPages", (Object)kullaniciPage.getTotalPages());
            model.addAttribute("totalElements", (Object)kullaniciPage.getTotalElements());
            model.addAttribute("sortBy", (Object)sortBy);
            model.addAttribute("sortDir", (Object)sortDir);
            model.addAttribute("search", (Object)search);
            model.addAttribute("size", (Object)size);
            long totalUsers = kullaniciPage.getTotalElements();
            long adminCount = this.kullaniciService.getKullaniciCountByRol("Admin");
            long activeUsers = totalUsers;
            LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
            long newUsersThisMonth = this.kullaniciService.getKullanicilarByKayitTarihiAfter(startOfMonth).size();
            model.addAttribute("totalUsers", (Object)totalUsers);
            model.addAttribute("adminCount", (Object)adminCount);
            model.addAttribute("activeUsers", (Object)activeUsers);
            model.addAttribute("newUsersThisMonth", (Object)newUsersThisMonth);
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Kullan\u0131c\u0131lar y\u00fcklenirken hata olu\u015ftu: " + e.getMessage()));
        }
        model.addAttribute("title", (Object)"Kullan\u0131c\u0131 Y\u00f6netimi");
        return "admin/kullanicilar/index";
    }

    @GetMapping(value={"/create"})
    public String create(Model model, HttpSession session) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        model.addAttribute("kullanici", (Object)new Kullanici());
        model.addAttribute("title", (Object)"Yeni Kullan\u0131c\u0131 Ekle");
        return "admin/kullanicilar/create";
    }

    @PostMapping(value={"/create"})
    public String create(@Valid @ModelAttribute Kullanici kullanici, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        block5: {
            String accessCheck = this.sessionUtil.checkAdminAccess(session);
            if (accessCheck != null) {
                return accessCheck;
            }
            if (bindingResult.hasErrors()) {
                model.addAttribute("title", (Object)"Yeni Kullan\u0131c\u0131 Ekle");
                return "admin/kullanicilar/create";
            }
            try {
                if (!this.kullaniciService.existsByEmail(kullanici.getEmail())) break block5;
                model.addAttribute("errorMessage", (Object)"Bu email adresi zaten kullan\u0131lmaktad\u0131r.");
                model.addAttribute("title", (Object)"Yeni Kullan\u0131c\u0131 Ekle");
                return "admin/kullanicilar/create";
            }
            catch (Exception e) {
                model.addAttribute("errorMessage", (Object)("Kullan\u0131c\u0131 eklenirken hata olu\u015ftu: " + e.getMessage()));
                model.addAttribute("title", (Object)"Yeni Kullan\u0131c\u0131 Ekle");
                return "admin/kullanicilar/create";
            }
        }
        this.kullaniciService.save(kullanici);
        redirectAttributes.addFlashAttribute("successMessage", (Object)"Kullan\u0131c\u0131 ba\u015far\u0131yla eklendi.");
        return "redirect:/admin/kullanicilar";
    }

    @GetMapping(value={"/edit/{id}"})
    public String edit(@PathVariable Long id, Model model, HttpSession session) {
        block4: {
            String accessCheck = this.sessionUtil.checkAdminAccess(session);
            if (accessCheck != null) {
                return accessCheck;
            }
            try {
                Optional kullaniciOpt = this.kullaniciService.findById(id);
                if (!kullaniciOpt.isPresent()) break block4;
                Kullanici kullanici = (Kullanici)kullaniciOpt.get();
                kullanici.setSifreHash(null);
                model.addAttribute("kullanici", (Object)kullanici);
                model.addAttribute("title", (Object)"Kullan\u0131c\u0131 D\u00fczenle");
                return "admin/kullanicilar/edit";
            }
            catch (Exception e) {
                model.addAttribute("errorMessage", (Object)("Kullan\u0131c\u0131 y\u00fcklenirken hata olu\u015ftu: " + e.getMessage()));
                return "redirect:/admin/kullanicilar";
            }
        }
        model.addAttribute("errorMessage", (Object)"Kullan\u0131c\u0131 bulunamad\u0131.");
        return "redirect:/admin/kullanicilar";
    }

    @PostMapping(value={"/edit/{id}"})
    public String edit(@PathVariable Long id, @Valid @ModelAttribute Kullanici kullanici, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Kullanici mevcutKullanici;
        block7: {
            Optional mevcutKullaniciOpt;
            block6: {
                String accessCheck = this.sessionUtil.checkAdminAccess(session);
                if (accessCheck != null) {
                    return accessCheck;
                }
                if (bindingResult.hasErrors()) {
                    model.addAttribute("title", (Object)"Kullan\u0131c\u0131 D\u00fczenle");
                    return "admin/kullanicilar/edit";
                }
                try {
                    mevcutKullaniciOpt = this.kullaniciService.findById(id);
                    if (mevcutKullaniciOpt.isPresent()) break block6;
                    redirectAttributes.addFlashAttribute("errorMessage", (Object)"Kullan\u0131c\u0131 bulunamad\u0131.");
                    return "redirect:/admin/kullanicilar";
                }
                catch (Exception e) {
                    model.addAttribute("errorMessage", (Object)("Kullan\u0131c\u0131 g\u00fcncellenirken hata olu\u015ftu: " + e.getMessage()));
                    model.addAttribute("title", (Object)"Kullan\u0131c\u0131 D\u00fczenle");
                    return "admin/kullanicilar/edit";
                }
            }
            mevcutKullanici = (Kullanici)mevcutKullaniciOpt.get();
            if (mevcutKullanici.getEmail().equals(kullanici.getEmail()) || !this.kullaniciService.existsByEmail(kullanici.getEmail())) break block7;
            model.addAttribute("errorMessage", (Object)"Bu email adresi ba\u015fka bir kullan\u0131c\u0131 taraf\u0131ndan kullan\u0131lmaktad\u0131r.");
            model.addAttribute("title", (Object)"Kullan\u0131c\u0131 D\u00fczenle");
            return "admin/kullanicilar/edit";
        }
        mevcutKullanici.setAdSoyad(kullanici.getAdSoyad());
        mevcutKullanici.setEmail(kullanici.getEmail());
        mevcutKullanici.setRol(kullanici.getRol());
        this.kullaniciService.save(mevcutKullanici);
        redirectAttributes.addFlashAttribute("successMessage", (Object)"Kullan\u0131c\u0131 ba\u015far\u0131yla g\u00fcncellendi.");
        return "redirect:/admin/kullanicilar";
    }

    @PostMapping(value={"/delete/{id}"})
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        try {
            this.kullaniciService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", (Object)"Kullan\u0131c\u0131 ba\u015far\u0131yla silindi.");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)("Kullan\u0131c\u0131 silinirken hata olu\u015ftu: " + e.getMessage()));
        }
        return "redirect:/admin/kullanicilar";
    }
}

