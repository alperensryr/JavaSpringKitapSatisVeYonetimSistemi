/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.FavorilerWebController
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.service.FavoriService
 *  com.alperen.kitapsatissistemi.util.SessionUtil
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpSession
 *  org.springframework.beans.factory.annotation.Autowired
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

import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.service.FavoriService;
import com.alperen.kitapsatissistemi.util.SessionUtil;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/favoriler"})
public class FavorilerWebController {
    @Autowired
    private FavoriService favoriService;
    @Autowired
    private SessionUtil sessionUtil;

    @GetMapping
    public String index(Model model, HttpSession session) {
        String accessCheck = this.sessionUtil.checkUserAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        Long kullaniciId = this.sessionUtil.getCurrentUserId(session);
        model.addAttribute("title", (Object)"Favorilerim");
        try {
            List favoriler = this.favoriService.getFavorilerByKullaniciIdWithDetails(kullaniciId);
            model.addAttribute("favoriler", (Object)favoriler);
        }
        catch (BusinessException e) {
            model.addAttribute("errorMessage", (Object)e.getMessage());
            model.addAttribute("favoriler", new ArrayList());
        }
        catch (Exception e) {
            System.out.println("=== FAVORILER HATA DETAYI ===");
            System.out.println("Hata mesaj\u0131: " + e.getMessage());
            System.out.println("Hata s\u0131n\u0131f\u0131: " + e.getClass().getName());
            e.printStackTrace();
            model.addAttribute("errorMessage", (Object)("Favoriler y\u00fcklenirken hata olu\u015ftu: " + e.getMessage()));
            model.addAttribute("favoriler", new ArrayList());
        }
        return "favoriler";
    }

    @PostMapping(value={"/ekle"})
    public String addFavori(@RequestParam Long kitapId, HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String accessCheck = this.sessionUtil.checkUserAccess(session);
        if (accessCheck != null) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)"Favorilere \u00fcr\u00fcn eklemek i\u00e7in \u00f6nce giri\u015f yapmal\u0131s\u0131n\u0131z!");
            return accessCheck;
        }
        Long kullaniciId = this.sessionUtil.getCurrentUserId(session);
        try {
            this.favoriService.addFavori(kullaniciId, kitapId);
            redirectAttributes.addFlashAttribute("successMessage", (Object)"Kitap favorilere eklendi!");
        }
        catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)e.getMessage());
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)("Favori eklenirken hata olu\u015ftu: " + e.getMessage()));
        }
        String returnUrl = request.getHeader("Referer");
        if (returnUrl != null && !returnUrl.isEmpty()) {
            return "redirect:" + returnUrl;
        }
        return "redirect:/kitaplar";
    }

    @PostMapping(value={"/remove/{id}"})
    public String removeFavori(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        String accessCheck = this.sessionUtil.checkUserAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        try {
            this.favoriService.deleteFavori(id);
            redirectAttributes.addFlashAttribute("successMessage", (Object)"Kitap favorilerden kald\u0131r\u0131ld\u0131.");
        }
        catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)e.getMessage());
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)("Favori kald\u0131r\u0131l\u0131rken hata olu\u015ftu: " + e.getMessage()));
        }
        return "redirect:/favoriler";
    }
}

