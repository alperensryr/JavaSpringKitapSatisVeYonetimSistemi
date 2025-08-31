/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.LegacyRouteRedirectController
 *  org.springframework.stereotype.Controller
 *  org.springframework.web.bind.annotation.GetMapping
 */
package com.alperen.kitapsatissistemi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LegacyRouteRedirectController {
    @GetMapping(value={"/kullanici/adres-ekle"})
    public String redirectAdresEkle() {
        return "redirect:/adres/ekle";
    }

    @GetMapping(value={"/kullanici/adresler"})
    public String redirectAdresListe() {
        return "redirect:/adres/liste";
    }
}

