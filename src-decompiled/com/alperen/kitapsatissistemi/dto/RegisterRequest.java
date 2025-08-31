/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.dto.RegisterRequest
 *  javax.validation.constraints.Email
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.Size
 */
package com.alperen.kitapsatissistemi.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterRequest {
    @NotBlank(message="Ad soyad bo\u015f olamaz")
    @Size(min=2, max=100, message="Ad soyad 2-100 karakter aras\u0131nda olmal\u0131d\u0131r")
    private @NotBlank(message="Ad soyad bo\u015f olamaz") @Size(min=2, max=100, message="Ad soyad 2-100 karakter aras\u0131nda olmal\u0131d\u0131r") String adSoyad;
    @NotBlank(message="Email adresi bo\u015f olamaz")
    @Email(message="Ge\u00e7erli bir email adresi giriniz")
    private @NotBlank(message="Email adresi bo\u015f olamaz") @Email(message="Ge\u00e7erli bir email adresi giriniz") String email;
    @NotBlank(message="\u015eifre bo\u015f olamaz")
    @Size(min=6, max=50, message="\u015eifre 6-50 karakter aras\u0131nda olmal\u0131d\u0131r")
    private @NotBlank(message="\u015eifre bo\u015f olamaz") @Size(min=6, max=50, message="\u015eifre 6-50 karakter aras\u0131nda olmal\u0131d\u0131r") String sifre;
    @NotBlank(message="\u015eifre tekrar\u0131 bo\u015f olamaz")
    private @NotBlank(message="\u015eifre tekrar\u0131 bo\u015f olamaz") String sifreTekrar;

    public RegisterRequest() {
    }

    public RegisterRequest(String adSoyad, String email, String sifre, String sifreTekrar) {
        this.adSoyad = adSoyad;
        this.email = email;
        this.sifre = sifre;
        this.sifreTekrar = sifreTekrar;
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

    public String getSifre() {
        return this.sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getSifreTekrar() {
        return this.sifreTekrar;
    }

    public void setSifreTekrar(String sifreTekrar) {
        this.sifreTekrar = sifreTekrar;
    }

    public boolean isPasswordMatching() {
        return this.sifre != null && this.sifre.equals(this.sifreTekrar);
    }
}

