/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Favori
 *  com.alperen.kitapsatissistemi.entity.Kitap
 *  com.alperen.kitapsatissistemi.entity.Kullanici
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  javax.persistence.Entity
 *  javax.persistence.FetchType
 *  javax.persistence.GeneratedValue
 *  javax.persistence.GenerationType
 *  javax.persistence.Id
 *  javax.persistence.JoinColumn
 *  javax.persistence.ManyToOne
 *  javax.persistence.Table
 *  javax.validation.constraints.NotNull
 */
package com.alperen.kitapsatissistemi.entity;

import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="favoriler")
public class Favori {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @NotNull(message="Kullan\u0131c\u0131 zorunludur.")
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="kullanici_id", nullable=false)
    @JsonIgnore
    private @NotNull(message="Kullan\u0131c\u0131 zorunludur.") Kullanici kullanici;
    @NotNull(message="Kitap zorunludur.")
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="kitap_id", nullable=false)
    @JsonIgnore
    private @NotNull(message="Kitap zorunludur.") Kitap kitap;

    public Favori() {
    }

    public Favori(Kullanici kullanici, Kitap kitap) {
        this.kullanici = kullanici;
        this.kitap = kitap;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKullaniciId() {
        return this.kullanici != null ? this.kullanici.getId() : null;
    }

    public Long getKitapId() {
        return this.kitap != null ? this.kitap.getId() : null;
    }

    public Kullanici getKullanici() {
        return this.kullanici;
    }

    public void setKullanici(Kullanici kullanici) {
        this.kullanici = kullanici;
    }

    public Kitap getKitap() {
        return this.kitap;
    }

    public void setKitap(Kitap kitap) {
        this.kitap = kitap;
    }

    public String toString() {
        return "Favori{id=" + this.id + ", kullaniciId=" + this.getKullaniciId() + ", kitapId=" + this.getKitapId() + '}';
    }
}

