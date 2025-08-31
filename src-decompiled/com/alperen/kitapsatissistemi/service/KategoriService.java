/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Kategori
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.exception.DuplicateEntityException
 *  com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException
 *  com.alperen.kitapsatissistemi.repository.KategoriRepository
 *  com.alperen.kitapsatissistemi.repository.KitapRepository
 *  com.alperen.kitapsatissistemi.service.KategoriService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.util.StringUtils
 */
package com.alperen.kitapsatissistemi.service;

import com.alperen.kitapsatissistemi.entity.Kategori;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.exception.DuplicateEntityException;
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import com.alperen.kitapsatissistemi.repository.KategoriRepository;
import com.alperen.kitapsatissistemi.repository.KitapRepository;
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
public class KategoriService {
    private final KategoriRepository kategoriRepository;
    private final KitapRepository kitapRepository;

    @Autowired
    public KategoriService(KategoriRepository kategoriRepository, KitapRepository kitapRepository) {
        this.kategoriRepository = kategoriRepository;
        this.kitapRepository = kitapRepository;
    }

    @Transactional(readOnly=true)
    public List<Kategori> getAllKategoriler() {
        return this.kategoriRepository.findAll();
    }

    @Transactional(readOnly=true)
    public Optional<Kategori> getKategoriById(Long id) {
        return this.kategoriRepository.findById((Object)id);
    }

    @Transactional(readOnly=true)
    public Optional<Kategori> getKategoriByAd(String ad) {
        return this.kategoriRepository.findByAd(ad);
    }

    @Transactional(readOnly=true)
    public List<Kategori> searchKategorilerByAd(String ad) {
        return this.kategoriRepository.findByAdContainingIgnoreCase(ad);
    }

    public Kategori createKategori(Kategori kategori) {
        if (kategori == null) {
            throw new BusinessException("Kategori bilgisi bo\u015f olamaz");
        }
        if (!StringUtils.hasText((String)kategori.getAd())) {
            throw new BusinessException("Kategori ad\u0131 bo\u015f olamaz");
        }
        kategori.setAd(kategori.getAd().trim());
        if (this.kategoriRepository.existsByAd(kategori.getAd())) {
            throw new DuplicateEntityException("Kategori", "ad", (Object)kategori.getAd());
        }
        return (Kategori)this.kategoriRepository.save((Object)kategori);
    }

    public Kategori updateKategori(Long id, Kategori kategoriDetaylari) {
        if (id == null) {
            throw new BusinessException("Kategori ID'si bo\u015f olamaz");
        }
        if (kategoriDetaylari == null) {
            throw new BusinessException("Kategori bilgisi bo\u015f olamaz");
        }
        if (!StringUtils.hasText((String)kategoriDetaylari.getAd())) {
            throw new BusinessException("Kategori ad\u0131 bo\u015f olamaz");
        }
        kategoriDetaylari.setAd(kategoriDetaylari.getAd().trim());
        return this.kategoriRepository.findById((Object)id).map(kategori2 -> {
            if (this.kategoriRepository.existsByAdAndIdNot(kategoriDetaylari.getAd(), id)) {
                throw new DuplicateEntityException("Kategori", "ad", (Object)kategoriDetaylari.getAd());
            }
            kategori2.setAd(kategoriDetaylari.getAd());
            kategori2.setAciklama(kategoriDetaylari.getAciklama());
            return (Kategori)this.kategoriRepository.save(kategori2);
        }).orElseThrow(() -> new EntityNotFoundBusinessException("Kategori", (Object)id));
    }

    public void deleteKategori(Long id) {
        if (id == null) {
            throw new BusinessException("Kategori ID'si bo\u015f olamaz");
        }
        Kategori kategori = (Kategori)this.kategoriRepository.findById((Object)id).orElseThrow(() -> new EntityNotFoundBusinessException("Kategori", (Object)id));
        long kitapSayisi = this.kitapRepository.countByKategori_Id(kategori.getId());
        if (kitapSayisi > 0L) {
            throw new BusinessException("Bu kategoriye ait kitaplar bulundu\u011fu i\u00e7in silinemez. \u00d6nce kitaplar\u0131 ba\u015fka kategoriye ta\u015f\u0131y\u0131n.");
        }
        this.kategoriRepository.deleteById((Object)id);
    }

    @Transactional(readOnly=true)
    public boolean existsById(Long id) {
        return this.kategoriRepository.existsById((Object)id);
    }

    @Transactional(readOnly=true)
    public boolean existsByAd(String ad) {
        return this.kategoriRepository.existsByAd(ad);
    }

    @Transactional(readOnly=true)
    public List<Kategori> getKategorilerWithAciklama() {
        return this.kategoriRepository.findKategorilerWithAciklama();
    }

    @Transactional(readOnly=true)
    public long count() {
        return this.kategoriRepository.count();
    }

    @Transactional(readOnly=true)
    public List<Kategori> findByAdContainingIgnoreCase(String ad) {
        return this.kategoriRepository.findByAdContainingIgnoreCase(ad);
    }

    public Kategori save(Kategori kategori) {
        return (Kategori)this.kategoriRepository.save((Object)kategori);
    }

    public void deleteById(Long id) {
        this.kategoriRepository.deleteById((Object)id);
    }

    @Transactional(readOnly=true)
    public List<Kategori> findAll() {
        return this.getAllKategoriler();
    }

    @Transactional(readOnly=true)
    public Optional<Kategori> findById(Long id) {
        return this.getKategoriById(id);
    }

    @Transactional(readOnly=true)
    public long getKategoriCount() {
        return this.kategoriRepository.count();
    }

    @Transactional(readOnly=true)
    public Page<Kategori> findAll(Pageable pageable) {
        return this.kategoriRepository.findAll(pageable);
    }

    @Transactional(readOnly=true)
    public Page<Kategori> findByAdContainingIgnoreCase(String ad, Pageable pageable) {
        return this.kategoriRepository.findByAdContainingIgnoreCase(ad, pageable);
    }
}

