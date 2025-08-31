/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.AdminKitaplarController
 *  com.alperen.kitapsatissistemi.entity.Kitap
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.service.KategoriService
 *  com.alperen.kitapsatissistemi.service.KitapService
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

import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.service.KategoriService;
import com.alperen.kitapsatissistemi.service.KitapService;
import com.alperen.kitapsatissistemi.service.KullaniciService;
import com.alperen.kitapsatissistemi.util.SessionUtil;
import java.util.List;
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
@RequestMapping(value={"/admin/kitaplar"})
public class AdminKitaplarController {
    @Autowired
    private KitapService kitapService;
    @Autowired
    private KategoriService kategoriService;
    @Autowired
    private KullaniciService kullaniciService;
    @Autowired
    private SessionUtil sessionUtil;

    @GetMapping
    public String index(Model model, HttpSession session, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size, @RequestParam(defaultValue="ad") String sortBy, @RequestParam(defaultValue="asc") String sortDir, @RequestParam(required=false) String search) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        try {
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by((Sort.Direction)direction, (String[])new String[]{sortBy});
            PageRequest pageable = PageRequest.of((int)page, (int)size, (Sort)sort);
            Page kitapPage = search != null && !search.trim().isEmpty() ? this.kitapService.findByAdContainingIgnoreCase(search.trim(), (Pageable)pageable) : this.kitapService.findAll((Pageable)pageable);
            model.addAttribute("kitaplar", (Object)kitapPage.getContent());
            model.addAttribute("currentPage", (Object)page);
            model.addAttribute("totalPages", (Object)kitapPage.getTotalPages());
            model.addAttribute("totalElements", (Object)kitapPage.getTotalElements());
            model.addAttribute("sortBy", (Object)sortBy);
            model.addAttribute("sortDir", (Object)sortDir);
            model.addAttribute("search", (Object)search);
        }
        catch (BusinessException e) {
            model.addAttribute("errorMessage", (Object)e.getMessage());
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Kitaplar y\u00fcklenirken hata olu\u015ftu: " + e.getMessage()));
        }
        model.addAttribute("title", (Object)"Kitap Y\u00f6netimi");
        return "admin/kitaplar/index";
    }

    @GetMapping(value={"/create"})
    public String create(Model model, HttpSession session) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        try {
            List kategoriler = this.kategoriService.findAll();
            model.addAttribute("kategoriler", (Object)kategoriler);
            model.addAttribute("kitap", (Object)new Kitap());
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Kategoriler y\u00fcklenirken hata olu\u015ftu: " + e.getMessage()));
        }
        model.addAttribute("title", (Object)"Yeni Kitap Ekle");
        return "admin/kitaplar/create";
    }

    @PostMapping(value={"/create"})
    public String create(@Valid @ModelAttribute Kitap kitap, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        if (bindingResult.hasErrors()) {
            try {
                List kategoriler = this.kategoriService.findAll();
                model.addAttribute("kategoriler", (Object)kategoriler);
            }
            catch (BusinessException e) {
                model.addAttribute("errorMessage", (Object)e.getMessage());
            }
            catch (Exception e) {
                model.addAttribute("errorMessage", (Object)("Kategoriler y\u00fcklenirken hata olu\u015ftu: " + e.getMessage()));
            }
            model.addAttribute("title", (Object)"Yeni Kitap Ekle");
            return "admin/kitaplar/create";
        }
        try {
            this.kitapService.save(kitap);
            redirectAttributes.addFlashAttribute("successMessage", (Object)"Kitap ba\u015far\u0131yla eklendi.");
            return "redirect:/admin/kitaplar";
        }
        catch (BusinessException e) {
            model.addAttribute("errorMessage", (Object)e.getMessage());
            try {
                List kategoriler = this.kategoriService.findAll();
                model.addAttribute("kategoriler", (Object)kategoriler);
            }
            catch (BusinessException ex) {
                model.addAttribute("errorMessage", (Object)ex.getMessage());
            }
            catch (Exception ex) {
                model.addAttribute("errorMessage", (Object)("Kategoriler y\u00fcklenirken hata olu\u015ftu: " + ex.getMessage()));
            }
            model.addAttribute("title", (Object)"Yeni Kitap Ekle");
            return "admin/kitaplar/create";
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Kitap eklenirken hata olu\u015ftu: " + e.getMessage()));
            try {
                List kategoriler = this.kategoriService.findAll();
                model.addAttribute("kategoriler", (Object)kategoriler);
            }
            catch (BusinessException ex) {
                model.addAttribute("errorMessage", (Object)ex.getMessage());
            }
            catch (Exception ex) {
                model.addAttribute("errorMessage", (Object)("Kategoriler y\u00fcklenirken hata olu\u015ftu: " + ex.getMessage()));
            }
            model.addAttribute("title", (Object)"Yeni Kitap Ekle");
            return "admin/kitaplar/create";
        }
    }

    @GetMapping(value={"/edit/{id}"})
    public String edit(@PathVariable Long id, Model model, HttpSession session) {
        block5: {
            String accessCheck = this.sessionUtil.checkAdminAccess(session);
            if (accessCheck != null) {
                return accessCheck;
            }
            Optional kitapOpt = this.kitapService.findById(id);
            if (!kitapOpt.isPresent()) break block5;
            List kategoriler = this.kategoriService.findAll();
            model.addAttribute("kategoriler", (Object)kategoriler);
            model.addAttribute("kitap", kitapOpt.get());
            model.addAttribute("title", (Object)"Kitap D\u00fczenle");
            return "admin/kitaplar/edit";
        }
        try {
            model.addAttribute("errorMessage", (Object)"Kitap bulunamad\u0131.");
            return "redirect:/admin/kitaplar";
        }
        catch (BusinessException e) {
            model.addAttribute("errorMessage", (Object)e.getMessage());
            return "redirect:/admin/kitaplar";
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)"Kitap y\u00fcklenirken hata olu\u015ftu.");
            return "redirect:/admin/kitaplar";
        }
    }

    @PostMapping(value={"/edit/{id}"})
    public String edit(@PathVariable Long id, @Valid @ModelAttribute Kitap kitap, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        if (bindingResult.hasErrors()) {
            try {
                List kategoriler = this.kategoriService.findAll();
                model.addAttribute("kategoriler", (Object)kategoriler);
            }
            catch (Exception e) {
                model.addAttribute("errorMessage", (Object)("Kategoriler y\u00fcklenirken hata olu\u015ftu: " + e.getMessage()));
            }
            model.addAttribute("title", (Object)"Kitap D\u00fczenle");
            return "admin/kitaplar/edit";
        }
        try {
            kitap.setId(id);
            this.kitapService.updateKitap(id, kitap);
            redirectAttributes.addFlashAttribute("successMessage", (Object)"Kitap ba\u015far\u0131yla g\u00fcncellendi.");
            return "redirect:/admin/kitaplar";
        }
        catch (BusinessException e) {
            model.addAttribute("errorMessage", (Object)e.getMessage());
            try {
                List kategoriler = this.kategoriService.findAll();
                model.addAttribute("kategoriler", (Object)kategoriler);
            }
            catch (BusinessException ex) {
                model.addAttribute("errorMessage", (Object)ex.getMessage());
            }
            catch (Exception ex) {
                model.addAttribute("errorMessage", (Object)("Kategoriler y\u00fcklenirken hata olu\u015ftu: " + ex.getMessage()));
            }
            model.addAttribute("title", (Object)"Kitap D\u00fczenle");
            return "admin/kitaplar/edit";
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Kitap g\u00fcncellenirken hata olu\u015ftu: " + e.getMessage()));
            try {
                List kategoriler = this.kategoriService.findAll();
                model.addAttribute("kategoriler", (Object)kategoriler);
            }
            catch (BusinessException ex) {
                model.addAttribute("errorMessage", (Object)ex.getMessage());
            }
            catch (Exception ex) {
                model.addAttribute("errorMessage", (Object)("Kategoriler y\u00fcklenirken hata olu\u015ftu: " + ex.getMessage()));
            }
            model.addAttribute("title", (Object)"Kitap D\u00fczenle");
            return "admin/kitaplar/edit";
        }
    }

    @GetMapping(value={"/detail/{id}"})
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        block4: {
            String accessCheck = this.sessionUtil.checkAdminAccess(session);
            if (accessCheck != null) {
                return accessCheck;
            }
            try {
                Optional kitapOpt = this.kitapService.findById(id);
                if (!kitapOpt.isPresent()) break block4;
                model.addAttribute("kitap", kitapOpt.get());
                model.addAttribute("title", (Object)"Kitap Detay\u0131");
                return "admin/kitaplar/detail";
            }
            catch (Exception e) {
                model.addAttribute("errorMessage", (Object)("Kitap y\u00fcklenirken hata olu\u015ftu: " + e.getMessage()));
                return "redirect:/admin/kitaplar";
            }
        }
        model.addAttribute("errorMessage", (Object)"Kitap bulunamad\u0131.");
        return "redirect:/admin/kitaplar";
    }

    @PostMapping(value={"/delete/{id}"})
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        try {
            this.kitapService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", (Object)"Kitap ba\u015far\u0131yla silindi.");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)("Kitap silinirken hata olu\u015ftu: " + e.getMessage()));
        }
        return "redirect:/admin/kitaplar";
    }
}

