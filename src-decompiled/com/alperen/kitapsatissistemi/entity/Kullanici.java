/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Kullanici
 *  javax.persistence.Column
 *  javax.persistence.Entity
 *  javax.persistence.GeneratedValue
 *  javax.persistence.GenerationType
 *  javax.persistence.Id
 *  javax.persistence.PrePersist
 *  javax.persistence.Table
 *  javax.validation.constraints.Email
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.Size
 *  javax.validation.constraints.Size$List
 */
package com.alperen.kitapsatissistemi.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="kullanicilar")
public class Kullanici {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message="Ad soyad zorunludur.")
    @Size(max=100, message="Ad soyad en fazla 100 karakter olabilir.")
    @Column(name="ad_soyad", nullable=false, length=100)
    private @NotBlank(message="Ad soyad zorunludur.") @Size(max=100, message="Ad soyad en fazla 100 karakter olabilir.") String adSoyad;
    @NotBlank(message="Email zorunludur.")
    @Email(message="Ge\u00e7erli bir email adresi giriniz.")
    @Size(max=100, message="Email en fazla 100 karakter olabilir.")
    @Column(name="email", nullable=false, length=100, unique=true)
    private @NotBlank(message="Email zorunludur.") @Email(message="Ge\u00e7erli bir email adresi giriniz.") @Size(max=100, message="Email en fazla 100 karakter olabilir.") String email;
    @NotBlank(message="\u015eifre zorunludur.")
    @Size.List(value={@Size(min=6, message="\u015eifre en az 6 karakter olmal\u0131d\u0131r."), @Size(max=255, message="\u015eifre en fazla 255 karakter olabilir.")})
    @Column(name="sifre_hash", nullable=false, length=255)
    private @NotBlank(message="\u015eifre zorunludur.") @Size.List(value={@Size(min=6, message="\u015eifre en az 6 karakter olmal\u0131d\u0131r."), @Size(max=255, message="\u015eifre en fazla 255 karakter olabilir.")}) String sifreHash;
    @NotBlank(message="Rol zorunludur.")
    @Size(max=50, message="Rol en fazla 50 karakter olabilir.")
    @Column(name="rol", nullable=false, length=50)
    private @NotBlank(message="Rol zorunludur.") @Size(max=50, message="Rol en fazla 50 karakter olabilir.") String rol = "User";
    @Column(name="kayit_tarihi", nullable=false)
    private LocalDateTime kayitTarihi = LocalDateTime.now();

    public Kullanici() {
    }

    public Kullanici(String adSoyad, String email, String sifreHash) {
        this();
        this.adSoyad = adSoyad;
        this.email = email;
        this.sifreHash = sifreHash;
    }

    public Kullanici(String adSoyad, String email, String sifreHash, String rol) {
        this(adSoyad, email, sifreHash);
        this.rol = rol;
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

    public String getSifreHash() {
        return this.sifreHash;
    }

    public void setSifreHash(String sifreHash) {
        this.sifreHash = sifreHash;
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

    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(this.rol);
    }

    public boolean isUser() {
        return "User".equalsIgnoreCase(this.rol);
    }

    @PrePersist
    protected void onCreate() {
        if (this.kayitTarihi == null) {
            this.kayitTarihi = LocalDateTime.now();
        }
        if (this.rol == null || this.rol.trim().isEmpty()) {
            this.rol = "User";
        }
    }

    public String toString() {
        return "Kullanici{id=" + this.id + ", adSoyad='" + this.adSoyad + '\'' + ", email='" + this.email + '\'' + ", rol='" + this.rol + '\'' + ", kayitTarihi=" + this.kayitTarihi + '}';
    }
}

