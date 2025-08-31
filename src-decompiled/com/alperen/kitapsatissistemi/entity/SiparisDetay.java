/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Kitap
 *  com.alperen.kitapsatissistemi.entity.Siparis
 *  com.alperen.kitapsatissistemi.entity.SiparisDetay
 *  javax.persistence.Column
 *  javax.persistence.Entity
 *  javax.persistence.FetchType
 *  javax.persistence.GeneratedValue
 *  javax.persistence.GenerationType
 *  javax.persistence.Id
 *  javax.persistence.JoinColumn
 *  javax.persistence.ManyToOne
 *  javax.persistence.Table
 *  javax.validation.constraints.Min
 *  javax.validation.constraints.NotNull
 */
package com.alperen.kitapsatissistemi.entity;

import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.entity.Siparis;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="siparis_detaylari")
public class SiparisDetay {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @NotNull(message="Adet zorunludur.")
    @Min(value=1L, message="Adet en az 1 olmal\u0131d\u0131r.")
    @Column(name="adet", nullable=false)
    private @NotNull(message="Adet zorunludur.") @Min(value=1L, message="Adet en az 1 olmal\u0131d\u0131r.") Integer adet;
    @NotNull(message="Fiyat zorunludur.")
    @Column(name="fiyat", nullable=false, precision=18, scale=2)
    private @NotNull(message="Fiyat zorunludur.") BigDecimal fiyat;
    @NotNull(message="Sipari\u015f zorunludur.")
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="siparis_id", nullable=false)
    private @NotNull(message="Sipari\u015f zorunludur.") Siparis siparis;
    @NotNull(message="Kitap zorunludur.")
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="kitap_id", nullable=false)
    private @NotNull(message="Kitap zorunludur.") Kitap kitap;

    public SiparisDetay() {
    }

    public SiparisDetay(Siparis siparis, Kitap kitap, Integer adet, BigDecimal fiyat) {
        this.siparis = siparis;
        this.kitap = kitap;
        this.adet = adet;
        this.fiyat = fiyat;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSiparisId() {
        return this.siparis != null ? this.siparis.getId() : null;
    }

    public Long getKitapId() {
        return this.kitap != null ? this.kitap.getId() : null;
    }

    public Integer getAdet() {
        return this.adet;
    }

    public void setAdet(Integer adet) {
        this.adet = adet;
    }

    public BigDecimal getFiyat() {
        return this.fiyat;
    }

    public void setFiyat(BigDecimal fiyat) {
        this.fiyat = fiyat;
    }

    public Siparis getSiparis() {
        return this.siparis;
    }

    public void setSiparis(Siparis siparis) {
        this.siparis = siparis;
    }

    public Kitap getKitap() {
        return this.kitap;
    }

    public void setKitap(Kitap kitap) {
        this.kitap = kitap;
    }

    public BigDecimal getToplamFiyat() {
        if (this.adet != null && this.fiyat != null) {
            return this.fiyat.multiply(BigDecimal.valueOf(this.adet.intValue()));
        }
        return BigDecimal.ZERO;
    }

    public String toString() {
        return "SiparisDetay{id=" + this.id + ", siparisId=" + this.getSiparisId() + ", kitapId=" + this.getKitapId() + ", adet=" + this.adet + ", fiyat=" + this.fiyat + '}';
    }
}

