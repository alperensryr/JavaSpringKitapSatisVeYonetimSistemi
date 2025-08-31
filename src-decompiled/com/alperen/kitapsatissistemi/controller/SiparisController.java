/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.SiparisController
 *  com.alperen.kitapsatissistemi.entity.Kitap
 *  com.alperen.kitapsatissistemi.entity.Siparis
 *  com.alperen.kitapsatissistemi.entity.SiparisDetay
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException
 *  com.alperen.kitapsatissistemi.service.KitapService
 *  com.alperen.kitapsatissistemi.service.SiparisService
 *  javax.validation.Valid
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.format.annotation.DateTimeFormat
 *  org.springframework.format.annotation.DateTimeFormat$ISO
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.entity.Siparis;
import com.alperen.kitapsatissistemi.entity.SiparisDetay;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import com.alperen.kitapsatissistemi.service.KitapService;
import com.alperen.kitapsatissistemi.service.SiparisService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/siparisler"})
@CrossOrigin(origins={"*"})
public class SiparisController {
    private final SiparisService siparisService;
    private final KitapService kitapService;

    @Autowired
    public SiparisController(SiparisService siparisService, KitapService kitapService) {
        this.siparisService = siparisService;
        this.kitapService = kitapService;
    }

    @GetMapping
    public ResponseEntity<List<Siparis>> getAllSiparisler(@RequestParam(defaultValue="false") boolean withDetails) {
        try {
            List siparisler = withDetails ? this.siparisService.getAllSiparislerWithDetails() : this.siparisService.getAllSiparisler();
            return ResponseEntity.ok((Object)siparisler);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<Siparis> getSiparisById(@PathVariable Long id, @RequestParam(defaultValue="false") boolean withDetails) {
        try {
            Optional siparis = withDetails ? this.siparisService.getSiparisByIdWithDetails(id) : this.siparisService.getSiparisById(id);
            return siparis.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/kullanici/{kullaniciId}"})
    public ResponseEntity<List<Siparis>> getSiparislerByKullaniciId(@PathVariable Long kullaniciId, @RequestParam(defaultValue="false") boolean withDetails) {
        try {
            List siparisler = withDetails ? this.siparisService.getSiparislerByKullaniciIdWithDetails(kullaniciId) : this.siparisService.getSiparislerByKullaniciId(kullaniciId);
            return ResponseEntity.ok((Object)siparisler);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/durum/{durum}"})
    public ResponseEntity<List<Siparis>> getSiparislerByDurum(@PathVariable String durum) {
        try {
            List siparisler = this.siparisService.getSiparislerByDurum(durum);
            return ResponseEntity.ok((Object)siparisler);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/tarih-araligi"})
    public ResponseEntity<List<Siparis>> getSiparislerByTarihAraligi(@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime baslangic, @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime bitis) {
        try {
            List siparisler = this.siparisService.getSiparislerByTarihAraligi(baslangic, bitis);
            return ResponseEntity.ok((Object)siparisler);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/tutar-araligi"})
    public ResponseEntity<List<Siparis>> getSiparislerByTutarAraligi(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        try {
            List siparisler = this.siparisService.getSiparislerByTutarAraligi(min, max);
            return ResponseEntity.ok((Object)siparisler);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createSiparis(@Valid @RequestBody Map<String, Object> request) {
        try {
            Long kullaniciId = Long.valueOf(request.get("kullaniciId").toString());
            List detaylarMap = (List)request.get("siparisDetaylari");
            List siparisDetaylari = detaylarMap.stream().map(detayMap -> {
                SiparisDetay detay = new SiparisDetay();
                Long kitapId = Long.valueOf(detayMap.get("kitapId").toString());
                Kitap kitap = (Kitap)this.kitapService.getKitapById(kitapId).orElseThrow(() -> new EntityNotFoundBusinessException("Kitap", (Object)kitapId));
                detay.setKitap(kitap);
                detay.setAdet(Integer.valueOf(detayMap.get("adet").toString()));
                detay.setFiyat(new BigDecimal(detayMap.get("fiyat").toString()));
                return detay;
            }).collect(Collectors.toList());
            Siparis yeniSiparis = this.siparisService.createSiparis(kullaniciId, siparisDetaylari);
            return ResponseEntity.status((HttpStatus)HttpStatus.CREATED).body((Object)yeniSiparis);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Sipari\u015f olu\u015fturulurken bir hata olu\u015ftu");
        }
    }

    @PostMapping(value={"/with-details"})
    public ResponseEntity<?> createSiparisWithDetails(@Valid @RequestBody Map<String, Object> request) {
        try {
            Long kullaniciId = Long.valueOf(request.get("kullaniciId").toString());
            List detaylarMap = (List)request.get("siparisDetaylari");
            List siparisDetaylari = detaylarMap.stream().map(detayMap -> {
                SiparisDetay detay = new SiparisDetay();
                Long kitapId = Long.valueOf(detayMap.get("kitapId").toString());
                Kitap kitap = (Kitap)this.kitapService.getKitapById(kitapId).orElseThrow(() -> new EntityNotFoundBusinessException("Kitap", (Object)kitapId));
                detay.setKitap(kitap);
                detay.setAdet(Integer.valueOf(detayMap.get("adet").toString()));
                detay.setFiyat(new BigDecimal(detayMap.get("fiyat").toString()));
                return detay;
            }).collect(Collectors.toList());
            Map teslimatAdresi = (Map)request.get("teslimatAdresi");
            String teslimatAdSoyad = teslimatAdresi.get("adSoyad").toString();
            String teslimatTelefon = teslimatAdresi.get("telefon").toString();
            String teslimatSehir = teslimatAdresi.get("sehir").toString();
            String teslimatIlce = teslimatAdresi.get("ilce").toString();
            String teslimatMahalle = teslimatAdresi.get("mahalle").toString();
            String teslimatAdresDetay = teslimatAdresi.get("adresDetay").toString();
            String teslimatPostaKodu = teslimatAdresi.get("postaKodu") != null ? teslimatAdresi.get("postaKodu").toString() : null;
            Map odemeBilgileri = (Map)request.get("odemeBilgileri");
            String odemeYontemi = odemeBilgileri.get("odemeYontemi").toString();
            String kartSahibiAdi = odemeBilgileri.get("kartSahibiAdi") != null ? odemeBilgileri.get("kartSahibiAdi").toString() : null;
            String kartNumarasiSon4 = odemeBilgileri.get("kartNumarasiSon4") != null ? odemeBilgileri.get("kartNumarasiSon4").toString() : null;
            Siparis yeniSiparis = this.siparisService.createSiparisWithAddressAndPayment(kullaniciId, siparisDetaylari, teslimatAdSoyad, teslimatTelefon, teslimatSehir, teslimatIlce, teslimatMahalle, teslimatAdresDetay, teslimatPostaKodu, odemeYontemi, kartSahibiAdi, kartNumarasiSon4);
            return ResponseEntity.status((HttpStatus)HttpStatus.CREATED).body((Object)yeniSiparis);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Sipari\u015f olu\u015fturulurken bir hata olu\u015ftu");
        }
    }

    @PutMapping(value={"/{id}/durum"})
    public ResponseEntity<?> updateSiparisDurum(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String yeniDurum = request.get("durum");
            Siparis guncellenenSiparis = this.siparisService.updateSiparisDurum(id, yeniDurum);
            return ResponseEntity.ok((Object)guncellenenSiparis);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Sipari\u015f durumu g\u00fcncellenirken bir hata olu\u015ftu");
        }
    }

    @PutMapping(value={"/{id}"})
    public ResponseEntity<?> updateSiparis(@PathVariable Long id, @Valid @RequestBody Siparis siparisDetaylari) {
        try {
            Siparis guncellenenSiparis = this.siparisService.updateSiparis(id, siparisDetaylari);
            return ResponseEntity.ok((Object)guncellenenSiparis);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Sipari\u015f g\u00fcncellenirken bir hata olu\u015ftu");
        }
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<?> deleteSiparis(@PathVariable Long id) {
        try {
            this.siparisService.deleteSiparis(id);
            return ResponseEntity.ok().body((Object)"Sipari\u015f ba\u015far\u0131yla silindi");
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Sipari\u015f silinirken bir hata olu\u015ftu");
        }
    }

    @GetMapping(value={"/kullanici/{kullaniciId}/count"})
    public ResponseEntity<Long> getSiparisCountByKullaniciId(@PathVariable Long kullaniciId) {
        try {
            long count = this.siparisService.getSiparisCountByKullaniciId(kullaniciId);
            return ResponseEntity.ok((Object)count);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/durum/{durum}/count"})
    public ResponseEntity<Long> getSiparisCountByDurum(@PathVariable String durum) {
        try {
            long count = this.siparisService.getSiparisCountByDurum(durum);
            return ResponseEntity.ok((Object)count);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/kullanici/{kullaniciId}/total-spent"})
    public ResponseEntity<BigDecimal> getTotalSpentByKullaniciId(@PathVariable Long kullaniciId) {
        try {
            BigDecimal totalSpent = this.siparisService.getTotalSpentByKullaniciId(kullaniciId);
            return ResponseEntity.ok((Object)totalSpent);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/stats/daily"})
    public ResponseEntity<List<Object[]>> getDailySalesStats() {
        try {
            List stats = this.siparisService.getDailySalesStats();
            return ResponseEntity.ok((Object)stats);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/stats/monthly"})
    public ResponseEntity<List<Object[]>> getMonthlySalesStats() {
        try {
            List stats = this.siparisService.getMonthlySalesStats();
            return ResponseEntity.ok((Object)stats);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/top-customers"})
    public ResponseEntity<List<Object[]>> getTopCustomers() {
        try {
            List topCustomers = this.siparisService.getTopCustomers();
            return ResponseEntity.ok((Object)topCustomers);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/count"})
    public ResponseEntity<Long> getSiparisCount() {
        try {
            long count = this.siparisService.getSiparisCount();
            return ResponseEntity.ok((Object)count);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

