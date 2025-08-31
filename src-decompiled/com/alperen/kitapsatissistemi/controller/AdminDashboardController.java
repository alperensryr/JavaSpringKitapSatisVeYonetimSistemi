/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.AdminDashboardController
 *  com.alperen.kitapsatissistemi.entity.Kategori
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.service.KategoriService
 *  com.alperen.kitapsatissistemi.service.KitapService
 *  com.alperen.kitapsatissistemi.service.KullaniciService
 *  com.alperen.kitapsatissistemi.service.SiparisService
 *  com.alperen.kitapsatissistemi.util.SessionUtil
 *  javax.servlet.http.HttpSession
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Controller
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.ui.Model
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 */
package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kategori;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.service.KategoriService;
import com.alperen.kitapsatissistemi.service.KitapService;
import com.alperen.kitapsatissistemi.service.KullaniciService;
import com.alperen.kitapsatissistemi.service.SiparisService;
import com.alperen.kitapsatissistemi.util.SessionUtil;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/admin"})
public class AdminDashboardController {
    @Autowired
    private KitapService kitapService;
    @Autowired
    private KategoriService kategoriService;
    @Autowired
    private KullaniciService kullaniciService;
    @Autowired
    private SiparisService siparisService;
    @Autowired
    private SessionUtil sessionUtil;

    @GetMapping(value={"/dashboard"})
    @Transactional
    public String index(Model model, HttpSession session) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        try {
            long totalBooks = this.kitapService.count();
            long totalCategories = this.kategoriService.count();
            long totalUsers = this.kullaniciService.count();
            long totalOrders = this.siparisService.count();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            long thisMonthOrders = this.siparisService.getSiparislerAfterDate(startOfMonth).size();
            LinkedHashMap<String, Integer> kategoriDagilimi = new LinkedHashMap<String, Integer>();
            List kategoriler = this.kategoriService.getAllKategoriler();
            for (Kategori kategori : kategoriler) {
                long kitapSayisi = this.kitapService.getKitapCountByKategoriId(kategori.getId());
                kategoriDagilimi.put(kategori.getAd(), (int)kitapSayisi);
            }
            LinkedHashMap<String, Integer> aylikSiparisler = new LinkedHashMap<String, Integer>();
            int i = 5;
            while (i >= 0) {
                LocalDateTime ay = now.minusMonths(i);
                String ayAdi = ay.getMonth().getDisplayName(TextStyle.FULL, new Locale("tr", "TR"));
                LocalDateTime ayBaslangic = ay.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                LocalDateTime ayBitis = ay.withDayOfMonth(ay.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
                List aySiparisleri = this.siparisService.getSiparislerAfterDate(ayBaslangic).stream().filter(s -> s.getSiparisTarihi().isBefore(ayBitis)).collect(Collectors.toList());
                aylikSiparisler.put(ayAdi, aySiparisleri.size());
                --i;
            }
            List sonSiparisler = this.siparisService.getAllSiparislerWithDetails().stream().sorted((s1, s2) -> s2.getSiparisTarihi().compareTo(s1.getSiparisTarihi())).limit(5L).collect(Collectors.toList());
            List populerKitaplar = this.kitapService.getAllKitaplarWithKategori().stream().limit(5L).collect(Collectors.toList());
            ArrayList aylikSiparislerLabels = new ArrayList(aylikSiparisler.keySet());
            ArrayList aylikSiparislerValues = new ArrayList(aylikSiparisler.values());
            ArrayList kategoriLabels = new ArrayList(kategoriDagilimi.keySet());
            ArrayList kategoriValues = new ArrayList(kategoriDagilimi.values());
            List sonKullanicilar = this.kullaniciService.getAllKullanicilar().stream().sorted((k1, k2) -> k2.getKayitTarihi().compareTo(k1.getKayitTarihi())).limit(5L).collect(Collectors.toList());
            List sonKitaplar = this.kitapService.getAllKitaplarWithKategori().stream().sorted((k1, k2) -> k2.getId().compareTo(k1.getId())).limit(5L).collect(Collectors.toList());
            model.addAttribute("kullaniciCount", (Object)totalUsers);
            model.addAttribute("kitapCount", (Object)totalBooks);
            model.addAttribute("kategoriCount", (Object)totalCategories);
            model.addAttribute("siparisCount", (Object)totalOrders);
            model.addAttribute("thisMonthOrders", (Object)thisMonthOrders);
            model.addAttribute("kategoriDagilimi", kategoriDagilimi);
            model.addAttribute("aylikSiparisler", aylikSiparisler);
            model.addAttribute("latestSiparisler", sonSiparisler);
            model.addAttribute("latestKitaplar", sonKitaplar);
            model.addAttribute("latestKullanicilar", sonKullanicilar);
            model.addAttribute("populerKitaplar", populerKitaplar);
            model.addAttribute("aylikSiparislerLabels", aylikSiparislerLabels);
            model.addAttribute("aylikSiparislerValues", aylikSiparislerValues);
            model.addAttribute("kategoriLabels", kategoriLabels);
            model.addAttribute("kategoriValues", kategoriValues);
        }
        catch (BusinessException e) {
            model.addAttribute("errorMessage", (Object)("\u0130\u015f kural\u0131 hatas\u0131: " + e.getMessage()));
        }
        catch (Exception e) {
            model.addAttribute("errorMessage", (Object)("\u0130statistikler y\u00fcklenirken hata olu\u015ftu: " + e.getMessage()));
            e.printStackTrace();
        }
        model.addAttribute("title", (Object)"Admin Dashboard");
        return "admin/dashboard";
    }

