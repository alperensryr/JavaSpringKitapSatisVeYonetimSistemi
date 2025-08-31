/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Kategori
 *  com.alperen.kitapsatissistemi.entity.Kitap
 *  javax.persistence.Column
 *  javax.persistence.Entity
 *  javax.persistence.FetchType
 *  javax.persistence.GeneratedValue
 *  javax.persistence.GenerationType
 *  javax.persistence.Id
 *  javax.persistence.JoinColumn
 *  javax.persistence.ManyToOne
 *  javax.persistence.Table
 *  javax.validation.constraints.DecimalMax
 *  javax.validation.constraints.DecimalMin
 *  javax.validation.constraints.Min
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.NotNull
 *  javax.validation.constraints.Size
 */
package com.alperen.kitapsatissistemi.entity;

import com.alperen.kitapsatissistemi.entity.Kategori;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="kitaplar")
public class Kitap {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message="Kitap ad\u0131 zorunludur.")
    @Size(max=200, message="Kitap ad\u0131 en fazla 200 karakter olabilir.")
    @Column(name="ad", nullable=false, length=200)
    private @NotBlank(message="Kitap ad\u0131 zorunludur.") @Size(max=200, message="Kitap ad\u0131 en fazla 200 karakter olabilir.") String ad;
    @NotBlank(message="Yazar ad\u0131 zorunludur.")
    @Size(max=100, message="Yazar ad\u0131 en fazla 100 karakter olabilir.")
    @Column(name="yazar", nullable=false, length=100)
    private @NotBlank(message="Yazar ad\u0131 zorunludur.") @Size(max=100, message="Yazar ad\u0131 en fazla 100 karakter olabilir.") String yazar;
    @NotNull(message="Fiyat zorunludur.")
    @DecimalMin(value="0.01", message="Fiyat 0.01'den b\u00fcy\u00fck olmal\u0131d\u0131r.")
    @DecimalMax(value="999999.99", message="Fiyat 999999.99'dan k\u00fc\u00e7\u00fck olmal\u0131d\u0131r.")
    @Column(name="fiyat", nullable=false, precision=18, scale=2)
    private @NotNull(message="Fiyat zorunludur.") @DecimalMin(value="0.01", message="Fiyat 0.01'den b\u00fcy\u00fck olmal\u0131d\u0131r.") @DecimalMax(value="999999.99", message="Fiyat 999999.99'dan k\u00fc\u00e7\u00fck olmal\u0131d\u0131r.") BigDecimal fiyat;
    @Size(max=1000, message="A\u00e7\u0131klama en fazla 1000 karakter olabilir.")
    @Column(name="aciklama", length=1000)
    private @Size(max=1000, message="A\u00e7\u0131klama en fazla 1000 karakter olabilir.") String aciklama;
    @Size(max=500, message="Resim URL'si en fazla 500 karakter olabilir.")
    @Column(name="resim_url", length=500)
    private @Size(max=500, message="Resim URL'si en fazla 500 karakter olabilir.") String resimUrl;
    @Min(value=0L, message="Stok miktar\u0131 0'dan k\u00fc\u00e7\u00fck olamaz.")
    @Column(name="stok_miktari", nullable=false)
    private @Min(value=0L, message="Stok miktar\u0131 0'dan k\u00fc\u00e7\u00fck olamaz.") Integer stokMiktari = 0;
    @NotNull(message="Kategori zorunludur.")
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="kategori_id", nullable=false)
    private @NotNull(message="Kategori zorunludur.") Kategori kategori;

    public Kitap() {
    }

    public Kitap(String ad, String yazar, BigDecimal fiyat, Kategori kategori) {
        this.ad = ad;
        this.yazar = yazar;
        this.fiyat = fiyat;
        this.kategori = kategori;
    }

    public Kitap(String ad, String yazar, BigDecimal fiyat, String aciklama, Kategori kategori, String resimUrl) {
        this.ad = ad;
        this.yazar = yazar;
        this.fiyat = fiyat;
        this.aciklama = aciklama;
        this.kategori = kategori;
        this.resimUrl = resimUrl;
        this.stokMiktari = 10;
    }

    public Kitap(String ad, String yazar, BigDecimal fiyat, String aciklama, Kategori kategori, String resimUrl, Integer stokMiktari) {
        this.ad = ad;
        this.yazar = yazar;
        this.fiyat = fiyat;
        this.aciklama = aciklama;
        this.kategori = kategori;
        this.resimUrl = resimUrl;
        this.stokMiktari = stokMiktari;
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

    public String getResimUrl() {
        return this.resimUrl;
    }

    public void setResimUrl(String resimUrl) {
        this.resimUrl = resimUrl;
    }

    public Kategori getKategori() {
        return this.kategori;
    }

    public void setKategori(Kategori kategori) {
        this.kategori = kategori;
    }

    public Long getKategoriId() {
        return this.kategori != null ? this.kategori.getId() : null;
    }

    public Integer getStokMiktari() {
        return this.stokMiktari;
    }

    public void setStokMiktari(Integer stokMiktari) {
        this.stokMiktari = stokMiktari;
    }

    public String toString() {
        return "Kitap{id=" + this.id + ", ad='" + this.ad + '\'' + ", yazar='" + this.yazar + '\'' + ", fiyat=" + this.fiyat + ", kategoriId=" + this.getKategoriId() + '}';
    }
}

