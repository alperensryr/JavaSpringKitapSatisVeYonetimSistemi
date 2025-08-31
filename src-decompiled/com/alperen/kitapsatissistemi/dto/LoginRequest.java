/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.dto.LoginRequest
 *  javax.validation.constraints.Email
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.Size
 */
package com.alperen.kitapsatissistemi.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginRequest {
    @NotBlank(message="Email adresi bo\u015f olamaz")
    @Email(message="Ge\u00e7erli bir email adresi giriniz")
    private @NotBlank(message="Email adresi bo\u015f olamaz") @Email(message="Ge\u00e7erli bir email adresi giriniz") String email;
    @NotBlank(message="\u015eifre bo\u015f olamaz")
    @Size(min=6, message="\u015eifre en az 6 karakter olmal\u0131d\u0131r")
    private @NotBlank(message="\u015eifre bo\u015f olamaz") @Size(min=6, message="\u015eifre en az 6 karakter olmal\u0131d\u0131r") String sifre;

    public LoginRequest() {
    }

    public LoginRequest(String email, String sifre) {
        this.email = email;
        this.sifre = sifre;
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
}

