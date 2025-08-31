/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.AdresController
 *  com.alperen.kitapsatissistemi.entity.Adres
 *  com.alperen.kitapsatissistemi.entity.Kullanici
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException
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
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import com.alperen.kitapsatissistemi.service.AdresService;
import com.alperen.kitapsatissistemi.service.KullaniciService;
import java.util.List;
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
@RequestMapping(value={"/api/adresler"})
@CrossOrigin(origins={"*"})
public class AdresController {
    private final AdresService adresService;
    private final KullaniciService kullaniciService;

    @Autowired
    public AdresController(AdresService adresService, KullaniciService kullaniciService) {
        this.adresService = adresService;
        this.kullaniciService = kullaniciService;
    }

    @GetMapping(value={"/kullanici/{kullaniciId}"})
    public ResponseEntity<List<Adres>> getAdreslerByKullaniciId(@PathVariable Long kullaniciId) {
        try {
            List adresler = this.adresService.getAdreslerByKullaniciId(kullaniciId);
            return ResponseEntity.ok((Object)adresler);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<Adres> getAdresById(@PathVariable Long id) {
        try {
            Optional adres = this.adresService.getAdresById(id);
            return adres.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/kullanici/{kullaniciId}/varsayilan"})
    public ResponseEntity<Adres> getVarsayilanAdres(@PathVariable Long kullaniciId) {
        try {
            Optional adres = this.adresService.getVarsayilanAdres(kullaniciId);
            return adres.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value={"/kullanici/{kullaniciId}"})
    public ResponseEntity<?> createAdres(@PathVariable Long kullaniciId, @Valid @RequestBody Adres adres) {
        try {
            Kullanici kullanici = (Kullanici)this.kullaniciService.findById(kullaniciId).orElseThrow(() -> new EntityNotFoundBusinessException("Kullan\u0131c\u0131", (Object)kullaniciId));
            Adres yeniAdres = this.adresService.createAdres(kullanici, adres);
            return ResponseEntity.status((HttpStatus)HttpStatus.CREATED).body((Object)yeniAdres);
        }
        catch (EntityNotFoundBusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Adres olu\u015fturulurken bir hata olu\u015ftu");
        }
    }

    @PutMapping(value={"/{id}"})
    public ResponseEntity<?> updateAdres(@PathVariable Long id, @Valid @RequestBody Adres adresDetaylari) {
        try {
            Adres guncelAdres = this.adresService.updateAdres(id, adresDetaylari);
            return ResponseEntity.ok((Object)guncelAdres);
        }
        catch (EntityNotFoundBusinessException e) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Adres g\u00fcncellenirken bir hata olu\u015ftu");
        }
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<?> deleteAdres(@PathVariable Long id) {
        try {
            this.adresService.deleteAdres(id);
            return ResponseEntity.ok().body((Object)"Adres ba\u015far\u0131yla silindi");
        }
        catch (EntityNotFoundBusinessException e) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Adres silinirken bir hata olu\u015ftu");
        }
    }

    @PutMapping(value={"/{id}/varsayilan"})
    public ResponseEntity<?> setVarsayilanAdres(@PathVariable Long id, @RequestParam Long kullaniciId) {
        try {
            Adres adres = this.adresService.setVarsayilanAdres(kullaniciId, id);
            return ResponseEntity.ok((Object)adres);
        }
        catch (EntityNotFoundBusinessException e) {
            return ResponseEntity.notFound().build();
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Varsay\u0131lan adres belirlenirken bir hata olu\u015ftu");
        }
    }

    @GetMapping(value={"/sehir/{sehir}"})
    public ResponseEntity<List<Adres>> getAdreslerBySehir(@PathVariable String sehir) {
        try {
            List adresler = this.adresService.getAdreslerBySehir(sehir);
            return ResponseEntity.ok((Object)adresler);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/kullanici/{kullaniciId}/count"})
    public ResponseEntity<Long> getAdresSayisi(@PathVariable Long kullaniciId) {
        try {
            long adresSayisi = this.adresService.getAdresSayisi(kullaniciId);
            return ResponseEntity.ok((Object)adresSayisi);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

