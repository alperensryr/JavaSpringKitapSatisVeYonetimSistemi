/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Kullanici
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.exception.DuplicateEntityException
 *  com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException
 *  com.alperen.kitapsatissistemi.repository.KullaniciRepository
 *  com.alperen.kitapsatissistemi.repository.SiparisRepository
 *  com.alperen.kitapsatissistemi.service.KullaniciService
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.security.crypto.password.PasswordEncoder
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 *  org.springframework.util.StringUtils
 */
package com.alperen.kitapsatissistemi.service;

import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.exception.DuplicateEntityException;
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import com.alperen.kitapsatissistemi.repository.KullaniciRepository;
import com.alperen.kitapsatissistemi.repository.SiparisRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class KullaniciService {
    private static final Logger logger = LoggerFactory.getLogger(KullaniciService.class);
    private final KullaniciRepository kullaniciRepository;
    private final PasswordEncoder passwordEncoder;
    private final SiparisRepository siparisRepository;

    @Autowired
    public KullaniciService(KullaniciRepository kullaniciRepository, PasswordEncoder passwordEncoder, SiparisRepository siparisRepository) {
        this.kullaniciRepository = kullaniciRepository;
        this.passwordEncoder = passwordEncoder;
        this.siparisRepository = siparisRepository;
    }

    @Transactional(readOnly=true)
    public List<Kullanici> getAllKullanicilar() {
        return this.kullaniciRepository.findAll();
    }

    @Transactional(readOnly=true)
    public Optional<Kullanici> getKullaniciById(Long id) {
        return this.kullaniciRepository.findById((Object)id);
    }

    @Transactional(readOnly=true)
    public Optional<Kullanici> getKullaniciByEmail(String email) {
        String normalizedEmail = email != null ? email.trim().toLowerCase() : "";
        return this.kullaniciRepository.findByEmail(normalizedEmail);
    }

    @Transactional(readOnly=true)
    public List<Kullanici> getKullanicilarByRol(String rol) {
        return this.kullaniciRepository.findByRol(rol);
    }

    @Transactional(readOnly=true)
    public List<Kullanici> getAdminKullanicilar() {
        return this.kullaniciRepository.findByRol("Admin");
    }

    @Transactional(readOnly=true)
    public List<Kullanici> getNormalKullanicilar() {
        return this.kullaniciRepository.findByRol("User");
    }

    @Transactional(readOnly=true)
    public List<Kullanici> searchKullanicilarByAdSoyad(String adSoyad) {
        return this.kullaniciRepository.findByAdSoyadContainingIgnoreCase(adSoyad);
    }

    public Kullanici registerKullanici(Kullanici kullanici, String sifre) {
        if (kullanici == null) {
            throw new BusinessException("Kullan\u0131c\u0131 bilgisi bo\u015f olamaz");
        }
        if (!StringUtils.hasText((String)sifre)) {
            throw new BusinessException("\u015eifre bo\u015f olamaz");
        }
        if (!StringUtils.hasText((String)kullanici.getEmail())) {
            throw new BusinessException("Email adresi bo\u015f olamaz");
        }
        if (!StringUtils.hasText((String)kullanici.getAdSoyad())) {
            throw new BusinessException("Ad soyad bo\u015f olamaz");
        }
        kullanici.setEmail(kullanici.getEmail().trim().toLowerCase());
        kullanici.setAdSoyad(kullanici.getAdSoyad().trim());
        if (sifre.length() < 6) {
            throw new BusinessException("\u015eifre en az 6 karakter olmal\u0131d\u0131r");
        }
        if (this.kullaniciRepository.existsByEmail(kullanici.getEmail())) {
            throw new DuplicateEntityException("Kullan\u0131c\u0131", "email", (Object)kullanici.getEmail());
        }
        kullanici.setSifreHash(this.passwordEncoder.encode((CharSequence)sifre));
        kullanici.setKayitTarihi(LocalDateTime.now());
        if (!StringUtils.hasText((String)kullanici.getRol())) {
            kullanici.setRol("User");
        }
        return (Kullanici)this.kullaniciRepository.save((Object)kullanici);
    }

    @Transactional(readOnly=true)
    public Optional<Kullanici> authenticateKullanici(String email, String sifre) {
        logger.info("Kullan\u0131c\u0131 giri\u015fi denemesi: {}", (Object)email);
        String normalizedEmail = email != null ? email.trim().toLowerCase() : "";
        Optional kullaniciOpt = this.kullaniciRepository.findByEmail(normalizedEmail);
        if (kullaniciOpt.isPresent()) {
            logger.info("Kullan\u0131c\u0131 bulundu: {}", (Object)normalizedEmail);
            Kullanici kullanici = (Kullanici)kullaniciOpt.get();
            if (this.passwordEncoder.matches((CharSequence)sifre, kullanici.getSifreHash())) {
                logger.info("\u015eifre do\u011fruland\u0131: {}", (Object)normalizedEmail);
                return Optional.of(kullanici);
            }
            logger.warn("\u015eifre yanl\u0131\u015f: {}", (Object)normalizedEmail);
        } else {
            logger.warn("Kullan\u0131c\u0131 bulunamad\u0131: {}", (Object)normalizedEmail);
        }
        return Optional.empty();
    }

    public Kullanici updateKullanici(Long id, Kullanici kullaniciDetaylari) {
        if (id == null) {
            throw new BusinessException("Kullan\u0131c\u0131 ID'si bo\u015f olamaz");
        }
        if (kullaniciDetaylari == null) {
            throw new BusinessException("Kullan\u0131c\u0131 bilgisi bo\u015f olamaz");
        }
        if (!StringUtils.hasText((String)kullaniciDetaylari.getEmail())) {
            throw new BusinessException("Email adresi bo\u015f olamaz");
        }
        if (!StringUtils.hasText((String)kullaniciDetaylari.getAdSoyad())) {
            throw new BusinessException("Ad soyad bo\u015f olamaz");
        }
        kullaniciDetaylari.setEmail(kullaniciDetaylari.getEmail().trim().toLowerCase());
        kullaniciDetaylari.setAdSoyad(kullaniciDetaylari.getAdSoyad().trim());
        return this.kullaniciRepository.findById((Object)id).map(kullanici2 -> {
            if (!kullanici2.getEmail().equals(kullaniciDetaylari.getEmail()) && this.kullaniciRepository.existsByEmail(kullaniciDetaylari.getEmail())) {
                throw new DuplicateEntityException("Kullan\u0131c\u0131", "email", (Object)kullaniciDetaylari.getEmail());
            }
            kullanici2.setAdSoyad(kullaniciDetaylari.getAdSoyad());
            kullanici2.setEmail(kullaniciDetaylari.getEmail());
            if (StringUtils.hasText((String)kullaniciDetaylari.getRol())) {
                kullanici2.setRol(kullaniciDetaylari.getRol());
            }
            return (Kullanici)this.kullaniciRepository.save(kullanici2);
        }).orElseThrow(() -> new EntityNotFoundBusinessException("Kullan\u0131c\u0131", (Object)id));
    }

    public void changePassword(Long id, String eskiSifre, String yeniSifre) {
        if (id == null) {
            throw new BusinessException("Kullan\u0131c\u0131 ID'si bo\u015f olamaz");
        }
        if (!StringUtils.hasText((String)eskiSifre)) {
            throw new BusinessException("Eski \u015fifre bo\u015f olamaz");
        }
        if (!StringUtils.hasText((String)yeniSifre)) {
            throw new BusinessException("Yeni \u015fifre bo\u015f olamaz");
        }
        if (yeniSifre.length() < 6) {
            throw new BusinessException("Yeni \u015fifre en az 6 karakter olmal\u0131d\u0131r");
        }
        Kullanici kullanici = (Kullanici)this.kullaniciRepository.findById((Object)id).orElseThrow(() -> new EntityNotFoundBusinessException("Kullan\u0131c\u0131", (Object)id));
        if (!this.passwordEncoder.matches((CharSequence)eskiSifre, kullanici.getSifreHash())) {
            throw new BusinessException("Eski \u015fifre yanl\u0131\u015f");
        }
        kullanici.setSifreHash(this.passwordEncoder.encode((CharSequence)yeniSifre));
        this.kullaniciRepository.save((Object)kullanici);
    }

    public void resetPassword(Long id, String yeniSifre) {
        if (id == null) {
            throw new BusinessException("Kullan\u0131c\u0131 ID'si bo\u015f olamaz");
        }
        if (!StringUtils.hasText((String)yeniSifre)) {
            throw new BusinessException("Yeni \u015fifre bo\u015f olamaz");
        }
        if (yeniSifre.length() < 6) {
            throw new BusinessException("Yeni \u015fifre en az 6 karakter olmal\u0131d\u0131r");
        }
        Kullanici kullanici = (Kullanici)this.kullaniciRepository.findById((Object)id).orElseThrow(() -> new EntityNotFoundBusinessException("Kullan\u0131c\u0131", (Object)id));
        kullanici.setSifreHash(this.passwordEncoder.encode((CharSequence)yeniSifre));
        this.kullaniciRepository.save((Object)kullanici);
    }

    @Transactional(readOnly=true)
    public long getKullaniciCount() {
        return this.kullaniciRepository.count();
    }

    @Transactional(readOnly=true)
    public List<Kullanici> getLatestKullanicilar(int limit) {
        return this.kullaniciRepository.findTopByOrderByKayitTarihiDesc(limit);
    }

    public void deleteKullanici(Long id) {
        if (id == null) {
            throw new BusinessException("Kullan\u0131c\u0131 ID'si bo\u015f olamaz");
        }
        Kullanici kullanici = (Kullanici)this.kullaniciRepository.findById((Object)id).orElseThrow(() -> new EntityNotFoundBusinessException("Kullan\u0131c\u0131", (Object)id));
        long siparisSayisi = this.siparisRepository.countByKullanici_Id(kullanici.getId());
        if (siparisSayisi > 0L) {
            throw new BusinessException("Bu kullan\u0131c\u0131ya ait sipari\u015fler bulundu\u011fu i\u00e7in silinemez.");
        }
        this.kullaniciRepository.deleteById((Object)id);
    }

    @Transactional(readOnly=true)
    public boolean existsByEmail(String email) {
        String normalizedEmail = email != null ? email.trim().toLowerCase() : "";
        return this.kullaniciRepository.existsByEmail(normalizedEmail);
    }

    @Transactional(readOnly=true)
    public boolean existsById(Long id) {
        return this.kullaniciRepository.existsById((Object)id);
    }

    @Transactional(readOnly=true)
    public List<Kullanici> getKullanicilarByKayitTarihiAfter(LocalDateTime tarih) {
        return this.kullaniciRepository.findByKayitTarihiAfter(tarih);
    }

    @Transactional(readOnly=true)
    public List<Kullanici> getKullanicilarByKayitTarihiBefore(LocalDateTime tarih) {
        return this.kullaniciRepository.findByKayitTarihiBefore(tarih);
    }

    @Transactional(readOnly=true)
    public long count() {
        return this.kullaniciRepository.count();
    }

    @Transactional(readOnly=true)
    public Page<Kullanici> findByAdSoyadContainingIgnoreCaseOrEmailContainingIgnoreCase(String adSoyad, String email, Pageable pageable) {
        return this.kullaniciRepository.findByAdSoyadContainingIgnoreCaseOrEmailContainingIgnoreCase(adSoyad, email, pageable);
    }

    @Transactional(readOnly=true)
    public long getKullaniciCountByRol(String rol) {
        return this.kullaniciRepository.countByRol(rol);
    }

    @Transactional(readOnly=true)
    public Page<Kullanici> findAll(Pageable pageable) {
        return this.kullaniciRepository.findAll(pageable);
    }

    @Transactional(readOnly=true)
    public Page<Kullanici> findByAdSoyadContainingIgnoreCase(String adSoyad, Pageable pageable) {
        return this.kullaniciRepository.findByAdSoyadContainingIgnoreCase(adSoyad, pageable);
    }

    @Transactional(readOnly=true)
    public Optional<Kullanici> findById(Long id) {
        return this.kullaniciRepository.findById((Object)id);
    }

    public Kullanici save(Kullanici kullanici) {
        return (Kullanici)this.kullaniciRepository.save((Object)kullanici);
    }

    public void deleteById(Long id) {
        this.kullaniciRepository.deleteById((Object)id);
    }
}

