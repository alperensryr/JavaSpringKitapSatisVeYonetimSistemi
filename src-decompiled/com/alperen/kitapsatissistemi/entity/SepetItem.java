/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.SepetItem
 */
package com.alperen.kitapsatissistemi.entity;

import java.math.BigDecimal;

public class SepetItem {
    private int kitapId;
    private String kitapAd;
    private BigDecimal fiyat;
    private int adet;
    private String resimUrl;
    private BigDecimal toplamFiyat;

    public SepetItem() {
    }

    public SepetItem(int kitapId, String kitapAd, BigDecimal fiyat, int adet, String resimUrl) {
        this.kitapId = kitapId;
        this.kitapAd = kitapAd;
        this.fiyat = fiyat;
        this.adet = adet;
        this.resimUrl = resimUrl;
    }

    public int getKitapId() {
        return this.kitapId;
    }

    public void setKitapId(int kitapId) {
        this.kitapId = kitapId;
    }

    public String getKitapAd() {
        return this.kitapAd;
    }

    public void setKitapAd(String kitapAd) {
        this.kitapAd = kitapAd;
    }

    public BigDecimal getFiyat() {
        return this.fiyat;
    }

    public void setFiyat(BigDecimal fiyat) {
        this.fiyat = fiyat;
    }

    public int getAdet() {
        return this.adet;
    }

    public void setAdet(int adet) {
        this.adet = adet;
    }

    public String getResimUrl() {
        return this.resimUrl;
    }

    public void setResimUrl(String resimUrl) {
        this.resimUrl = resimUrl;
    }

    public BigDecimal getToplamFiyat() {
        if (this.fiyat == null) {
            return BigDecimal.ZERO;
        }
        return this.fiyat.multiply(BigDecimal.valueOf(this.adet));
    }

    public void setToplamFiyat(BigDecimal toplamFiyat) {
        this.toplamFiyat = toplamFiyat;
    }
}

