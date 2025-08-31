/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.dto.ChangePasswordViewModel
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.Size
 */
package com.alperen.kitapsatissistemi.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ChangePasswordViewModel {
    private Long kullaniciId;
    private String kullaniciAd = "";
    @NotBlank(message="Yeni \u015fifre zorunludur.")
    @Size(min=6, message="\u015eifre en az 6 karakter olmal\u0131d\u0131r.")
    private @NotBlank(message="Yeni \u015fifre zorunludur.") @Size(min=6, message="\u015eifre en az 6 karakter olmal\u0131d\u0131r.") String yeniSifre = "";
    @NotBlank(message="\u015eifre tekrar\u0131 zorunludur.")
    private @NotBlank(message="\u015eifre tekrar\u0131 zorunludur.") String yeniSifreTekrar = "";

    public ChangePasswordViewModel() {
    }

    public ChangePasswordViewModel(Long kullaniciId, String kullaniciAd) {
        this.kullaniciId = kullaniciId;
        this.kullaniciAd = kullaniciAd;
    }

    public Long getKullaniciId() {
        return this.kullaniciId;
    }

    public void setKullaniciId(Long kullaniciId) {
        this.kullaniciId = kullaniciId;
    }

    public String getKullaniciAd() {
        return this.kullaniciAd;
    }

    public void setKullaniciAd(String kullaniciAd) {
        this.kullaniciAd = kullaniciAd;
    }

    public String getYeniSifre() {
        return this.yeniSifre;
    }

    public void setYeniSifre(String yeniSifre) {
        this.yeniSifre = yeniSifre;
    }

    public String getYeniSifreTekrar() {
        return this.yeniSifreTekrar;
    }

    public void setYeniSifreTekrar(String yeniSifreTekrar) {
        this.yeniSifreTekrar = yeniSifreTekrar;
    }

    public boolean isPasswordMatching() {
        return this.yeniSifre != null && this.yeniSifre.equals(this.yeniSifreTekrar);
    }
}

