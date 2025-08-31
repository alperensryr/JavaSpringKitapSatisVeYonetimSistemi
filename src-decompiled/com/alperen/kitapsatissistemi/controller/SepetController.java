/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.SepetController
 *  com.alperen.kitapsatissistemi.entity.Kitap
 *  com.alperen.kitapsatissistemi.entity.SepetItem
 *  com.alperen.kitapsatissistemi.entity.Siparis
 *  com.alperen.kitapsatissistemi.entity.SiparisDetay
 *  com.alperen.kitapsatissistemi.service.KitapService
 *  com.alperen.kitapsatissistemi.service.SiparisService
 *  com.fasterxml.jackson.core.type.TypeReference
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  javax.servlet.http.HttpServletRequest
 *  javax.servlet.http.HttpSession
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.ResponseEntity
 *  org.springframework.stereotype.Controller
 *  org.springframework.ui.Model
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.ResponseBody
 *  org.springframework.web.servlet.mvc.support.RedirectAttributes
 */
package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.entity.SepetItem;
import com.alperen.kitapsatissistemi.entity.Siparis;
import com.alperen.kitapsatissistemi.entity.SiparisDetay;
import com.alperen.kitapsatissistemi.service.KitapService;
import com.alperen.kitapsatissistemi.service.SiparisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value={"/sepet"})
public class SepetController {
    @Autowired
    private KitapService kitapService;
    @Autowired
    private SiparisService siparisService;
    private static final String SEPET_SESSION_KEY = "Sepet";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public String index(Model model, HttpSession session) {
        List sepet = this.getSepetFromSession(session);
        model.addAttribute("sepetItems", (Object)sepet);
        return "sepet/index";
    }

