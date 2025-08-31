/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Kitap
 *  com.alperen.kitapsatissistemi.entity.Siparis
 *  com.alperen.kitapsatissistemi.entity.SiparisDetay
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException
 *  com.alperen.kitapsatissistemi.repository.KitapRepository
 *  com.alperen.kitapsatissistemi.repository.SiparisDetayRepository
 *  com.alperen.kitapsatissistemi.repository.SiparisRepository
 *  com.alperen.kitapsatissistemi.service.SiparisDetayService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.alperen.kitapsatissistemi.service;

import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.entity.Siparis;
import com.alperen.kitapsatissistemi.entity.SiparisDetay;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import com.alperen.kitapsatissistemi.repository.KitapRepository;
import com.alperen.kitapsatissistemi.repository.SiparisDetayRepository;
import com.alperen.kitapsatissistemi.repository.SiparisRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SiparisDetayService {
    private final SiparisDetayRepository siparisDetayRepository;
    private final SiparisRepository siparisRepository;
    private final KitapRepository kitapRepository;

    @Autowired
    public SiparisDetayService(SiparisDetayRepository siparisDetayRepository, SiparisRepository siparisRepository, KitapRepository kitapRepository) {
        this.siparisDetayRepository = siparisDetayRepository;
        this.siparisRepository = siparisRepository;
        this.kitapRepository = kitapRepository;
    }

    @Transactional(readOnly=true)
    public List<SiparisDetay> getAllSiparisDetaylar() {
        return this.siparisDetayRepository.findAll();
    }

    @Transactional(readOnly=true)
    public List<SiparisDetay> getAllSiparisDetaylarWithDetails() {
        return this.siparisDetayRepository.findAllWithKitapAndSiparis();
    }

    @Transactional(readOnly=true)
    public Optional<SiparisDetay> getSiparisDetayById(Long id) {
        return this.siparisDetayRepository.findById((Object)id);
    }

    @Transactional(readOnly=true)
    public List<SiparisDetay> getSiparisDetaylarBySiparisId(Long siparisId) {
        return this.siparisDetayRepository.findBySiparis_Id(siparisId);
    }

    @Transactional(readOnly=true)
    public List<SiparisDetay> getSiparisDetaylarBySiparisIdWithKitap(Long siparisId) {
        return this.siparisDetayRepository.findBySiparis_IdWithKitap(siparisId);
    }

    @Transactional(readOnly=true)
    public List<SiparisDetay> getSiparisDetaylarByKitapId(Long kitapId) {
        return this.siparisDetayRepository.findByKitap_Id(kitapId);
    }

    @Transactional(readOnly=true)
    public List<SiparisDetay> getSiparisDetaylarByKitapIdWithSiparis(Long kitapId) {
        return this.siparisDetayRepository.findByKitap_IdWithSiparis(kitapId);
    }

    @Transactional(readOnly=true)
    public Optional<SiparisDetay> getSiparisDetayBySiparisIdAndKitapId(Long siparisId, Long kitapId) {
        return this.siparisDetayRepository.findBySiparis_IdAndKitap_Id(siparisId, kitapId);
    }

    public SiparisDetay createSiparisDetay(SiparisDetay siparisDetay) {
        Siparis siparis = (Siparis)this.siparisRepository.findById((Object)siparisDetay.getSiparisId()).orElseThrow(() -> new EntityNotFoundBusinessException("Sipari\u015f", (Object)siparisDetay.getSiparisId()));
        Kitap kitap = (Kitap)this.kitapRepository.findById((Object)siparisDetay.getKitapId()).orElseThrow(() -> new EntityNotFoundBusinessException("Kitap", (Object)siparisDetay.getKitapId()));
        if (siparisDetay.getAdet() <= 0) {
            throw new BusinessException("Adet 0'dan b\u00fcy\u00fck olmal\u0131d\u0131r");
        }
        if (siparisDetay.getFiyat().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Fiyat 0'dan b\u00fcy\u00fck olmal\u0131d\u0131r");
        }
        siparisDetay.setSiparis(siparis);
        siparisDetay.setKitap(kitap);
        return (SiparisDetay)this.siparisDetayRepository.save((Object)siparisDetay);
    }

    public SiparisDetay updateSiparisDetay(Long id, SiparisDetay siparisDetayDetaylari) {
        return this.siparisDetayRepository.findById((Object)id).map(siparisDetay2 -> {
            Kitap kitap = (Kitap)this.kitapRepository.findById((Object)siparisDetayDetaylari.getKitapId()).orElseThrow(() -> new EntityNotFoundBusinessException("Kitap", (Object)siparisDetayDetaylari.getKitapId()));
            if (siparisDetayDetaylari.getAdet() <= 0) {
                throw new BusinessException("Adet 0'dan b\u00fcy\u00fck olmal\u0131d\u0131r");
            }
            if (siparisDetayDetaylari.getFiyat().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("Fiyat 0'dan b\u00fcy\u00fck olmal\u0131d\u0131r");
            }
            siparisDetay2.setKitap(kitap);
            siparisDetay2.setAdet(siparisDetayDetaylari.getAdet());
            siparisDetay2.setFiyat(siparisDetayDetaylari.getFiyat());
            return (SiparisDetay)this.siparisDetayRepository.save(siparisDetay2);
        }).orElseThrow(() -> new EntityNotFoundBusinessException("Sipari\u015f detay\u0131", (Object)id));
    }

    public void deleteSiparisDetay(Long id) {
        if (!this.siparisDetayRepository.existsById((Object)id)) {
            throw new EntityNotFoundBusinessException("Sipari\u015f detay\u0131", (Object)id);
        }
        this.siparisDetayRepository.deleteById((Object)id);
    }

    public void deleteAllSiparisDetaylarBySiparisId(Long siparisId) {
        List detaylar = this.siparisDetayRepository.findBySiparis_Id(siparisId);
        this.siparisDetayRepository.deleteAll((Iterable)detaylar);
    }

    @Transactional(readOnly=true)
    public boolean existsById(Long id) {
        return this.siparisDetayRepository.existsById((Object)id);
    }

    @Transactional(readOnly=true)
    public Integer getTotalSalesQuantityByKitapId(Long kitapId) {
        Integer total = this.siparisDetayRepository.getTotalSalesQuantityByKitap_Id(kitapId);
        return total != null ? total : 0;
    }

    @Transactional(readOnly=true)
    public BigDecimal getTotalSalesAmountByKitapId(Long kitapId) {
        return this.siparisDetayRepository.findToplamSatisTutariByKitap_Id(kitapId);
    }

    @Transactional(readOnly=true)
    public List<Object[]> getBestSellingBooks() {
        return this.siparisDetayRepository.findEnCokSatilanKitaplar();
    }

    @Transactional(readOnly=true)
    public List<Object[]> getHighestEarningBooks() {
        return this.siparisDetayRepository.findEnCokGelirGetirenKitaplar();
    }

    @Transactional(readOnly=true)
    public BigDecimal calculateTotalOrderAmount(Long siparisId) {
        return this.siparisDetayRepository.calculateToplamTutarBySiparis_Id(siparisId);
    }

    @Transactional(readOnly=true)
    public List<Object[]> getSalesStatsByCategory() {
        return this.siparisDetayRepository.getSalesStatsByCategory();
    }

    @Transactional(readOnly=true)
    public List<Object[]> getSalesStatsByAuthor() {
        return this.siparisDetayRepository.getSalesStatsByAuthor();
    }

    @Transactional(readOnly=true)
    public long getSiparisDetayCountBySiparisId(Long siparisId) {
        return this.siparisDetayRepository.countBySiparis_Id(siparisId);
    }

    @Transactional(readOnly=true)
    public long getSiparisDetayCountByKitapId(Long kitapId) {
        return this.siparisDetayRepository.countByKitap_Id(kitapId);
    }

    @Transactional(readOnly=true)
    public long getSiparisDetayCount() {
        return this.siparisDetayRepository.count();
    }
}

