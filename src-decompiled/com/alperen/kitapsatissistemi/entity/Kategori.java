/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Kategori
 *  javax.persistence.Column
 *  javax.persistence.Entity
 *  javax.persistence.GeneratedValue
 *  javax.persistence.GenerationType
 *  javax.persistence.Id
 *  javax.persistence.Table
 *  javax.persistence.Transient
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.Size
 */
package com.alperen.kitapsatissistemi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="kategoriler")
public class Kategori {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message="Kategori ad\u0131 zorunludur.")
    @Size(max=100, message="Kategori ad\u0131 en fazla 100 karakter olabilir.")
    @Column(name="ad", nullable=false, length=100)
    private @NotBlank(message="Kategori ad\u0131 zorunludur.") @Size(max=100, message="Kategori ad\u0131 en fazla 100 karakter olabilir.") String ad;
    @Size(max=500, message="A\u00e7\u0131klama en fazla 500 karakter olabilir.")
    @Column(name="aciklama", length=500)
    private @Size(max=500, message="A\u00e7\u0131klama en fazla 500 karakter olabilir.") String aciklama;
    @Transient
    private Integer kitapSayisi;

    public Kategori() {
    }

    public Kategori(String ad) {
        this.ad = ad;
    }

    public Kategori(String ad, String aciklama) {
        this.ad = ad;
        this.aciklama = aciklama;
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

    public String getAciklama() {
        return this.aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Integer getKitapSayisi() {
        return this.kitapSayisi;
    }

    public void setKitapSayisi(Integer kitapSayisi) {
        this.kitapSayisi = kitapSayisi;
    }

    public String toString() {
        return "Kategori{id=" + this.id + ", ad='" + this.ad + '\'' + ", aciklama='" + this.aciklama + '\'' + '}';
    }
}