    @PostMapping(value={"/ekle"})
    public String sepeteEkle(@RequestParam int kitapId, @RequestParam(defaultValue="1") int adet, HttpSession session, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        block7: {
            try {
                Boolean isLoggedIn = (Boolean)session.getAttribute("IsLoggedIn");
                if (isLoggedIn != null && isLoggedIn.booleanValue()) break block7;
                redirectAttributes.addFlashAttribute("errorMessage", (Object)"Sepete \u00fcr\u00fcn eklemek i\u00e7in \u00f6nce giri\u015f yapmal\u0131s\u0131n\u0131z! Giri\u015f yapt\u0131ktan sonra i\u015fleminize devam edebilirsiniz.");
                String returnUrl = request.getHeader("Referer");
                if (returnUrl != null && !returnUrl.isEmpty()) {
                    redirectAttributes.addFlashAttribute("returnUrl", (Object)returnUrl);
                    session.setAttribute("returnUrl", (Object)returnUrl);
                }
                return "redirect:/kullanici/login";
            }
            catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", (Object)("Sepete eklenirken bir hata olu\u015ftu: " + e.getMessage()));
                return "redirect:" + request.getHeader("Referer");
            }
        }
        Optional kitapOpt = this.kitapService.findById(Long.valueOf(kitapId));
        if (!kitapOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)"Kitap bulunamad\u0131!");
            return "redirect:" + request.getHeader("Referer");
        }
        Kitap kitap = (Kitap)kitapOpt.get();
        List sepet = this.getSepetFromSession(session);
        Optional<SepetItem> mevcutItem = sepet.stream().filter(item -> item.getKitapId() == kitapId).findFirst();
        if (mevcutItem.isPresent()) {
            mevcutItem.get().setAdet(mevcutItem.get().getAdet() + adet);
        } else {
            SepetItem yeniItem = new SepetItem(kitapId, kitap.getAd(), kitap.getFiyat(), adet, kitap.getResimUrl());
            sepet.add(yeniItem);
        }
        this.saveSepetToSession(session, sepet);
        redirectAttributes.addFlashAttribute("successMessage", (Object)(String.valueOf(kitap.getAd()) + " sepete eklendi!"));
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping(value={"/count"})
    @ResponseBody
    public int getSepetItemCount(HttpSession session) {
        List sepet = this.getSepetFromSession(session);
        return sepet.stream().mapToInt(SepetItem::getAdet).sum();
    }

    @PostMapping(value={"/sil"})
    public String sepettenSil(@RequestParam int kitapId, HttpSession session, RedirectAttributes redirectAttributes) {
        List sepet = this.getSepetFromSession(session);
        Optional<SepetItem> silinecekItem = sepet.stream().filter(item -> item.getKitapId() == kitapId).findFirst();
        if (silinecekItem.isPresent()) {
            sepet.remove(silinecekItem.get());
            this.saveSepetToSession(session, sepet);
            redirectAttributes.addFlashAttribute("infoMessage", (Object)(String.valueOf(silinecekItem.get().getKitapAd()) + " sepetten kald\u0131r\u0131ld\u0131."));
        }
        return "redirect:/sepet";
    }

    @PostMapping(value={"/adet-guncelle"})
    @ResponseBody
    public ResponseEntity<Map<String, Object>> adetGuncelle(@RequestParam int kitapId, @RequestParam int adet, HttpSession session) {
        HashMap<String, Object> response = new HashMap<String, Object>();
        try {
            List sepet = this.getSepetFromSession(session);
            Optional<SepetItem> guncellenecekItem = sepet.stream().filter(item -> item.getKitapId() == kitapId).findFirst();
            if (guncellenecekItem.isPresent()) {
                if (adet <= 0) {
                    adet = 1;
                }
                guncellenecekItem.get().setAdet(adet);
                this.saveSepetToSession(session, sepet);
                response.put("success", true);
                response.put("message", String.valueOf(guncellenecekItem.get().getKitapAd()) + " adedi " + adet + " olarak g\u00fcncellendi.");
                response.put("yeniAdet", adet);
            } else {
                response.put("success", false);
                response.put("message", "\u00dcr\u00fcn bulunamad\u0131.");
            }
        }
        catch (Exception e) {
            response.put("success", false);
            response.put("message", "G\u00fcncelleme s\u0131ras\u0131nda hata olu\u015ftu.");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(value={"/siparis-tamamla"})
    public String siparisiTamamla(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        List sepet = this.getSepetFromSession(session);
        if (sepet.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)"Sepetiniz bo\u015f!");
            return "redirect:/sepet";
        }
        model.addAttribute("sepetItems", (Object)sepet);
        return "sepet/siparis-tamamla";
    }

    @PostMapping(value={"/siparis-tamamla"})
    public String siparisiTamamlaOnay(@RequestParam String il, @RequestParam String ilce, @RequestParam String adres, @RequestParam(required=false) String postaKodu, @RequestParam String telefon, @RequestParam String kartSahibi, @RequestParam String kartNumarasi, @RequestParam String sonKullanma, @RequestParam String cvv, @RequestParam(required=false) String sozlesme, HttpSession session, RedirectAttributes redirectAttributes) {
        List sepet = this.getSepetFromSession(session);
        if (sepet.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)"Sepetiniz bo\u015f!");
            return "redirect:/sepet";
        }
        Boolean isLoggedIn = (Boolean)session.getAttribute("IsLoggedIn");
        Long kullaniciId = (Long)session.getAttribute("KullaniciId");
        String kullaniciAd = (String)session.getAttribute("KullaniciAd");
        System.out.println("IsLoggedIn: " + isLoggedIn);
        System.out.println("KullaniciId: " + kullaniciId);
        System.out.println("KullaniciAd: " + kullaniciAd);
        if (isLoggedIn == null || !isLoggedIn.booleanValue() || kullaniciId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)"Sipari\u015f vermek i\u00e7in giri\u015f yapmal\u0131s\u0131n\u0131z!");
            return "redirect:/kullanici/login";
        }
        try {
            HashMap<String, List> orderDetails = new HashMap<String, List>();
            orderDetails.put("sepetItems", sepet);
            HashMap<String, String> deliveryAddress = new HashMap<String, String>();
            deliveryAddress.put("il", il);
            deliveryAddress.put("ilce", ilce);
            deliveryAddress.put("detayliAdres", adres);
            deliveryAddress.put("postaKodu", postaKodu != null ? postaKodu : "");
            deliveryAddress.put("telefon", telefon);
            HashMap<String, String> paymentInfo = new HashMap<String, String>();
            paymentInfo.put("odemeYontemi", "Kredi Kart\u0131");
            paymentInfo.put("kartSahibiAdi", kartSahibi);
            String kartNumarasiTemiz = kartNumarasi.replaceAll("\\s+", "");
            String son4Hane = kartNumarasiTemiz.length() >= 4 ? kartNumarasiTemiz.substring(kartNumarasiTemiz.length() - 4) : kartNumarasiTemiz;
            paymentInfo.put("kartSon4Hane", son4Hane);
            List siparisDetaylari = sepet.stream().map(item -> {
                SiparisDetay detay = new SiparisDetay();
                detay.setAdet(Integer.valueOf(item.getAdet()));
                detay.setFiyat(item.getFiyat());
                return detay;
            }).collect(Collectors.toList());
            Siparis siparis = this.siparisService.createSiparisWithAddressAndPayment(kullaniciId, siparisDetaylari, (String)deliveryAddress.get("adSoyad"), (String)deliveryAddress.get("telefon"), (String)deliveryAddress.get("il"), (String)deliveryAddress.get("ilce"), (String)deliveryAddress.get("mahalle"), (String)deliveryAddress.get("detayliAdres"), (String)deliveryAddress.get("postaKodu"), (String)paymentInfo.get("odemeYontemi"), (String)paymentInfo.get("kartSahibiAdi"), (String)paymentInfo.get("kartSon4Hane"));
            session.removeAttribute(SEPET_SESSION_KEY);
            redirectAttributes.addFlashAttribute("successMessage", (Object)("Sipari\u015finiz ba\u015far\u0131yla olu\u015fturuldu! Sipari\u015f No: " + siparis.getId()));
            return "redirect:/siparisler";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", (Object)("Sipari\u015f olu\u015fturulurken bir hata olu\u015ftu: " + e.getMessage()));
            return "redirect:/sepet/siparis-tamamla";
        }
    }

    private List<SepetItem> getSepetFromSession(HttpSession session) {
        try {
            String sepetJson = (String)session.getAttribute(SEPET_SESSION_KEY);
            if (sepetJson == null || sepetJson.isEmpty()) {
                return new ArrayList<SepetItem>();
            }
            return (List)this.objectMapper.readValue(sepetJson, (TypeReference)new /* Unavailable Anonymous Inner Class!! */);
        }
        catch (Exception e) {
            return new ArrayList<SepetItem>();
        }
    }

    private void saveSepetToSession(HttpSession session, List<SepetItem> sepet) {
        try {
            String sepetJson = this.objectMapper.writeValueAsString(sepet);
            session.setAttribute(SEPET_SESSION_KEY, (Object)sepetJson);
        }
        catch (Exception e) {
            session.setAttribute(SEPET_SESSION_KEY, (Object)"[]");
        }
    }
}

