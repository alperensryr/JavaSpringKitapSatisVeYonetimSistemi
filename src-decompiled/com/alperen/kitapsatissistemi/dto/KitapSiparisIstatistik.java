/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.dto.KitapSiparisIstatistik
 */
package com.alperen.kitapsatissistemi.dto;

import java.math.BigDecimal;

public class KitapSiparisIstatistik {
    private Long kitapId;
    private String kitapAd;
    private String yazar;
    private int toplamSatilanAdet;
    private BigDecimal toplamSatisTutari;
    private int siparisAdedi;

    public KitapSiparisIstatistik() {
    }

    public KitapSiparisIstatistik(Long kitapId, String kitapAd, String yazar, int toplamSatilanAdet, BigDecimal toplamSatisTutari, int siparisAdedi) {
        this.kitapId = kitapId;
        this.kitapAd = kitapAd;
        this.yazar = yazar;
        this.toplamSatilanAdet = toplamSatilanAdet;
        this.toplamSatisTutari = toplamSatisTutari;
        this.siparisAdedi = siparisAdedi;
    }

    public Long getKitapId() {
        return this.kitapId;
    }

    public void setKitapId(Long kitapId) {
        this.kitapId = kitapId;
    }

    public String getKitapAd() {
        return this.kitapAd;
    }

    public void setKitapAd(String kitapAd) {
        this.kitapAd = kitapAd;
    }

    public String getYazar() {
        return this.yazar;
    }

    public void setYazar(String yazar) {
        this.yazar = yazar;
    }

    public int getToplamSatilanAdet() {
        return this.toplamSatilanAdet;
    }

    public void setToplamSatilanAdet(int toplamSatilanAdet) {
        this.toplamSatilanAdet = toplamSatilanAdet;
    }

    public BigDecimal getToplamSatisTutari() {
        return this.toplamSatisTutari;
    }

    public void setToplamSatisTutari(BigDecimal toplamSatisTutari) {
        this.toplamSatisTutari = toplamSatisTutari;
    }

    public int getSiparisAdedi() {
        return this.siparisAdedi;
    }

    public void setSiparisAdedi(int siparisAdedi) {
        this.siparisAdedi = siparisAdedi;
    }
}

