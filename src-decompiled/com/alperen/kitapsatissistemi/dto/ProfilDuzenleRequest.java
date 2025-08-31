/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.dto.ProfilDuzenleRequest
 *  javax.validation.constraints.Email
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.Size
 */
package com.alperen.kitapsatissistemi.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ProfilDuzenleRequest {
    @NotBlank(message="Ad soyad zorunludur.")
    @Size(max=100, message="Ad soyad en fazla 100 karakter olabilir.")
    private @NotBlank(message="Ad soyad zorunludur.") @Size(max=100, message="Ad soyad en fazla 100 karakter olabilir.") String adSoyad;
    @NotBlank(message="Email zorunludur.")
    @Email(message="Ge\u00e7erli bir email adresi giriniz.")
    @Size(max=100, message="Email en fazla 100 karakter olabilir.")
    private @NotBlank(message="Email zorunludur.") @Email(message="Ge\u00e7erli bir email adresi giriniz.") @Size(max=100, message="Email en fazla 100 karakter olabilir.") String email;

    public ProfilDuzenleRequest() {
    }

    public ProfilDuzenleRequest(String adSoyad, String email) {
        this.adSoyad = adSoyad;
        this.email = email;
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

    public String toString() {
        return "ProfilDuzenleRequest{adSoyad='" + this.adSoyad + '\'' + ", email='" + this.email + '\'' + '}';
    }
}

