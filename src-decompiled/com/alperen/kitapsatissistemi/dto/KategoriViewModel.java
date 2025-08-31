/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.dto.KategoriViewModel
 *  com.alperen.kitapsatissistemi.entity.Kitap
 */
package com.alperen.kitapsatissistemi.dto;

import com.alperen.kitapsatissistemi.entity.Kitap;
import java.util.ArrayList;
import java.util.List;

public class KategoriViewModel {
    private Long id;
    private String ad = "";
    private String aciklama = "";
    private List<Kitap> kitaplar = new ArrayList();

    public KategoriViewModel() {
    }

    public KategoriViewModel(Long id, String ad, String aciklama) {
        this.id = id;
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

    public List<Kitap> getKitaplar() {
        return this.kitaplar;
    }

    public void setKitaplar(List<Kitap> kitaplar) {
        this.kitaplar = kitaplar;
    }
}

