/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.FavoriController
 *  com.alperen.kitapsatissistemi.entity.Favori
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.service.FavoriService
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.alperen.kitapsatissistemi.controller;

import com.alperen.kitapsatissistemi.entity.Favori;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.service.FavoriService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/favoriler"})
@CrossOrigin(origins={"*"})
public class FavoriController {
    private final FavoriService favoriService;

    public FavoriController(FavoriService favoriService) {
        this.favoriService = favoriService;
    }

    @GetMapping
    public ResponseEntity<List<Favori>> getAllFavoriler(@RequestParam(defaultValue="false") boolean withDetails) {
        try {
            List favoriler = withDetails ? this.favoriService.getAllFavorilerWithDetails() : this.favoriService.getAllFavoriler();
            return ResponseEntity.ok((Object)favoriler);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<Favori> getFavoriById(@PathVariable Long id) {
        try {
            Optional favori = this.favoriService.getFavoriById(id);
            return favori.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/kullanici/{kullaniciId}"})
    public ResponseEntity<List<Favori>> getFavorilerByKullaniciId(@PathVariable Long kullaniciId, @RequestParam(defaultValue="false") boolean withDetails) {
        try {
            List favoriler = withDetails ? this.favoriService.getFavorilerByKullaniciIdWithDetails(kullaniciId) : this.favoriService.getFavorilerByKullaniciId(kullaniciId);
            return ResponseEntity.ok((Object)favoriler);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/kitap/{kitapId}"})
    public ResponseEntity<List<Favori>> getFavorilerByKitapId(@PathVariable Long kitapId, @RequestParam(defaultValue="false") boolean withDetails) {
        try {
            List favoriler = withDetails ? this.favoriService.getFavorilerByKitapIdWithDetails(kitapId) : this.favoriService.getFavorilerByKitapId(kitapId);
            return ResponseEntity.ok((Object)favoriler);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addFavori(@RequestBody Map<String, Long> request) {
        try {
            Long kullaniciId = request.get("kullaniciId");
            Long kitapId = request.get("kitapId");
            Favori yeniFavori = this.favoriService.addFavori(kullaniciId, kitapId);
            return ResponseEntity.status((HttpStatus)HttpStatus.CREATED).body((Object)yeniFavori);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Favori eklenirken bir hata olu\u015ftu");
        }
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<?> deleteFavori(@PathVariable Long id) {
        try {
            this.favoriService.deleteFavori(id);
            return ResponseEntity.ok().body((Object)"Favori ba\u015far\u0131yla silindi");
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Favori silinirken bir hata olu\u015ftu");
        }
    }

    @DeleteMapping(value={"/kullanici/{kullaniciId}/kitap/{kitapId}"})
    public ResponseEntity<?> deleteFavoriByKullaniciAndKitap(@PathVariable Long kullaniciId, @PathVariable Long kitapId) {
        try {
            this.favoriService.deleteFavoriByKullaniciAndKitap(kullaniciId, kitapId);
            return ResponseEntity.ok().body((Object)"Favori ba\u015far\u0131yla silindi");
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Favori silinirken bir hata olu\u015ftu");
        }
    }

    @DeleteMapping(value={"/kullanici/{kullaniciId}"})
    public ResponseEntity<?> deleteAllFavorilerByKullaniciId(@PathVariable Long kullaniciId) {
        try {
            this.favoriService.deleteAllFavorilerByKullaniciId(kullaniciId);
            return ResponseEntity.ok().body((Object)"Kullan\u0131c\u0131n\u0131n t\u00fcm favorileri ba\u015far\u0131yla silindi");
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Favoriler silinirken bir hata olu\u015ftu");
        }
    }

    @DeleteMapping(value={"/kitap/{kitapId}"})
    public ResponseEntity<?> deleteAllFavorilerByKitapId(@PathVariable Long kitapId) {
        try {
            this.favoriService.deleteAllFavorilerByKitapId(kitapId);
            return ResponseEntity.ok().body((Object)"Kitab\u0131n t\u00fcm favorileri ba\u015far\u0131yla silindi");
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Favoriler silinirken bir hata olu\u015ftu");
        }
    }

    @GetMapping(value={"/check"})
    public ResponseEntity<Boolean> checkFavoriExists(@RequestParam Long kullaniciId, @RequestParam Long kitapId) {
        try {
            boolean exists = this.favoriService.existsByKullaniciIdAndKitapId(kullaniciId, kitapId);
            return ResponseEntity.ok((Object)exists);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/kullanici/{kullaniciId}/count"})
    public ResponseEntity<Long> getFavoriCountByKullaniciId(@PathVariable Long kullaniciId) {
        try {
            long count = this.favoriService.getFavoriCountByKullaniciId(kullaniciId);
            return ResponseEntity.ok((Object)count);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/kitap/{kitapId}/count"})
    public ResponseEntity<Long> getFavoriCountByKitapId(@PathVariable Long kitapId) {
        try {
            long count = this.favoriService.getFavoriCountByKitapId(kitapId);
            return ResponseEntity.ok((Object)count);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/most-favorited-books"})
    public ResponseEntity<List<Object[]>> getEnCokFavorilenenKitaplar() {
        try {
            List result = this.favoriService.getEnCokFavorilenenKitaplar();
            return ResponseEntity.ok((Object)result);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/most-active-users"})
    public ResponseEntity<List<Object[]>> getEnAktifKullanicilar() {
        try {
            List result = this.favoriService.getEnAktifKullanicilar();
            return ResponseEntity.ok((Object)result);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/count"})
    public ResponseEntity<Long> getFavoriCount() {
        try {
            long count = this.favoriService.getFavoriCount();
            return ResponseEntity.ok((Object)count);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

