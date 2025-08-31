/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.KitapController
 *  com.alperen.kitapsatissistemi.entity.Kitap
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.exception.DuplicateEntityException
 *  com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException
 *  com.alperen.kitapsatissistemi.service.KategoriService
 *  com.alperen.kitapsatissistemi.service.KitapService
 *  javax.validation.Valid
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.ResponseEntity
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
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.exception.DuplicateEntityException;
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import com.alperen.kitapsatissistemi.service.KategoriService;
import com.alperen.kitapsatissistemi.service.KitapService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(value={"/api/kitaplar"})
public class KitapController {
    @Autowired
    private KitapService kitapService;
    @Autowired
    private KategoriService kategoriService;

    @GetMapping
    public ResponseEntity<List<Kitap>> getAllKitaplar(@RequestParam(defaultValue="false") boolean withKategori) {
        try {
            List kitaplar = withKategori ? this.kitapService.getAllKitaplarWithKategori() : this.kitapService.getAllKitaplar();
            return ResponseEntity.ok((Object)kitaplar);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<Kitap> getKitapById(@PathVariable Long id, @RequestParam(defaultValue="false") boolean withKategori) {
        try {
            Optional kitap = withKategori ? this.kitapService.getKitapByIdWithKategori(id) : this.kitapService.getKitapById(id);
            return kitap.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/search/ad"})
    public ResponseEntity<List<Kitap>> searchKitaplarByAd(@RequestParam String q) {
        try {
            List kitaplar = this.kitapService.searchKitaplarByAd(q);
            return ResponseEntity.ok((Object)kitaplar);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/search/yazar"})
    public ResponseEntity<List<Kitap>> searchKitaplarByYazar(@RequestParam String q) {
        try {
            List kitaplar = this.kitapService.searchKitaplarByYazar(q);
            return ResponseEntity.ok((Object)kitaplar);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/kategori/{kategoriId}"})
    public ResponseEntity<List<Kitap>> getKitaplarByKategoriId(@PathVariable Long kategoriId) {
        try {
            List kitaplar = this.kitapService.getKitaplarByKategoriId(kategoriId);
            return ResponseEntity.ok((Object)kitaplar);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/fiyat-araligi"})
    public ResponseEntity<List<Kitap>> getKitaplarByFiyatAraligi(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        try {
            List kitaplar = this.kitapService.getKitaplarByFiyatAraligi(min, max);
            return ResponseEntity.ok((Object)kitaplar);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/en-pahali"})
    public ResponseEntity<List<Kitap>> getEnPahaliKitaplar() {
        try {
            List kitaplar = this.kitapService.getEnPahaliKitaplar();
            return ResponseEntity.ok((Object)kitaplar);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/en-ucuz"})
    public ResponseEntity<List<Kitap>> getEnUcuzKitaplar() {
        try {
            List kitaplar = this.kitapService.getEnUcuzKitaplar();
            return ResponseEntity.ok((Object)kitaplar);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/with-resim"})
    public ResponseEntity<List<Kitap>> getKitaplarWithResim() {
        try {
            List kitaplar = this.kitapService.getKitaplarWithResim();
            return ResponseEntity.ok((Object)kitaplar);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createKitap(@Valid @RequestBody Kitap kitap) {
        try {
            Kitap yeniKitap = this.kitapService.createKitap(kitap);
            return ResponseEntity.status((HttpStatus)HttpStatus.CREATED).body((Object)yeniKitap);
        }
        catch (DuplicateEntityException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Kitap olu\u015fturulurken bir hata olu\u015ftu");
        }
    }

    @PutMapping(value={"/{id}"})
    public ResponseEntity<?> updateKitap(@PathVariable Long id, @Valid @RequestBody Kitap kitapDetaylari) {
        try {
            Kitap guncellenenKitap = this.kitapService.updateKitap(id, kitapDetaylari);
            return ResponseEntity.ok((Object)guncellenenKitap);
        }
        catch (EntityNotFoundBusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (DuplicateEntityException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Kitap g\u00fcncellenirken bir hata olu\u015ftu");
        }
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<?> deleteKitap(@PathVariable Long id) {
        try {
            this.kitapService.deleteKitap(id);
            return ResponseEntity.ok().body((Object)"Kitap ba\u015far\u0131yla silindi");
        }
        catch (EntityNotFoundBusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Kitap silinirken bir hata olu\u015ftu");
        }
    }

    @GetMapping(value={"/{id}/exists"})
    public ResponseEntity<Boolean> checkKitapExists(@PathVariable Long id) {
        try {
            boolean exists = this.kitapService.existsById(id);
            return ResponseEntity.ok((Object)exists);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/kategori/{kategoriId}/count"})
    public ResponseEntity<Long> getKitapCountByKategoriId(@PathVariable Long kategoriId) {
        try {
            long count = this.kitapService.getKitapCountByKategoriId(kategoriId);
            return ResponseEntity.ok((Object)count);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/count"})
    public ResponseEntity<Long> getKitapCount() {
        try {
            long count = this.kitapService.getKitapCount();
            return ResponseEntity.ok((Object)count);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

