/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.HomeController
 *  com.alperen.kitapsatissistemi.service.KategoriService
 *  com.alperen.kitapsatissistemi.service.KitapService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 */
package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.service.KategoriService;
import com.alperen.kitapsatissistemi.service.KitapService;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/"})
public class HomeController {
    @Autowired
    private KitapService kitapService;
    @Autowired
    private KategoriService kategoriService;

    @GetMapping
    public String index(Model model) {
        try {
            List featuredBooks = this.kitapService.findAll().stream().limit(8L).collect(Collectors.toList());
            List categories = this.kategoriService.findAll().stream().limit(4L).collect(Collectors.toList());
            long totalBooks = this.kitapService.count();
            long totalCategories = this.kategoriService.count();
            model.addAttribute("featuredBooks", featuredBooks);
            model.addAttribute("categories", categories);
            model.addAttribute("totalBooks", (Object)totalBooks);
            model.addAttribute("totalCategories", (Object)totalCategories);
            model.addAttribute("happyCustomers", (Object)1250);
            model.addAttribute("customerSatisfaction", (Object)98);
        }
        catch (Exception e) {
            model.addAttribute("featuredBooks", Collections.emptyList());
            model.addAttribute("categories", Collections.emptyList());
            model.addAttribute("totalBooks", (Object)0L);
            model.addAttribute("totalCategories", (Object)0L);
            model.addAttribute("happyCustomers", (Object)0);
            model.addAttribute("customerSatisfaction", (Object)0);
        }
        return "index";
    }

    @GetMapping(value={"/about"})
    public String about() {
        return "about";
    }

    @GetMapping(value={"/contact"})
    public String contact() {
        return "contact";
    }

    @GetMapping(value={"/privacy"})
    public String privacy() {
        return "privacy";
    }
}

