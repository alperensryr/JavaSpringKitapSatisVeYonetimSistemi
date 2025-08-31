/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.KullaniciController
 *  com.alperen.kitapsatissistemi.entity.Adres
 *  com.alperen.kitapsatissistemi.entity.Kullanici
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.service.AdresService
 *  com.alperen.kitapsatissistemi.service.KullaniciService
 *  javax.validation.Valid
 *  org.springframework.beans.factory.annotation.Autowired
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

import com.alperen.kitapsatissistemi.entity.Adres;
import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.service.AdresService;
import com.alperen.kitapsatissistemi.service.KullaniciService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value={"/api/kullanicilar"})
@CrossOrigin(origins={"*"})
public class KullaniciController {
    private final KullaniciService kullaniciService;
    private final AdresService adresService;

    @Autowired
    public KullaniciController(KullaniciService kullaniciService, AdresService adresService) {
        this.kullaniciService = kullaniciService;
        this.adresService = adresService;
    }

    @GetMapping
    public ResponseEntity<List<Kullanici>> getAllKullanicilar() {
        try {
            List kullanicilar = this.kullaniciService.getAllKullanicilar();
            kullanicilar.forEach(k -> k.setSifreHash(null));
            return ResponseEntity.ok((Object)kullanicilar);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<Kullanici> getKullaniciById(@PathVariable Long id) {
        try {
            Optional kullanici = this.kullaniciService.getKullaniciById(id);
            if (kullanici.isPresent()) {
                Kullanici k = (Kullanici)kullanici.get();
                k.setSifreHash(null);
                return ResponseEntity.ok((Object)k);
            }
            return ResponseEntity.notFound().build();
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/email/{email}"})
    public ResponseEntity<Kullanici> getKullaniciByEmail(@PathVariable String email) {
        try {
            Optional kullanici = this.kullaniciService.getKullaniciByEmail(email);
            if (kullanici.isPresent()) {
                Kullanici k = (Kullanici)kullanici.get();
                k.setSifreHash(null);
                return ResponseEntity.ok((Object)k);
            }
            return ResponseEntity.notFound().build();
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/rol/{rol}"})
    public ResponseEntity<List<Kullanici>> getKullanicilarByRol(@PathVariable String rol) {
        try {
            List kullanicilar = this.kullaniciService.getKullanicilarByRol(rol);
            kullanicilar.forEach(k -> k.setSifreHash(null));
            return ResponseEntity.ok((Object)kullanicilar);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/adminler"})
    public ResponseEntity<List<Kullanici>> getAdminKullanicilar() {
        try {
            List kullanicilar = this.kullaniciService.getAdminKullanicilar();
            kullanicilar.forEach(k -> k.setSifreHash(null));
            return ResponseEntity.ok((Object)kullanicilar);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/search"})
    public ResponseEntity<List<Kullanici>> searchKullanicilar(@RequestParam String q) {
        try {
            List kullanicilar = this.kullaniciService.searchKullanicilarByAdSoyad(q);
            kullanicilar.forEach(k -> k.setSifreHash(null));
            return ResponseEntity.ok((Object)kullanicilar);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value={"/register"})
    public ResponseEntity<?> registerKullanici(@Valid @RequestBody Map<String, Object> request) {
        try {
            Kullanici kullanici = new Kullanici();
            kullanici.setAdSoyad((String)request.get("adSoyad"));
            kullanici.setEmail((String)request.get("email"));
            kullanici.setRol((String)request.get("rol"));
            String sifre = (String)request.get("sifre");
            Kullanici yeniKullanici = this.kullaniciService.registerKullanici(kullanici, sifre);
            yeniKullanici.setSifreHash(null);
            return ResponseEntity.status((HttpStatus)HttpStatus.CREATED).body((Object)yeniKullanici);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Kullan\u0131c\u0131 kayd\u0131 s\u0131ras\u0131nda bir hata olu\u015ftu");
        }
    }

    @PostMapping(value={"/login"})
    public ResponseEntity<?> loginKullanici(@RequestBody Map<String, String> loginRequest) {
        try {
            String email = loginRequest.get("email");
            String sifre = loginRequest.get("sifre");
            Optional kullanici = this.kullaniciService.authenticateKullanici(email, sifre);
            if (kullanici.isPresent()) {
                Kullanici k = (Kullanici)kullanici.get();
                k.setSifreHash(null);
                return ResponseEntity.ok((Object)k);
            }
            return ResponseEntity.status((HttpStatus)HttpStatus.UNAUTHORIZED).body((Object)"Email veya \u015fifre hatal\u0131");
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Giri\u015f s\u0131ras\u0131nda bir hata olu\u015ftu");
        }
    }

    @PutMapping(value={"/{id}"})
    public ResponseEntity<?> updateKullanici(@PathVariable Long id, @Valid @RequestBody Kullanici kullaniciDetaylari) {
        try {
            Kullanici guncellenenKullanici = this.kullaniciService.updateKullanici(id, kullaniciDetaylari);
            guncellenenKullanici.setSifreHash(null);
            return ResponseEntity.ok((Object)guncellenenKullanici);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Kullan\u0131c\u0131 g\u00fcncellenirken bir hata olu\u015ftu");
        }
    }

    @PutMapping(value={"/{id}/change-password"})
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody Map<String, String> passwordRequest) {
        try {
            String eskiSifre = passwordRequest.get("eskiSifre");
            String yeniSifre = passwordRequest.get("yeniSifre");
            this.kullaniciService.changePassword(id, eskiSifre, yeniSifre);
            return ResponseEntity.ok().body((Object)"\u015eifre ba\u015far\u0131yla de\u011fi\u015ftirildi");
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"\u015eifre de\u011fi\u015ftirilirken bir hata olu\u015ftu");
        }
    }

    @PutMapping(value={"/{id}/reset-password"})
    public ResponseEntity<?> resetPassword(@PathVariable Long id, @RequestBody Map<String, String> passwordRequest) {
        try {
            String yeniSifre = passwordRequest.get("yeniSifre");
            this.kullaniciService.resetPassword(id, yeniSifre);
            return ResponseEntity.ok().body((Object)"\u015eifre ba\u015far\u0131yla s\u0131f\u0131rland\u0131");
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"\u015eifre s\u0131f\u0131rlan\u0131rken bir hata olu\u015ftu");
        }
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<?> deleteKullanici(@PathVariable Long id) {
        try {
            this.kullaniciService.deleteKullanici(id);
            return ResponseEntity.ok().body((Object)"Kullan\u0131c\u0131 ba\u015far\u0131yla silindi");
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Kullan\u0131c\u0131 silinirken bir hata olu\u015ftu");
        }
    }

    @GetMapping(value={"/check-email"})
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        try {
            boolean exists = this.kullaniciService.existsByEmail(email);
            return ResponseEntity.ok((Object)exists);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/count"})
    public ResponseEntity<Long> getKullaniciCount() {
        try {
            long count = this.kullaniciService.getKullaniciCount();
            return ResponseEntity.ok((Object)count);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/count/rol/{rol}"})
    public ResponseEntity<Long> getKullaniciCountByRol(@PathVariable String rol) {
        try {
            long count = this.kullaniciService.getKullaniciCountByRol(rol);
            return ResponseEntity.ok((Object)count);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/{id}/adresler"})
    public ResponseEntity<List<Adres>> getKullaniciAdresler(@PathVariable Long id) {
        try {
            List adresler = this.adresService.getAdreslerByKullaniciId(id);
            return ResponseEntity.ok((Object)adresler);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value={"/{id}/adresler"})
    public ResponseEntity<?> addKullaniciAdres(@PathVariable Long id, @Valid @RequestBody Adres adres) {
        try {
            Kullanici kullanici = (Kullanici)this.kullaniciService.getKullaniciById(id).orElseThrow(() -> new BusinessException("Kullan\u0131c\u0131 bulunamad\u0131"));
            adres.setKullanici(kullanici);
            Adres yeniAdres = this.adresService.createAdres(kullanici, adres);
            return ResponseEntity.status((HttpStatus)HttpStatus.CREATED).body((Object)yeniAdres);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Adres eklenirken bir hata olu\u015ftu");
        }
    }

    @PutMapping(value={"/{kullaniciId}/adresler/{adresId}"})
    public ResponseEntity<?> updateKullaniciAdres(@PathVariable Long kullaniciId, @PathVariable Long adresId, @Valid @RequestBody Adres adresDetaylari) {
        try {
            Kullanici kullanici = (Kullanici)this.kullaniciService.getKullaniciById(kullaniciId).orElseThrow(() -> new BusinessException("Kullan\u0131c\u0131 bulunamad\u0131"));
            adresDetaylari.setKullanici(kullanici);
            Adres guncelAdres = this.adresService.updateAdres(adresId, adresDetaylari);
            return ResponseEntity.ok((Object)guncelAdres);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Adres g\u00fcncellenirken bir hata olu\u015ftu");
        }
    }

    @DeleteMapping(value={"/{kullaniciId}/adresler/{adresId}"})
    public ResponseEntity<?> deleteKullaniciAdres(@PathVariable Long kullaniciId, @PathVariable Long adresId) {
        try {
            this.adresService.deleteAdres(adresId);
            return ResponseEntity.ok().build();
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Adres silinirken bir hata olu\u015ftu");
        }
    }

    @PutMapping(value={"/{kullaniciId}/adresler/{adresId}/varsayilan"})
    public ResponseEntity<?> setVarsayilanAdres(@PathVariable Long kullaniciId, @PathVariable Long adresId) {
        try {
            this.adresService.setVarsayilanAdres(kullaniciId, adresId);
            return ResponseEntity.ok().build();
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Varsay\u0131lan adres ayarlan\u0131rken bir hata olu\u015ftu");
        }
    }
}

