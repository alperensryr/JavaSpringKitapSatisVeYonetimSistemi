/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Kullanici
 *  com.alperen.kitapsatissistemi.entity.Siparis
 *  com.alperen.kitapsatissistemi.entity.SiparisDetay
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException
 *  com.alperen.kitapsatissistemi.repository.KullaniciRepository
 *  com.alperen.kitapsatissistemi.repository.SiparisRepository
 *  com.alperen.kitapsatissistemi.service.SiparisService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.util.StringUtils
 */
package com.alperen.kitapsatissistemi.service;

import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.entity.Siparis;
import com.alperen.kitapsatissistemi.entity.SiparisDetay;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import com.alperen.kitapsatissistemi.repository.KullaniciRepository;
import com.alperen.kitapsatissistemi.repository.SiparisRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class SiparisService {
    private final SiparisRepository siparisRepository;
    private final KullaniciRepository kullaniciRepository;

    @Autowired
    public SiparisService(SiparisRepository siparisRepository, KullaniciRepository kullaniciRepository) {
        this.siparisRepository = siparisRepository;
        this.kullaniciRepository = kullaniciRepository;
    }

    @Transactional(readOnly=true)
    public List<Siparis> getAllSiparisler() {
        return this.siparisRepository.findAll();
    }

    @Transactional(readOnly=true)
    public List<Siparis> getAllSiparislerWithDetails() {
        return this.siparisRepository.findAllWithDetails();
    }

    @Transactional(readOnly=true)
    public Optional<Siparis> getSiparisById(Long id) {
        return this.siparisRepository.findById((Object)id);
    }

    @Transactional(readOnly=true)
    public Optional<Siparis> getSiparisByIdWithDetails(Long id) {
        return this.siparisRepository.findByIdWithDetails(id);
    }

    @Transactional(readOnly=true)
    public List<Siparis> getSiparislerByKullaniciId(Long kullaniciId) {
        return this.siparisRepository.findByKullanici_Id(kullaniciId);
    }

    @Transactional(readOnly=true)
    public List<Siparis> getSiparislerByKullaniciIdWithDetails(Long kullaniciId) {
        return this.siparisRepository.findByKullanici_IdWithDetails(kullaniciId);
    }

    @Transactional(readOnly=true)
    public List<Siparis> getSiparislerByDurum(String durum) {
        return this.siparisRepository.findByDurum(durum);
    }

    @Transactional(readOnly=true)
    public List<Siparis> getSiparislerByTarihAraligi(LocalDateTime baslangic, LocalDateTime bitis) {
        return this.siparisRepository.findBySiparisTarihiBetween(baslangic, bitis);
    }

    @Transactional(readOnly=true)
    public List<Siparis> getSiparislerByTutarAraligi(BigDecimal minTutar, BigDecimal maxTutar) {
        return this.siparisRepository.findByToplamTutarBetween(minTutar, maxTutar);
    }

    public Siparis createSiparis(Long kullaniciId, List<SiparisDetay> siparisDetaylari) {
        if (kullaniciId == null) {
            throw new BusinessException("Kullan\u0131c\u0131 ID'si bo\u015f olamaz");
        }
        if (siparisDetaylari == null || siparisDetaylari.isEmpty()) {
            throw new BusinessException("Sipari\u015f detaylar\u0131 bo\u015f olamaz");
        }
        Kullanici kullanici = (Kullanici)this.kullaniciRepository.findById((Object)kullaniciId).orElseThrow(() -> new EntityNotFoundBusinessException("Kullan\u0131c\u0131", (Object)kullaniciId));
        for (SiparisDetay detay2 : siparisDetaylari) {
            if (detay2 == null) {
                throw new BusinessException("Sipari\u015f detay\u0131 null olamaz");
            }
            if (detay2.getAdet() == null || detay2.getAdet() <= 0) {
                throw new BusinessException("Sipari\u015f detay\u0131 adedi pozitif olmal\u0131d\u0131r");
            }
            if (detay2.getFiyat() != null && detay2.getFiyat().compareTo(BigDecimal.ZERO) > 0) continue;
            throw new BusinessException("Sipari\u015f detay\u0131 fiyat\u0131 pozitif olmal\u0131d\u0131r");
        }
        BigDecimal toplamTutar = siparisDetaylari.stream().map(detay -> detay.getFiyat().multiply(BigDecimal.valueOf(detay.getAdet().intValue()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        Siparis siparis = new Siparis(kullanici, toplamTutar);
        for (SiparisDetay detay3 : siparisDetaylari) {
            detay3.setSiparis(siparis);
        }
        siparis.setSiparisDetaylari(siparisDetaylari);
        return (Siparis)this.siparisRepository.save((Object)siparis);
    }

    public Siparis createSiparisWithAddressAndPayment(Long kullaniciId, List<SiparisDetay> siparisDetaylari, String teslimatAdSoyad, String teslimatTelefon, String teslimatSehir, String teslimatIlce, String teslimatMahalle, String teslimatAdresDetay, String teslimatPostaKodu, String odemeYontemi, String kartSahibiAdi, String kartNumarasiSon4) {
        Siparis siparis = this.createSiparis(kullaniciId, siparisDetaylari);
        siparis.setTeslimatAdSoyad(teslimatAdSoyad);
        siparis.setTeslimatTelefon(teslimatTelefon);
        siparis.setTeslimatSehir(teslimatSehir);
        siparis.setTeslimatIlce(teslimatIlce);
        siparis.setTeslimatMahalle(teslimatMahalle);
        siparis.setTeslimatAdresDetay(teslimatAdresDetay);
        siparis.setTeslimatPostaKodu(teslimatPostaKodu);
        siparis.setOdemeYontemi(odemeYontemi);
        siparis.setKartSahibiAdi(kartSahibiAdi);
        siparis.setKartNumarasiSon4(kartNumarasiSon4);
        siparis.setOdemeDurumu("Beklemede");
        if ("Kredi Kart\u0131".equals(odemeYontemi)) {
            siparis.setOdemeDurumu("Onayland\u0131");
        }
        return (Siparis)this.siparisRepository.save((Object)siparis);
    }

    public Siparis updateSiparisDurum(Long id, String yeniDurum) {
        if (id == null) {
            throw new BusinessException("Sipari\u015f ID'si bo\u015f olamaz");
        }
        if (!StringUtils.hasText((String)yeniDurum)) {
            throw new BusinessException("Yeni durum bo\u015f olamaz");
        }
        return this.siparisRepository.findById((Object)id).map(siparis -> {
            siparis.setDurum(yeniDurum.trim());
            return (Siparis)this.siparisRepository.save(siparis);
        }).orElseThrow(() -> new EntityNotFoundBusinessException("Sipari\u015f", (Object)id));
    }

    @Transactional
    public void bulkUpdateSiparisDurum(List<Long> ids, String yeniDurum) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("Sipari\u015f ID listesi bo\u015f olamaz");
        }
        if (!StringUtils.hasText((String)yeniDurum)) {
            throw new BusinessException("Yeni durum bo\u015f olamaz");
        }
        List siparisler = this.siparisRepository.findAllById(ids);
        siparisler.forEach(s -> s.setDurum(yeniDurum.trim()));
        this.siparisRepository.saveAll((Iterable)siparisler);
    }

    public Siparis updateSiparis(Long id, Siparis siparisDetaylari) {
        if (id == null) {
            throw new BusinessException("Sipari\u015f ID'si bo\u015f olamaz");
        }
        if (siparisDetaylari == null) {
            throw new BusinessException("Sipari\u015f detaylar\u0131 bo\u015f olamaz");
        }
        return this.siparisRepository.findById((Object)id).map(siparis2 -> {
            if (siparisDetaylari.getToplamTutar() != null) {
                siparis2.setToplamTutar(siparisDetaylari.getToplamTutar());
            }
            if (StringUtils.hasText((String)siparisDetaylari.getDurum())) {
                siparis2.setDurum(siparisDetaylari.getDurum().trim());
            }
            return (Siparis)this.siparisRepository.save(siparis2);
        }).orElseThrow(() -> new EntityNotFoundBusinessException("Sipari\u015f", (Object)id));
    }

    public void deleteSiparis(Long id) {
        if (id == null) {
            throw new BusinessException("Sipari\u015f ID'si bo\u015f olamaz");
        }
        if (!this.siparisRepository.existsById((Object)id)) {
            throw new EntityNotFoundBusinessException("Sipari\u015f", (Object)id);
        }
        this.siparisRepository.deleteById((Object)id);
    }

    @Transactional(readOnly=true)
    public boolean existsById(Long id) {
        return this.siparisRepository.existsById((Object)id);
    }

    @Transactional(readOnly=true)
    public long getSiparisCountByKullaniciId(Long kullaniciId) {
        return this.siparisRepository.countByKullanici_Id(kullaniciId);
    }

    @Transactional(readOnly=true)
    public long getSiparisCountByDurum(String durum) {
        return this.siparisRepository.countByDurum(durum);
    }

    @Transactional(readOnly=true)
    public BigDecimal getTotalSpentByKullaniciId(Long kullaniciId) {
        BigDecimal total = this.siparisRepository.findToplamHarcamaByKullanici_Id(kullaniciId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Transactional(readOnly=true)
    public List<Object[]> getDailySalesStats() {
        return this.siparisRepository.findGunlukSatisIstatistikleri();
    }

    @Transactional(readOnly=true)
    public List<Object[]> getMonthlySalesStats() {
        return this.siparisRepository.getMonthlySalesStats();
    }

    @Transactional(readOnly=true)
    public List<Object[]> getTopCustomers() {
        return this.siparisRepository.getTopCustomers();
    }

    @Transactional(readOnly=true)
    public List<Siparis> getSiparislerAfterDate(LocalDateTime tarih) {
        return this.siparisRepository.findBySiparisTarihiAfter(tarih);
    }

    @Transactional(readOnly=true)
    public List<Siparis> getSiparislerBeforeDate(LocalDateTime tarih) {
        return this.siparisRepository.findBySiparisTarihiBefore(tarih);
    }

    @Transactional(readOnly=true)
    public long getSiparisCount() {
        return this.siparisRepository.count();
    }

    @Transactional(readOnly=true)
    public List<Siparis> getLatestSiparisler(int limit) {
        return this.siparisRepository.findTopByOrderByIdDesc(limit);
    }

    @Transactional(readOnly=true)
    public Page<Siparis> findAll(Pageable pageable) {
        return this.siparisRepository.findAll(pageable);
    }

    @Transactional(readOnly=true)
    public Page<Siparis> findByDurum(String durum, Pageable pageable) {
        return this.siparisRepository.findByDurum(durum, pageable);
    }

    @Transactional(readOnly=true)
    public Optional<Siparis> findById(Long id) {
        return this.siparisRepository.findById((Object)id);
    }

    public void deleteById(Long id) {
        this.siparisRepository.deleteById((Object)id);
    }

    public Siparis updateDurum(Long id, String durum) {
        Optional siparisOpt = this.siparisRepository.findById((Object)id);
        if (siparisOpt.isPresent()) {
            Siparis siparis = (Siparis)siparisOpt.get();
            siparis.setDurum(durum);
            return (Siparis)this.siparisRepository.save((Object)siparis);
        }
        throw new EntityNotFoundBusinessException("Sipari\u015f", (Object)id);
    }

    public Siparis save(Siparis siparis) {
        return (Siparis)this.siparisRepository.save((Object)siparis);
    }

    @Transactional(readOnly=true)
    public long count() {
        return this.getSiparisCount();
    }
}

