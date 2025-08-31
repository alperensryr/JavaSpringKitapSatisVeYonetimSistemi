/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.dto.SiparisDetayViewModel
 *  com.alperen.kitapsatissistemi.dto.SiparisViewModel
 */
package com.alperen.kitapsatissistemi.dto;

import com.alperen.kitapsatissistemi.dto.SiparisDetayViewModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SiparisViewModel {
    private Long id;
    private String kullaniciAd = "";
    private String kullaniciEmail = "";
    private String siparisTarihi = "";
    private BigDecimal toplamTutar;
    private String durum = "Beklemede";
    private List<SiparisDetayViewModel> siparisDetaylari = new ArrayList();

    public SiparisViewModel() {
    }

    public SiparisViewModel(Long id, String kullaniciAd, String kullaniciEmail, String siparisTarihi, BigDecimal toplamTutar, String durum) {
        this.id = id;
        this.kullaniciAd = kullaniciAd;
        this.kullaniciEmail = kullaniciEmail;
        this.siparisTarihi = siparisTarihi;
        this.toplamTutar = toplamTutar;
        this.durum = durum;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKullaniciAd() {
        return this.kullaniciAd;
    }

    public void setKullaniciAd(String kullaniciAd) {
        this.kullaniciAd = kullaniciAd;
    }

    public String getKullaniciEmail() {
        return this.kullaniciEmail;
    }

    public void setKullaniciEmail(String kullaniciEmail) {
        this.kullaniciEmail = kullaniciEmail;
    }

    public String getSiparisTarihi() {
        return this.siparisTarihi;
    }

    public void setSiparisTarihi(String siparisTarihi) {
        this.siparisTarihi = siparisTarihi;
    }

    public BigDecimal getToplamTutar() {
        return this.toplamTutar;
    }

    public void setToplamTutar(BigDecimal toplamTutar) {
        this.toplamTutar = toplamTutar;
    }

    public String getDurum() {
        return this.durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }

    public List<SiparisDetayViewModel> getSiparisDetaylari() {
        return this.siparisDetaylari;
    }

    public void setSiparisDetaylari(List<SiparisDetayViewModel> siparisDetaylari) {
        this.siparisDetaylari = siparisDetaylari;
    }
}

