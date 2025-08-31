/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Favori
 *  com.alperen.kitapsatissistemi.entity.Kitap
 *  com.alperen.kitapsatissistemi.entity.Kullanici
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException
 *  com.alperen.kitapsatissistemi.repository.FavoriRepository
 *  com.alperen.kitapsatissistemi.repository.KitapRepository
 *  com.alperen.kitapsatissistemi.repository.KullaniciRepository
 *  com.alperen.kitapsatissistemi.service.FavoriService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.alperen.kitapsatissistemi.service;

import com.alperen.kitapsatissistemi.entity.Favori;
import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import com.alperen.kitapsatissistemi.repository.FavoriRepository;
import com.alperen.kitapsatissistemi.repository.KitapRepository;
import com.alperen.kitapsatissistemi.repository.KullaniciRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriService {
    private final FavoriRepository favoriRepository;
    private final KullaniciRepository kullaniciRepository;
    private final KitapRepository kitapRepository;

    @Autowired
    public FavoriService(FavoriRepository favoriRepository, KullaniciRepository kullaniciRepository, KitapRepository kitapRepository) {
        this.favoriRepository = favoriRepository;
        this.kullaniciRepository = kullaniciRepository;
        this.kitapRepository = kitapRepository;
    }

    @Transactional(readOnly=true)
    public List<Favori> getAllFavoriler() {
        return this.favoriRepository.findAll();
    }

    @Transactional(readOnly=true)
    public List<Favori> getAllFavorilerWithDetails() {
        return this.favoriRepository.findAllWithKullaniciAndKitap();
    }

    @Transactional(readOnly=true)
    public Optional<Favori> getFavoriById(Long id) {
        return this.favoriRepository.findById((Object)id);
    }

    @Transactional(readOnly=true)
    public List<Favori> getFavorilerByKullaniciId(Long kullaniciId) {
        return this.favoriRepository.findByKullanici_Id(kullaniciId);
    }

    @Transactional(readOnly=true)
    public List<Favori> getFavorilerByKullaniciIdWithDetails(Long kullaniciId) {
        return this.favoriRepository.findByKullanici_IdWithKitap(kullaniciId);
    }

    @Transactional(readOnly=true)
    public List<Favori> getFavorilerByKitapId(Long kitapId) {
        return this.favoriRepository.findByKitap_Id(kitapId);
    }

    @Transactional(readOnly=true)
    public List<Favori> getFavorilerByKitapIdWithDetails(Long kitapId) {
        return this.favoriRepository.findByKitap_IdWithKullanici(kitapId);
    }

    public Favori addFavori(Long kullaniciId, Long kitapId) {
        Kullanici kullanici = (Kullanici)this.kullaniciRepository.findById((Object)kullaniciId).orElseThrow(() -> new EntityNotFoundBusinessException("Kullan\u0131c\u0131", (Object)kullaniciId));
        Kitap kitap = (Kitap)this.kitapRepository.findById((Object)kitapId).orElseThrow(() -> new EntityNotFoundBusinessException("Kitap", (Object)kitapId));
        if (this.favoriRepository.existsByKullanici_IdAndKitap_Id(kullaniciId, kitapId)) {
            throw new BusinessException("Bu kitap zaten favorilerinizde mevcut");
        }
        Favori favori = new Favori(kullanici, kitap);
        return (Favori)this.favoriRepository.save((Object)favori);
    }

    public void deleteFavori(Long id) {
        if (!this.favoriRepository.existsById((Object)id)) {
            throw new EntityNotFoundBusinessException("Favori", (Object)id);
        }
        this.favoriRepository.deleteById((Object)id);
    }

    public void deleteFavoriByKullaniciAndKitap(Long kullaniciId, Long kitapId) {
        Optional favoriOpt = this.favoriRepository.findByKullanici_IdAndKitap_Id(kullaniciId, kitapId);
        if (!favoriOpt.isPresent()) {
            throw new EntityNotFoundBusinessException("Favori bulunamad\u0131");
        }
        this.favoriRepository.delete((Object)((Favori)favoriOpt.get()));
    }

    public void deleteAllFavorilerByKullaniciId(Long kullaniciId) {
        List favoriler = this.favoriRepository.findByKullanici_Id(kullaniciId);
        this.favoriRepository.deleteAll((Iterable)favoriler);
    }

    public void deleteAllFavorilerByKitapId(Long kitapId) {
        List favoriler = this.favoriRepository.findByKitap_Id(kitapId);
        this.favoriRepository.deleteAll((Iterable)favoriler);
    }

    @Transactional(readOnly=true)
    public boolean existsById(Long id) {
        return this.favoriRepository.existsById((Object)id);
    }

    @Transactional(readOnly=true)
    public boolean existsByKullaniciIdAndKitapId(Long kullaniciId, Long kitapId) {
        return this.favoriRepository.existsByKullanici_IdAndKitap_Id(kullaniciId, kitapId);
    }

    @Transactional(readOnly=true)
    public long getFavoriCountByKullaniciId(Long kullaniciId) {
        return this.favoriRepository.countByKullanici_Id(kullaniciId);
    }

    @Transactional(readOnly=true)
    public long getFavoriCountByKitapId(Long kitapId) {
        return this.favoriRepository.countByKitap_Id(kitapId);
    }

    @Transactional(readOnly=true)
    public List<Object[]> getEnCokFavorilenenKitaplar() {
        return this.favoriRepository.findMostFavoritedBooks();
    }

    @Transactional(readOnly=true)
    public List<Object[]> getEnAktifKullanicilar() {
        return this.favoriRepository.findMostActiveUsers();
    }

    @Transactional(readOnly=true)
    public long getFavoriCount() {
        return this.favoriRepository.count();
    }

    @Transactional(readOnly=true)
    public Page<Favori> findAll(Pageable pageable) {
        return this.favoriRepository.findAll(pageable);
    }

    public void deleteById(Long id) {
        this.favoriRepository.deleteById((Object)id);
    }

    public void deleteByKullaniciId(Long kullaniciId) {
        this.favoriRepository.deleteByKullanici_Id(kullaniciId);
    }

    public void deleteByKitapId(Long kitapId) {
        this.favoriRepository.deleteByKitap_Id(kitapId);
    }

    @Transactional(readOnly=true)
    public Optional<Favori> findById(Long id) {
        return this.favoriRepository.findById((Object)id);
    }
}

