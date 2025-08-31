/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.KategoriController
 *  com.alperen.kitapsatissistemi.entity.Kategori
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.service.KategoriService
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
 *  org.springframework.web.bind.annotation.ResponseBody
 *  org.springframework.web.bind.annotation.RestController
 */
package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Kategori;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.service.KategoriService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/kategoriler"})
public class KategoriController {
    private final KategoriService kategoriService;

    @Autowired
    public KategoriController(KategoriService kategoriService) {
        this.kategoriService = kategoriService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Kategori>> getAllKategoriler() {
        try {
            List kategoriler = this.kategoriService.getAllKategoriler();
            return ResponseEntity.ok((Object)kategoriler);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/{id}"})
    @ResponseBody
    public ResponseEntity<Kategori> getKategoriById(@PathVariable Long id) {
        try {
            Optional kategori = this.kategoriService.getKategoriById(id);
            return kategori.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/search"})
    @ResponseBody
    public ResponseEntity<List<Kategori>> searchKategoriler(@RequestParam String ad) {
        try {
            List kategoriler = this.kategoriService.searchKategorilerByAd(ad);
            return ResponseEntity.ok((Object)kategoriler);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/with-aciklama"})
    @ResponseBody
    public ResponseEntity<List<Kategori>> getKategorilerWithAciklama() {
        try {
            List kategoriler = this.kategoriService.getKategorilerWithAciklama();
            return ResponseEntity.ok((Object)kategoriler);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> createKategori(@Valid @RequestBody Kategori kategori) {
        try {
            Kategori yeniKategori = this.kategoriService.createKategori(kategori);
            return ResponseEntity.status((HttpStatus)HttpStatus.CREATED).body((Object)yeniKategori);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Kategori olu\u015fturulurken bir hata olu\u015ftu");
        }
    }

    @PutMapping(value={"/{id}"})
    @ResponseBody
    public ResponseEntity<?> updateKategori(@PathVariable Long id, @Valid @RequestBody Kategori kategoriDetaylari) {
        try {
            Kategori guncellenenKategori = this.kategoriService.updateKategori(id, kategoriDetaylari);
            return ResponseEntity.ok((Object)guncellenenKategori);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Kategori g\u00fcncellenirken bir hata olu\u015ftu");
        }
    }

    @DeleteMapping(value={"/{id}"})
    @ResponseBody
    public ResponseEntity<?> deleteKategori(@PathVariable Long id) {
        try {
            this.kategoriService.deleteKategori(id);
            return ResponseEntity.ok().body((Object)"Kategori ba\u015far\u0131yla silindi");
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Kategori silinirken bir hata olu\u015ftu");
        }
    }

    @GetMapping(value={"/{id}/exists"})
    @ResponseBody
    public ResponseEntity<Boolean> checkKategoriExists(@PathVariable Long id) {
        try {
            boolean exists = this.kategoriService.existsById(id);
            return ResponseEntity.ok((Object)exists);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().build();
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/check-name"})
    @ResponseBody
    public ResponseEntity<Boolean> checkKategoriAdExists(@RequestParam String ad) {
        try {
            boolean exists = this.kategoriService.existsByAd(ad);
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
    @ResponseBody
    public ResponseEntity<Long> getKategoriCount() {
        try {
            long count = this.kategoriService.getKategoriCount();
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

