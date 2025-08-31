/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.dto.KitapViewModel
 *  javax.validation.constraints.DecimalMin
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.NotNull
 */
package com.alperen.kitapsatissistemi.dto;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class KitapViewModel {
    private Long id;
    @NotBlank(message="Kitap ad\u0131 zorunludur.")
    private @NotBlank(message="Kitap ad\u0131 zorunludur.") String ad = "";
    @NotBlank(message="Yazar ad\u0131 zorunludur.")
    private @NotBlank(message="Yazar ad\u0131 zorunludur.") String yazar = "";
    @NotNull(message="Fiyat zorunludur.")
    @DecimalMin(value="0.01", message="Fiyat 0'dan b\u00fcy\u00fck olmal\u0131d\u0131r.")
    private @NotNull(message="Fiyat zorunludur.") @DecimalMin(value="0.01", message="Fiyat 0'dan b\u00fcy\u00fck olmal\u0131d\u0131r.") BigDecimal fiyat;
    @NotBlank(message="A\u00e7\u0131klama zorunludur.")
    private @NotBlank(message="A\u00e7\u0131klama zorunludur.") String aciklama = "";
    @NotNull(message="Kategori se\u00e7imi zorunludur.")
    private @NotNull(message="Kategori se\u00e7imi zorunludur.") Long kategoriId;
    private String kategoriAd = "";
    private String resimUrl = "";

    public KitapViewModel() {
    }

    public KitapViewModel(Long id, String ad, String yazar, BigDecimal fiyat, String aciklama, Long kategoriId) {
        this.id = id;
        this.ad = ad;
        this.yazar = yazar;
        this.fiyat = fiyat;
        this.aciklama = aciklama;
        this.kategoriId = kategoriId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAd() {
        return this.ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getYazar() {
        return this.yazar;
    }

    public void setYazar(String yazar) {
        this.yazar = yazar;
    }

    public BigDecimal getFiyat() {
        return this.fiyat;
    }

    public void setFiyat(BigDecimal fiyat) {
        this.fiyat = fiyat;
    }

    public String getAciklama() {
        return this.aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Long getKategoriId() {
        return this.kategoriId;
    }

    public void setKategoriId(Long kategoriId) {
        this.kategoriId = kategoriId;
    }

    public String getKategoriAd() {
        return this.kategoriAd;
    }

    public void setKategoriAd(String kategoriAd) {
        this.kategoriAd = kategoriAd;
    }

    public String getResimUrl() {
        return this.resimUrl;
    }

    public void setResimUrl(String resimUrl) {
        this.resimUrl = resimUrl;
    }
}

