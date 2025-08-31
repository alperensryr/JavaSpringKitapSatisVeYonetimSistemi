/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.dto.EditKullaniciViewModel
 *  javax.validation.constraints.Email
 *  javax.validation.constraints.NotBlank
 */
package com.alperen.kitapsatissistemi.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class EditKullaniciViewModel {
    private Long id;
    @NotBlank(message="Ad Soyad alan\u0131 zorunludur.")
    private @NotBlank(message="Ad Soyad alan\u0131 zorunludur.") String adSoyad = "";
    @NotBlank(message="Email alan\u0131 zorunludur.")
    @Email(message="Ge\u00e7erli bir email adresi giriniz.")
    private @NotBlank(message="Email alan\u0131 zorunludur.") @Email(message="Ge\u00e7erli bir email adresi giriniz.") String email = "";
    @NotBlank(message="Rol alan\u0131 zorunludur.")
    private @NotBlank(message="Rol alan\u0131 zorunludur.") String rol = "";
    private LocalDateTime kayitTarihi;

    public EditKullaniciViewModel() {
    }

    public EditKullaniciViewModel(Long id, String adSoyad, String email, String rol, LocalDateTime kayitTarihi) {
        this.id = id;
        this.adSoyad = adSoyad;
        this.email = email;
        this.rol = rol;
        this.kayitTarihi = kayitTarihi;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdSoyad() {
        return this.adSoyad;
    }

    public void setAdSoyad(String adSoyad) {
        this.adSoyad = adSoyad;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRol() {
        return this.rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public LocalDateTime getKayitTarihi() {
        return this.kayitTarihi;
    }

    public void setKayitTarihi(LocalDateTime kayitTarihi) {
        this.kayitTarihi = kayitTarihi;
    }
}

