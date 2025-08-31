/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Adres
 *  com.alperen.kitapsatissistemi.entity.Kullanici
 *  com.alperen.kitapsatissistemi.exception.BusinessException
 *  com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException
 *  com.alperen.kitapsatissistemi.repository.AdresRepository
 *  com.alperen.kitapsatissistemi.repository.KullaniciRepository
 *  com.alperen.kitapsatissistemi.service.AdresService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 *  org.springframework.transaction.annotation.Transactional
 */
package com.alperen.kitapsatissistemi.service;

import com.alperen.kitapsatissistemi.entity.Adres;
import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.exception.BusinessException;
import com.alperen.kitapsatissistemi.exception.EntityNotFoundBusinessException;
import com.alperen.kitapsatissistemi.repository.AdresRepository;
import com.alperen.kitapsatissistemi.repository.KullaniciRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdresService {
    private final AdresRepository adresRepository;
    private final KullaniciRepository kullaniciRepository;

    @Autowired
    public AdresService(AdresRepository adresRepository, KullaniciRepository kullaniciRepository) {
        this.adresRepository = adresRepository;
        this.kullaniciRepository = kullaniciRepository;
    }

    public Adres createAdres(Kullanici kullanici, Adres adres) {
        adres.setKullanici(kullanici);
        if (this.adresRepository.countByKullanici_Id(kullanici.getId()) == 0L) {
            adres.setVarsayilan(Boolean.valueOf(true));
        }
        if (Boolean.TRUE.equals(adres.getVarsayilan())) {
            this.adresRepository.resetVarsayilanAdres(kullanici.getId());
        }
        return (Adres)this.adresRepository.save((Object)adres);
    }

    public Adres updateAdres(Long adresId, Adres adresDetaylari) {
        Adres mevcutAdres = (Adres)this.adresRepository.findById((Object)adresId).orElseThrow(() -> new EntityNotFoundBusinessException("Adres bulunamad\u0131: " + adresId));
        mevcutAdres.setBaslik(adresDetaylari.getBaslik());
        mevcutAdres.setAdSoyad(adresDetaylari.getAdSoyad());
        mevcutAdres.setTelefon(adresDetaylari.getTelefon());
        mevcutAdres.setSehir(adresDetaylari.getSehir());
        mevcutAdres.setIlce(adresDetaylari.getIlce());
        mevcutAdres.setMahalle(adresDetaylari.getMahalle());
        mevcutAdres.setAdresDetay(adresDetaylari.getAdresDetay());
        mevcutAdres.setPostaKodu(adresDetaylari.getPostaKodu());
        if (Boolean.TRUE.equals(adresDetaylari.getVarsayilan()) && !Boolean.TRUE.equals(mevcutAdres.getVarsayilan())) {
            this.adresRepository.resetVarsayilanAdres(mevcutAdres.getKullanici().getId());
            mevcutAdres.setVarsayilan(Boolean.valueOf(true));
        } else if (Boolean.FALSE.equals(adresDetaylari.getVarsayilan())) {
            mevcutAdres.setVarsayilan(Boolean.valueOf(false));
        }
        return (Adres)this.adresRepository.save((Object)mevcutAdres);
    }

    public void deleteAdres(Long adresId) {
        Adres adres = (Adres)this.adresRepository.findById((Object)adresId).orElseThrow(() -> new EntityNotFoundBusinessException("Adres bulunamad\u0131: " + adresId));
        this.adresRepository.delete((Object)adres);
    }

    @Transactional(readOnly=true)
    public Optional<Adres> getAdresById(Long adresId) {
        return this.adresRepository.findById((Object)adresId);
    }

    @Transactional(readOnly=true)
    public List<Adres> getAdreslerByKullaniciId(Long kullaniciId) {
        return this.adresRepository.findByKullanici_IdOrderByVarsayilanDescBaslikAsc(kullaniciId);
    }

    @Transactional(readOnly=true)
    public Optional<Adres> getVarsayilanAdres(Long kullaniciId) {
        return this.adresRepository.findByKullanici_IdAndVarsayilanTrue(kullaniciId);
    }

    public Adres setVarsayilanAdres(Long kullaniciId, Long adresId) {
        Adres adres = (Adres)this.adresRepository.findById((Object)adresId).orElseThrow(() -> new EntityNotFoundBusinessException("Adres bulunamad\u0131: " + adresId));
        if (!adres.getKullanici().getId().equals(kullaniciId)) {
            throw new BusinessException("Bu adres size ait de\u011fil");
        }
        this.adresRepository.resetVarsayilanAdres(kullaniciId);
        adres.setVarsayilan(Boolean.valueOf(true));
        return (Adres)this.adresRepository.save((Object)adres);
    }

    @Transactional(readOnly=true)
    public List<Adres> getAdreslerBySehir(String sehir) {
        return this.adresRepository.findBySehir(sehir);
    }

    @Transactional(readOnly=true)
    public long getAdresSayisi(Long kullaniciId) {
        return this.adresRepository.countByKullanici_Id(kullaniciId);
    }
}

