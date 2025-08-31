/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.dto.SiparisDetayViewModel
 */
package com.alperen.kitapsatissistemi.dto;

import java.math.BigDecimal;

public class SiparisDetayViewModel {
    private String kitapAd = "";
    private Integer adet;
    private BigDecimal fiyat;

    public SiparisDetayViewModel() {
    }

    public SiparisDetayViewModel(String kitapAd, Integer adet, BigDecimal fiyat) {
        this.kitapAd = kitapAd;
        this.adet = adet;
        this.fiyat = fiyat;
    }

    public String getKitapAd() {
        return this.kitapAd;
    }

    public void setKitapAd(String kitapAd) {
        this.kitapAd = kitapAd;
    }

    public Integer getAdet() {
        return this.adet;
    }

    public void setAdet(Integer adet) {
        this.adet = adet;
    }

    public BigDecimal getFiyat() {
        return this.fiyat;
    }

    public void setFiyat(BigDecimal fiyat) {
        this.fiyat = fiyat;
    }
}

