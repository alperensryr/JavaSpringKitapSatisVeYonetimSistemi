/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.controller.SiparisDetayController
 *  com.alperen.kitapsatissistemi.entity.SiparisDetay
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.service.SiparisDetayService
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

import com.alperen.kitapsatissistemi.entity.SiparisDetay;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.service.SiparisDetayService;
import java.math.BigDecimal;
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
@RequestMapping(value={"/api/siparis-detaylar"})
@CrossOrigin(origins={"*"})
public class SiparisDetayController {
    private final SiparisDetayService siparisDetayService;

    @Autowired
    public SiparisDetayController(SiparisDetayService siparisDetayService) {
        this.siparisDetayService = siparisDetayService;
    }

    @GetMapping
    public ResponseEntity<List<SiparisDetay>> getAllSiparisDetaylar(@RequestParam(defaultValue="false") boolean withDetails) {
        try {
            List siparisDetaylar = withDetails ? this.siparisDetayService.getAllSiparisDetaylarWithDetails() : this.siparisDetayService.getAllSiparisDetaylar();
            return ResponseEntity.ok((Object)siparisDetaylar);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<SiparisDetay> getSiparisDetayById(@PathVariable Long id) {
        try {
            Optional siparisDetay = this.siparisDetayService.getSiparisDetayById(id);
            return siparisDetay.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/siparis/{siparisId}"})
    public ResponseEntity<List<SiparisDetay>> getSiparisDetaylarBySiparisId(@PathVariable Long siparisId, @RequestParam(defaultValue="false") boolean withKitap) {
        try {
            List siparisDetaylar = withKitap ? this.siparisDetayService.getSiparisDetaylarBySiparisIdWithKitap(siparisId) : this.siparisDetayService.getSiparisDetaylarBySiparisId(siparisId);
            return ResponseEntity.ok((Object)siparisDetaylar);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/kitap/{kitapId}"})
    public ResponseEntity<List<SiparisDetay>> getSiparisDetaylarByKitapId(@PathVariable Long kitapId, @RequestParam(defaultValue="false") boolean withSiparis) {
        try {
            List siparisDetaylar = withSiparis ? this.siparisDetayService.getSiparisDetaylarByKitapIdWithSiparis(kitapId) : this.siparisDetayService.getSiparisDetaylarByKitapId(kitapId);
            return ResponseEntity.ok((Object)siparisDetaylar);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/siparis/{siparisId}/kitap/{kitapId}"})
    public ResponseEntity<SiparisDetay> getSiparisDetayBySiparisIdAndKitapId(@PathVariable Long siparisId, @PathVariable Long kitapId) {
        try {
            Optional siparisDetay = this.siparisDetayService.getSiparisDetayBySiparisIdAndKitapId(siparisId, kitapId);
            return siparisDetay.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createSiparisDetay(@Valid @RequestBody SiparisDetay siparisDetay) {
        try {
            SiparisDetay yeniSiparisDetay = this.siparisDetayService.createSiparisDetay(siparisDetay);
            return ResponseEntity.status((HttpStatus)HttpStatus.CREATED).body((Object)yeniSiparisDetay);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Sipari\u015f detay\u0131 olu\u015fturulurken bir hata olu\u015ftu");
        }
    }

    @PutMapping(value={"/{id}"})
    public ResponseEntity<?> updateSiparisDetay(@PathVariable Long id, @Valid @RequestBody SiparisDetay siparisDetayDetaylari) {
        try {
            SiparisDetay guncellenenSiparisDetay = this.siparisDetayService.updateSiparisDetay(id, siparisDetayDetaylari);
            return ResponseEntity.ok((Object)guncellenenSiparisDetay);
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Sipari\u015f detay\u0131 g\u00fcncellenirken bir hata olu\u015ftu");
        }
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<?> deleteSiparisDetay(@PathVariable Long id) {
        try {
            this.siparisDetayService.deleteSiparisDetay(id);
            return ResponseEntity.ok().body((Object)"Sipari\u015f detay\u0131 ba\u015far\u0131yla silindi");
        }
        catch (BusinessException e) {
            return ResponseEntity.badRequest().body((Object)e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Sipari\u015f detay\u0131 silinirken bir hata olu\u015ftu");
        }
    }

    @DeleteMapping(value={"/siparis/{siparisId}"})
    public ResponseEntity<?> deleteAllSiparisDetaylarBySiparisId(@PathVariable Long siparisId) {
        try {
            this.siparisDetayService.deleteAllSiparisDetaylarBySiparisId(siparisId);
            return ResponseEntity.ok().body((Object)"Sipari\u015fin t\u00fcm detaylar\u0131 ba\u015far\u0131yla silindi");
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)"Sipari\u015f detaylar\u0131 silinirken bir hata olu\u015ftu");
        }
    }

    @GetMapping(value={"/kitap/{kitapId}/total-quantity"})
    public ResponseEntity<Integer> getTotalSalesQuantityByKitapId(@PathVariable Long kitapId) {
        try {
            Integer totalQuantity = this.siparisDetayService.getTotalSalesQuantityByKitapId(kitapId);
            return ResponseEntity.ok((Object)totalQuantity);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/kitap/{kitapId}/total-amount"})
    public ResponseEntity<BigDecimal> getTotalSalesAmountByKitapId(@PathVariable Long kitapId) {
        try {
            BigDecimal totalAmount = this.siparisDetayService.getTotalSalesAmountByKitapId(kitapId);
            return ResponseEntity.ok((Object)totalAmount);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/best-selling-books"})
    public ResponseEntity<List<Object[]>> getBestSellingBooks() {
        try {
            List bestSellingBooks = this.siparisDetayService.getBestSellingBooks();
            return ResponseEntity.ok((Object)bestSellingBooks);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/highest-earning-books"})
    public ResponseEntity<List<Object[]>> getHighestEarningBooks() {
        try {
            List highestEarningBooks = this.siparisDetayService.getHighestEarningBooks();
            return ResponseEntity.ok((Object)highestEarningBooks);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/siparis/{siparisId}/total-amount"})
    public ResponseEntity<BigDecimal> calculateTotalOrderAmount(@PathVariable Long siparisId) {
        try {
            BigDecimal totalAmount = this.siparisDetayService.calculateTotalOrderAmount(siparisId);
            return ResponseEntity.ok((Object)totalAmount);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/stats/by-category"})
    public ResponseEntity<List<Object[]>> getSalesStatsByCategory() {
        try {
            List stats = this.siparisDetayService.getSalesStatsByCategory();
            return ResponseEntity.ok((Object)stats);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/stats/by-author"})
    public ResponseEntity<List<Object[]>> getSalesStatsByAuthor() {
        try {
            List stats = this.siparisDetayService.getSalesStatsByAuthor();
            return ResponseEntity.ok((Object)stats);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/siparis/{siparisId}/count"})
    public ResponseEntity<Long> getSiparisDetayCountBySiparisId(@PathVariable Long siparisId) {
        try {
            long count = this.siparisDetayService.getSiparisDetayCountBySiparisId(siparisId);
            return ResponseEntity.ok((Object)count);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/kitap/{kitapId}/count"})
    public ResponseEntity<Long> getSiparisDetayCountByKitapId(@PathVariable Long kitapId) {
        try {
            long count = this.siparisDetayService.getSiparisDetayCountByKitapId(kitapId);
            return ResponseEntity.ok((Object)count);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value={"/count"})
    public ResponseEntity<Long> getSiparisDetayCount() {
        try {
            long count = this.siparisDetayService.getSiparisDetayCount();
            return ResponseEntity.ok((Object)count);
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatus)HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

