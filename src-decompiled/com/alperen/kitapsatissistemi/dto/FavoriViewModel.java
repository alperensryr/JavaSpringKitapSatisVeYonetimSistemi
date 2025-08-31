/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.dto.FavoriViewModel
 */
package com.alperen.kitapsatissistemi.dto;

public class FavoriViewModel {
    private Long id;
    private String kullaniciAd = "";
    private String kitapAd = "";

    public FavoriViewModel() {
    }

    public FavoriViewModel(Long id, String kullaniciAd, String kitapAd) {
        this.id = id;
        this.kullaniciAd = kullaniciAd;
        this.kitapAd = kitapAd;
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

    public String getKitapAd() {
        return this.kitapAd;
    }

    public void setKitapAd(String kitapAd) {
        this.kitapAd = kitapAd;
    }
}

