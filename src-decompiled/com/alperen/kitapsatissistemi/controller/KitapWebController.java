/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.KitapWebController
 *  com.alperen.kitapsatissistemi.entity.Kitap
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException
 *  com.alperen.kitapsatissistemi.service.KategoriService
 *  com.alperen.kitapsatissistemi.service.KitapService
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

import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import com.alperen.kitapsatissistemi.service.KategoriService;
import com.alperen.kitapsatissistemi.service.KitapService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
@RequestMapping(value={"/kitaplar"})
public class KitapWebController {
    @Autowired
    private KitapService kitapService;
    @Autowired
    private KategoriService kategoriService;

    @GetMapping
    public String index(Model model, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="12") int size, @RequestParam(defaultValue="ad") String sortBy, @RequestParam(defaultValue="asc") String sortDir, @RequestParam(required=false) String search, @RequestParam(required=false) Long kategoriId) {
        try {
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by((Sort.Direction)direction, (String[])new String[]{sortBy});
            PageRequest pageable = PageRequest.of((int)page, (int)size, (Sort)sort);
            Page kitapPage = search != null && !search.trim().isEmpty() ? this.kitapService.findByAdContainingIgnoreCase(search.trim(), (Pageable)pageable) : (kategoriId != null ? this.kitapService.findByKategoriId(kategoriId, (Pageable)pageable) : this.kitapService.findAll((Pageable)pageable));
            if (kitapPage.isEmpty() && kitapPage.getTotalElements() > 0L) {
                pageable = PageRequest.of((int)0, (int)size, (Sort)sort);
                kitapPage = search != null && !search.trim().isEmpty() ? this.kitapService.findByAdContainingIgnoreCase(search.trim(), (Pageable)pageable) : (kategoriId != null ? this.kitapService.findByKategoriId(kategoriId, (Pageable)pageable) : this.kitapService.findAll((Pageable)pageable));
                page = 0;
            }
            List kategoriler = this.kategoriService.findAll();
            model.addAttribute("kitapPage", (Object)kitapPage);
            model.addAttribute("kitaplar", (Object)kitapPage.getContent());
            model.addAttribute("kategoriler", (Object)kategoriler);
            model.addAttribute("currentPage", (Object)page);
            model.addAttribute("totalPages", (Object)kitapPage.getTotalPages());
            model.addAttribute("totalElements", (Object)kitapPage.getTotalElements());
            model.addAttribute("sortBy", (Object)sortBy);
            model.addAttribute("sortDir", (Object)sortDir);
            model.addAttribute("search", (Object)search);
            model.addAttribute("kategoriId", (Object)kategoriId);
            return "kitaplar/index";
        }
        catch (BusinessException e) {
            model.addAttribute("kitaplar", Collections.emptyList());
            model.addAttribute("kategoriler", Collections.emptyList());
            model.addAttribute("error", (Object)e.getMessage());
            return "kitaplar/index";
        }
    }

    @GetMapping(value={"/{id}"})
    public String detail(@PathVariable Long id, Model model) {
        block7: {
            Optional kitapOpt = this.kitapService.getKitapByIdWithKategori(id);
            if (!kitapOpt.isPresent()) break block7;
            Kitap kitap = (Kitap)kitapOpt.get();
            model.addAttribute("kitap", (Object)kitap);
            if (kitap.getKategori() != null) {
                List benzerKitaplar = this.kitapService.getKitaplarByKategoriId(kitap.getKategori().getId()).stream().filter(k -> !k.getId().equals(id)).limit(4L).collect(Collectors.toList());
                model.addAttribute("relatedBooks", benzerKitaplar);
            } else {
                model.addAttribute("relatedBooks", Collections.emptyList());
            }
            return "kitaplar/detail";
        }
        try {
            model.addAttribute("error", (Object)"Kitap bulunamad\u0131.");
            return "error/404";
        }
        catch (EntityNotFoundBusinessException e) {
            model.addAttribute("errorMessage", (Object)e.getMessage());
            return "error/404";
        }
        catch (BusinessException e) {
            model.addAttribute("errorMessage", (Object)e.getMessage());
            model.addAttribute("kitap", (Object)new Kitap());
            model.addAttribute("relatedBooks", Collections.emptyList());
            return "kitaplar/detail";
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("Kitap detaylar\u0131 y\u00fcklenirken bir hata olu\u015ftu: " + e.getMessage()));
            model.addAttribute("kitap", (Object)new Kitap());
            model.addAttribute("relatedBooks", Collections.emptyList());
            return "kitaplar/detail";
        }
    }
}

