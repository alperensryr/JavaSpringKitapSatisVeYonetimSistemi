/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.AdminFavorilerController
 *  com.alperen.kitapsatissistemi.service.FavoriService
 *  com.alperen.kitapsatissistemi.service.KullaniciService
 *  com.alperen.kitapsatissistemi.util.SessionUtil
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
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.servlet.mvc.support.RedirectAttributes
 */
package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.service.FavoriService;
import com.alperen.kitapsatissistemi.service.KullaniciService;
import com.alperen.kitapsatissistemi.util.SessionUtil;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/admin/favoriler"})
public class AdminFavorilerController {
    @Autowired
    private FavoriService favoriService;
    @Autowired
    private KullaniciService kullaniciService;
    @Autowired
    private SessionUtil sessionUtil;

    @GetMapping
    public String index(Model model, HttpSession session, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size, @RequestParam(defaultValue="id") String sortBy, @RequestParam(defaultValue="desc") String sortDir) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        try {
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by((Sort.Direction)direction, (String[])new String[]{sortBy});
            PageRequest pageable = PageRequest.of((int)page, (int)size, (Sort)sort);
            Page favoriPage = this.favoriService.findAll((Pageable)pageable);
            model.addAttribute("favoriler", (Object)favoriPage.getContent());
            model.addAttribute("currentPage", (Object)page);
            model.addAttribute("totalPages", (Object)favoriPage.getTotalPages());
            model.addAttribute("totalElements", (Object)favoriPage.getTotalElements());
            model.addAttribute("sortBy", (Object)sortBy);
            model.addAttribute("sortDir", (Object)sortDir);
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Favoriler y\u00fcklenirken hata olu\u015ftu: " + e.getMessage()));
        }
        model.addAttribute("title", (Object)"Favori Y\u00f6netimi");
        return "admin/favoriler/index";
    }

    @PostMapping(value={"/delete/{id}"})
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        try {
            this.favoriService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", (Object)"Favori ba\u015far\u0131yla silindi.");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)("Favori silinirken hata olu\u015ftu: " + e.getMessage()));
        }
        return "redirect:/admin/favoriler";
    }

    @PostMapping(value={"/delete-by-user/{kullaniciId}"})
    public String deleteByUser(@PathVariable Long kullaniciId, HttpSession session, RedirectAttributes redirectAttributes) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        try {
            this.favoriService.deleteByKullaniciId(kullaniciId);
            redirectAttributes.addFlashAttribute("successMessage", (Object)"Kullan\u0131c\u0131n\u0131n t\u00fcm favorileri ba\u015far\u0131yla silindi.");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)("Favoriler silinirken hata olu\u015ftu: " + e.getMessage()));
        }
        return "redirect:/admin/favoriler";
    }

    @PostMapping(value={"/delete-by-book/{kitapId}"})
    public String deleteByBook(@PathVariable Long kitapId, HttpSession session, RedirectAttributes redirectAttributes) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        try {
            this.favoriService.deleteByKitapId(kitapId);
            redirectAttributes.addFlashAttribute("successMessage", (Object)"Kitab\u0131n t\u00fcm favorileri ba\u015far\u0131yla silindi.");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)("Favoriler silinirken hata olu\u015ftu: " + e.getMessage()));
        }
        return "redirect:/admin/favoriler";
    }
}

