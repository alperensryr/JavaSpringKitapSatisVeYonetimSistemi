/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.AdminKategorilerController
 *  com.alperen.kitapsatissistemi.entity.Kategori
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.service.KategoriService
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

import com.alperen.kitapsatissistemi.entity.Kategori;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.service.KategoriService;
import com.alperen.kitapsatissistemi.util.SessionUtil;
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
@RequestMapping(value={"/admin/kategoriler"})
public class AdminKategorilerController {
    @Autowired
    private KategoriService kategoriService;
    @Autowired
    private SessionUtil sessionUtil;

    @GetMapping
    public String index(Model model, HttpSession session, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size, @RequestParam(defaultValue="id") String sortBy, @RequestParam(defaultValue="desc") String sortDir, @RequestParam(required=false) String search) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        try {
            String normalizedSort;
            String string = normalizedSort = sortBy != null ? sortBy : "id";
            if (!("id".equalsIgnoreCase(normalizedSort) || "ad".equalsIgnoreCase(normalizedSort) || "aciklama".equalsIgnoreCase(normalizedSort))) {
                normalizedSort = "id";
            }
            String normalizedDir = "desc".equalsIgnoreCase(sortDir) ? "desc" : "asc";
            Sort.Direction direction = "desc".equalsIgnoreCase(normalizedDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by((Sort.Direction)direction, (String[])new String[]{normalizedSort});
            PageRequest pageable = PageRequest.of((int)page, (int)size, (Sort)sort);
            Page kategoriPage = search != null && !search.trim().isEmpty() ? this.kategoriService.findByAdContainingIgnoreCase(search.trim(), (Pageable)pageable) : this.kategoriService.findAll((Pageable)pageable);
            model.addAttribute("kategoriler", (Object)kategoriPage.getContent());
            model.addAttribute("currentPage", (Object)page);
            model.addAttribute("totalPages", (Object)kategoriPage.getTotalPages());
            model.addAttribute("totalElements", (Object)kategoriPage.getTotalElements());
            model.addAttribute("sortBy", (Object)normalizedSort);
            model.addAttribute("sortDir", (Object)normalizedDir);
            model.addAttribute("search", (Object)search);
            model.addAttribute("size", (Object)size);
        }
        catch (BusinessException e) {
            model.addAttribute("errorMessage", (Object)e.getMessage());
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Kategoriler y\u00fcklenirken hata olu\u015ftu: " + e.getMessage()));
        }
        model.addAttribute("title", (Object)"Kategori Y\u00f6netimi");
        return "admin/kategoriler/index";
    }

    @GetMapping(value={"/create"})
    public String create(Model model, HttpSession session) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        model.addAttribute("kategori", (Object)new Kategori());
        model.addAttribute("title", (Object)"Yeni Kategori Ekle");
        return "admin/kategoriler/create";
    }

    @PostMapping(value={"/create"})
    public String create(@Valid @ModelAttribute Kategori kategori, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", (Object)"Yeni Kategori Ekle");
            return "admin/kategoriler/create";
        }
        try {
            this.kategoriService.save(kategori);
            redirectAttributes.addFlashAttribute("successMessage", (Object)"Kategori ba\u015far\u0131yla eklendi.");
            return "redirect:/admin/kategoriler";
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Kategori eklenirken hata olu\u015ftu: " + e.getMessage()));
            model.addAttribute("title", (Object)"Yeni Kategori Ekle");
            return "admin/kategoriler/create";
        }
    }

    @GetMapping(value={"/edit/{id}"})
    public String edit(@PathVariable Long id, Model model, HttpSession session) {
        block5: {
            String accessCheck = this.sessionUtil.checkAdminAccess(session);
            if (accessCheck != null) {
                return accessCheck;
            }
            Optional kategoriOpt = this.kategoriService.findById(id);
            if (!kategoriOpt.isPresent()) break block5;
            model.addAttribute("kategori", kategoriOpt.get());
            model.addAttribute("title", (Object)"Kategori D\u00fczenle");
            return "admin/kategoriler/edit";
        }
        try {
            model.addAttribute("errorMessage", (Object)"Kategori bulunamad\u0131.");
            return "redirect:/admin/kategoriler";
        }
        catch (BusinessException e) {
            model.addAttribute("errorMessage", (Object)e.getMessage());
            return "redirect:/admin/kategoriler";
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Kategori y\u00fcklenirken hata olu\u015ftu: " + e.getMessage()));
            return "redirect:/admin/kategoriler";
        }
    }

    @PostMapping(value={"/edit/{id}"})
    public String edit(@PathVariable Long id, @Valid @ModelAttribute Kategori kategori, BindingResult bindingResult, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("title", (Object)"Kategori D\u00fczenle");
            return "admin/kategoriler/edit";
        }
        try {
            kategori.setId(id);
            this.kategoriService.save(kategori);
            redirectAttributes.addFlashAttribute("successMessage", (Object)"Kategori ba\u015far\u0131yla g\u00fcncellendi.");
            return "redirect:/admin/kategoriler";
        }
        catch (BusinessException e) {
            model.addAttribute("errorMessage", (Object)e.getMessage());
            model.addAttribute("title", (Object)"Kategori D\u00fczenle");
            return "admin/kategoriler/edit";
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Kategori g\u00fcncellenirken hata olu\u015ftu: " + e.getMessage()));
            model.addAttribute("title", (Object)"Kategori D\u00fczenle");
            return "admin/kategoriler/edit";
        }
    }

    @PostMapping(value={"/delete/{id}"})
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        try {
            this.kategoriService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", (Object)"Kategori ba\u015far\u0131yla silindi.");
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)("Kategori silinirken hata olu\u015ftu: " + e.getMessage()));
        }
        return "redirect:/admin/kategoriler";
    }
}