    @GetMapping(value={"/raporlar"})
    public String raporlar(Model model, HttpSession session) {
        String accessCheck = this.sessionUtil.checkAdminAccess(session);
        if (accessCheck != null) {
            return accessCheck;
        }
        try {
            List kitaplar = this.kitapService.getAllKitaplarWithKategori();
            List kullanicilar = this.kullaniciService.getAllKullanicilar();
            List kategoriler = this.kategoriService.getAllKategoriler();
            List siparisler = this.siparisService.getAllSiparislerWithDetails();
            long toplamKitap = kitaplar.size();
            long toplamKullanici = kullanicilar.size();
            long toplamKategori = kategoriler.size();
            LocalDateTime now = LocalDateTime.now();
            long buAySiparis = siparisler.stream().filter(s -> s.getSiparisTarihi().getMonth() == now.getMonth() && s.getSiparisTarihi().getYear() == now.getYear()).count();
            List sonSiparisler = siparisler.stream().sorted((s1, s2) -> s2.getSiparisTarihi().compareTo(s1.getSiparisTarihi())).limit(5L).collect(Collectors.toList());
            List populerKitaplar = kitaplar.stream().limit(5L).collect(Collectors.toList());
            LinkedHashMap<String, Integer> kategoriDagilimi = new LinkedHashMap<String, Integer>();
            for (Kategori kategori : kategoriler) {
                long kategoriKitapSayisi = kitaplar.stream().filter(k -> k.getKategori() != null && k.getKategori().getId().equals(kategori.getId())).count();
                kategoriDagilimi.put(kategori.getAd(), (int)kategoriKitapSayisi);
            }
            LinkedHashMap<String, Integer> aylikSiparisler = new LinkedHashMap<String, Integer>();
            int i = 5;
            while (i >= 0) {
                LocalDateTime ay = now.minusMonths(i);
                String ayAdi = ay.getMonth().getDisplayName(TextStyle.FULL, new Locale("tr", "TR"));
                long aySiparisSayisi = siparisler.stream().filter(s -> s.getSiparisTarihi().getMonth() == ay.getMonth() && s.getSiparisTarihi().getYear() == ay.getYear()).count();
                aylikSiparisler.put(ayAdi, (int)aySiparisSayisi);
                --i;
            }
            model.addAttribute("toplamKitap", (Object)toplamKitap);
            model.addAttribute("toplamKullanici", (Object)toplamKullanici);
            model.addAttribute("toplamKategori", (Object)toplamKategori);
            model.addAttribute("buAySiparis", (Object)buAySiparis);
            model.addAttribute("sonSiparisler", sonSiparisler);
            model.addAttribute("populerKitaplar", populerKitaplar);
            model.addAttribute("kategoriDagilimi", kategoriDagilimi);
            model.addAttribute("aylikSiparisler", aylikSiparisler);
            System.out.println("Toplam Kitap: " + toplamKitap);
            System.out.println("Kategori Da\u011f\u0131l\u0131m\u0131: " + kategoriDagilimi.entrySet().stream().map(entry -> String.valueOf((String)entry.getKey()) + ":" + entry.getValue()).collect(Collectors.joining(", ")));
            System.out.println("Ayl\u0131k Sipari\u015fler: " + aylikSiparisler.entrySet().stream().map(entry -> String.valueOf((String)entry.getKey()) + ":" + entry.getValue()).collect(Collectors.joining(", ")));
        }
        catch (Exception ex) {
            System.out.println("Rapor verileri y\u00fcklenirken hata: " + ex.getMessage());
            ex.printStackTrace();
            model.addAttribute("toplamKitap", (Object)0);
            model.addAttribute("toplamKullanici", (Object)0);
            model.addAttribute("toplamKategori", (Object)0);
            model.addAttribute("buAySiparis", (Object)0);
            model.addAttribute("sonSiparisler", new ArrayList());
            model.addAttribute("populerKitaplar", new ArrayList());
            model.addAttribute("kategoriDagilimi", new HashMap());
            model.addAttribute("aylikSiparisler", new HashMap());
        }
        model.addAttribute("title", (Object)"Raporlar");
        return "admin/raporlar";
    }
}

