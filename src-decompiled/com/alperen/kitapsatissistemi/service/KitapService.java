/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Kitap
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException
 *  com.alperen.kitapsatissistemi.repository.KategoriRepository
 *  com.alperen.kitapsatissistemi.repository.KitapRepository
 *  com.alperen.kitapsatissistemi.repository.SiparisDetayRepository
 *  com.alperen.kitapsatissistemi.service.KitapService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.util.StringUtils
 */
package com.alperen.kitapsatissistemi.service;

import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import com.alperen.kitapsatissistemi.repository.KategoriRepository;
import com.alperen.kitapsatissistemi.repository.KitapRepository;
import com.alperen.kitapsatissistemi.repository.SiparisDetayRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class KitapService {
    private final KitapRepository kitapRepository;
    private final KategoriRepository kategoriRepository;
    private final SiparisDetayRepository siparisDetayRepository;

    @Autowired
    public KitapService(KitapRepository kitapRepository, KategoriRepository kategoriRepository, SiparisDetayRepository siparisDetayRepository) {
        this.kitapRepository = kitapRepository;
        this.kategoriRepository = kategoriRepository;
        this.siparisDetayRepository = siparisDetayRepository;
    }

    @Transactional(readOnly=true)
    public List<Kitap> getAllKitaplar() {
        return this.kitapRepository.findAll();
    }

    @Transactional(readOnly=true)
    public List<Kitap> getAllKitaplarWithKategori() {
        return this.kitapRepository.findAllWithKategori();
    }

    @Transactional(readOnly=true)
    public Optional<Kitap> getKitapById(Long id) {
        return this.kitapRepository.findById((Object)id);
    }

    @Transactional(readOnly=true)
    public Optional<Kitap> getKitapByIdWithKategori(Long id) {
        return this.kitapRepository.findByIdWithKategori(id);
    }

    @Transactional(readOnly=true)
    public List<Kitap> searchKitaplarByAd(String ad) {
        return this.kitapRepository.findByAdContainingIgnoreCase(ad);
    }

    @Transactional(readOnly=true)
    public List<Kitap> searchKitaplarByYazar(String yazar) {
        return this.kitapRepository.findByYazarContainingIgnoreCase(yazar);
    }

    @Transactional(readOnly=true)
    public List<Kitap> searchKitaplarByAdAndYazar(String ad, String yazar) {
        return this.kitapRepository.findByAdAndYazar(ad, yazar);
    }

    @Transactional(readOnly=true)
    public List<Kitap> getKitaplarByKategoriId(Long kategoriId) {
        return this.kitapRepository.findByKategori_Id(kategoriId);
    }

    @Transactional(readOnly=true)
    public List<Kitap> getKitaplarByFiyatAraligi(BigDecimal minFiyat, BigDecimal maxFiyat) {
        return this.kitapRepository.findByFiyatBetween(minFiyat, maxFiyat);
    }

    public Kitap createKitap(Kitap kitap) {
        if (kitap == null) {
            throw new BusinessException("Kitap bilgileri bo\u015f olamaz");
        }
        if (!StringUtils.hasText((String)kitap.getAd())) {
            throw new BusinessException("Kitap ad\u0131 bo\u015f olamaz");
        }
        if (!StringUtils.hasText((String)kitap.getYazar())) {
            throw new BusinessException("Kitap yazar\u0131 bo\u015f olamaz");
        }
        if (kitap.getFiyat() == null || kitap.getFiyat().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Kitap fiyat\u0131 pozitif olmal\u0131d\u0131r");
        }
        if (kitap.getKategoriId() == null) {
            throw new BusinessException("Kategori ID'si bo\u015f olamaz");
        }
        if (!this.kategoriRepository.existsById((Object)kitap.getKategoriId())) {
            throw new EntityNotFoundBusinessException("Kategori", (Object)kitap.getKategoriId());
        }
        kitap.setAd(kitap.getAd().trim());
        kitap.setYazar(kitap.getYazar().trim());
        if (StringUtils.hasText((String)kitap.getAciklama())) {
            kitap.setAciklama(kitap.getAciklama().trim());
        }
        return (Kitap)this.kitapRepository.save((Object)kitap);
    }

    public Kitap updateKitap(Long id, Kitap kitapDetaylari) {
        if (id == null) {
            throw new BusinessException("Kitap ID'si bo\u015f olamaz");
        }
        if (kitapDetaylari == null) {
            throw new BusinessException("Kitap detaylar\u0131 bo\u015f olamaz");
        }
        if (!StringUtils.hasText((String)kitapDetaylari.getAd())) {
            throw new BusinessException("Kitap ad\u0131 bo\u015f olamaz");
        }
        if (!StringUtils.hasText((String)kitapDetaylari.getYazar())) {
            throw new BusinessException("Kitap yazar\u0131 bo\u015f olamaz");
        }
        if (kitapDetaylari.getFiyat() == null || kitapDetaylari.getFiyat().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Kitap fiyat\u0131 pozitif olmal\u0131d\u0131r");
        }
        if (kitapDetaylari.getKategoriId() == null) {
            throw new BusinessException("Kategori ID'si bo\u015f olamaz");
        }
        if (!this.kategoriRepository.existsById((Object)kitapDetaylari.getKategoriId())) {
            throw new EntityNotFoundBusinessException("Kategori", (Object)kitapDetaylari.getKategoriId());
        }
        return this.kitapRepository.findById((Object)id).map(kitap2 -> {
            kitap2.setAd(kitapDetaylari.getAd().trim());
            kitap2.setYazar(kitapDetaylari.getYazar().trim());
            kitap2.setFiyat(kitapDetaylari.getFiyat());
            if (StringUtils.hasText((String)kitapDetaylari.getAciklama())) {
                kitap2.setAciklama(kitapDetaylari.getAciklama().trim());
            }
            if (StringUtils.hasText((String)kitapDetaylari.getResimUrl())) {
                kitap2.setResimUrl(kitapDetaylari.getResimUrl().trim());
            }
            if (kitapDetaylari.getKategori() != null) {
                kitap2.setKategori(kitapDetaylari.getKategori());
            }
            return (Kitap)this.kitapRepository.save(kitap2);
        }).orElseThrow(() -> new EntityNotFoundBusinessException("Kitap", (Object)id));
    }

    public void deleteKitap(Long id) {
        if (id == null) {
            throw new BusinessException("Kitap ID'si bo\u015f olamaz");
        }
        if (!this.kitapRepository.existsById((Object)id)) {
            throw new EntityNotFoundBusinessException("Kitap", (Object)id);
        }
        if (this.siparisDetayRepository.countByKitap_Id(id) > 0L) {
            throw new BusinessException("Bu kitaba ait sipari\u015fler bulundu\u011fu i\u00e7in silinemez. Kitap sadece stoktan kald\u0131r\u0131labilir.");
        }
        this.kitapRepository.deleteById((Object)id);
    }

    @Transactional(readOnly=true)
    public boolean existsById(Long id) {
        return this.kitapRepository.existsById((Object)id);
    }

    @Transactional(readOnly=true)
    public List<Kitap> getEnPahaliKitaplar() {
        return this.kitapRepository.findTopByOrderByFiyatDesc();
    }

    @Transactional(readOnly=true)
    public List<Kitap> getEnUcuzKitaplar() {
        return this.kitapRepository.findTopByOrderByFiyatAsc();
    }

    @Transactional(readOnly=true)
    public List<Kitap> getKitaplarByFiyatGreaterThan(BigDecimal fiyat) {
        return this.kitapRepository.findByFiyatGreaterThan(fiyat);
    }

    @Transactional(readOnly=true)
    public List<Kitap> getKitaplarByFiyatLessThan(BigDecimal fiyat) {
        return this.kitapRepository.findByFiyatLessThan(fiyat);
    }

    @Transactional(readOnly=true)
    public List<Kitap> getKitaplarWithResim() {
        return this.kitapRepository.findKitaplarWithResim();
    }

    @Transactional(readOnly=true)
    public long getKitapCountByKategoriId(Long kategoriId) {
        return this.kitapRepository.countByKategori_Id(kategoriId);
    }

    @Transactional(readOnly=true)
    public long getKitapCount() {
        return this.kitapRepository.count();
    }

    @Transactional(readOnly=true)
    public List<Kitap> getLatestKitaplar(int limit) {
        return this.kitapRepository.findTopByOrderByIdDesc(limit);
    }

    @Transactional(readOnly=true)
    public long countByKategoriId(Long kategoriId) {
        return this.kitapRepository.countByKategori_Id(kategoriId);
    }

    @Transactional(readOnly=true)
    public List<Kitap> findAll() {
        return this.getAllKitaplar();
    }

    @Transactional(readOnly=true)
    public Page<Kitap> findAll(Pageable pageable) {
        return this.kitapRepository.findAll(pageable);
    }

    @Transactional(readOnly=true)
    public Optional<Kitap> findById(Long id) {
        return this.getKitapById(id);
    }

    @Transactional(readOnly=true)
    public Page<Kitap> findByAdContainingIgnoreCase(String ad, Pageable pageable) {
        return this.kitapRepository.findByAdContainingIgnoreCase(ad, pageable);
    }

    @Transactional(readOnly=true)
    public Page<Kitap> findByKategoriId(Long kategoriId, Pageable pageable) {
        return this.kitapRepository.findByKategori_Id(kategoriId, pageable);
    }

    @Transactional(readOnly=true)
    public Page<Kitap> findByAdContainingIgnoreCaseAndKategoriId(String ad, Long kategoriId, Pageable pageable) {
        return this.kitapRepository.findByAdContainingIgnoreCaseAndKategori_Id(ad, kategoriId, pageable);
    }

    @Transactional(readOnly=true)
    public List<Kitap> findByKategoriIdAndIdNot(Long kategoriId, Long excludeId) {
        return this.kitapRepository.findByKategori_IdAndIdNot(kategoriId, excludeId);
    }

    public Kitap save(Kitap kitap) {
        return this.createKitap(kitap);
    }

    public void deleteById(Long id) {
        this.deleteKitap(id);
    }

    @Transactional(readOnly=true)
    public long count() {
        return this.kitapRepository.count();
    }
}

