/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Adres
 *  com.alperen.kitapsatissistemi.entity.Kullanici
 *  javax.persistence.Column
 *  javax.persistence.Entity
 *  javax.persistence.FetchType
 *  javax.persistence.GeneratedValue
 *  javax.persistence.GenerationType
 *  javax.persistence.Id
 *  javax.persistence.JoinColumn
 *  javax.persistence.ManyToOne
 *  javax.persistence.Table
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.Size
 */
package com.alperen.kitapsatissistemi.entity;

import com.alperen.kitapsatissistemi.entity.Kullanici;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name="adresler")
public class Adres {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message="Adres ba\u015fl\u0131\u011f\u0131 zorunludur.")
    @Size(max=100, message="Adres ba\u015fl\u0131\u011f\u0131 en fazla 100 karakter olabilir.")
    @Column(name="baslik", length=100, nullable=false)
    private @NotBlank(message="Adres ba\u015fl\u0131\u011f\u0131 zorunludur.") @Size(max=100, message="Adres ba\u015fl\u0131\u011f\u0131 en fazla 100 karakter olabilir.") String baslik;
    @NotBlank(message="Ad Soyad zorunludur.")
    @Size(max=100, message="Ad Soyad en fazla 100 karakter olabilir.")
    @Column(name="ad_soyad", length=100, nullable=false)
    private @NotBlank(message="Ad Soyad zorunludur.") @Size(max=100, message="Ad Soyad en fazla 100 karakter olabilir.") String adSoyad;
    @NotBlank(message="Telefon zorunludur.")
    @Size(max=20, message="Telefon en fazla 20 karakter olabilir.")
    @Column(name="telefon", length=20, nullable=false)
    private @NotBlank(message="Telefon zorunludur.") @Size(max=20, message="Telefon en fazla 20 karakter olabilir.") String telefon;
    @NotBlank(message="\u015eehir zorunludur.")
    @Size(max=50, message="\u015eehir en fazla 50 karakter olabilir.")
    @Column(name="sehir", length=50, nullable=false)
    private @NotBlank(message="\u015eehir zorunludur.") @Size(max=50, message="\u015eehir en fazla 50 karakter olabilir.") String sehir;
    @NotBlank(message="\u0130l\u00e7e zorunludur.")
    @Size(max=50, message="\u0130l\u00e7e en fazla 50 karakter olabilir.")
    @Column(name="ilce", length=50, nullable=false)
    private @NotBlank(message="\u0130l\u00e7e zorunludur.") @Size(max=50, message="\u0130l\u00e7e en fazla 50 karakter olabilir.") String ilce;
    @NotBlank(message="Mahalle zorunludur.")
    @Size(max=100, message="Mahalle en fazla 100 karakter olabilir.")
    @Column(name="mahalle", length=100, nullable=false)
    private @NotBlank(message="Mahalle zorunludur.") @Size(max=100, message="Mahalle en fazla 100 karakter olabilir.") String mahalle;
    @NotBlank(message="Adres detay\u0131 zorunludur.")
    @Size(max=500, message="Adres detay\u0131 en fazla 500 karakter olabilir.")
    @Column(name="adres_detay", length=500, nullable=false)
    private @NotBlank(message="Adres detay\u0131 zorunludur.") @Size(max=500, message="Adres detay\u0131 en fazla 500 karakter olabilir.") String adresDetay;
    @Size(max=10, message="Posta kodu en fazla 10 karakter olabilir.")
    @Column(name="posta_kodu", length=10)
    private @Size(max=10, message="Posta kodu en fazla 10 karakter olabilir.") String postaKodu;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="kullanici_id", nullable=false)
    private Kullanici kullanici;
    @Column(name="varsayilan")
    private Boolean varsayilan = false;

    public Adres() {
    }

    public Adres(String baslik, String adSoyad, String telefon, String sehir, String ilce, String mahalle, String adresDetay, Kullanici kullanici) {
        this.baslik = baslik;
        this.adSoyad = adSoyad;
        this.telefon = telefon;
        this.sehir = sehir;
        this.ilce = ilce;
        this.mahalle = mahalle;
        this.adresDetay = adresDetay;
        this.kullanici = kullanici;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBaslik() {
        return this.baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getAdSoyad() {
        return this.adSoyad;
    }

    public void setAdSoyad(String adSoyad) {
        this.adSoyad = adSoyad;
    }

    public String getTelefon() {
        return this.telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getSehir() {
        return this.sehir;
    }

    public void setSehir(String sehir) {
        this.sehir = sehir;
    }

    public String getIlce() {
        return this.ilce;
    }

    public void setIlce(String ilce) {
        this.ilce = ilce;
    }

    public String getMahalle() {
        return this.mahalle;
    }

    public void setMahalle(String mahalle) {
        this.mahalle = mahalle;
    }

    public String getAdresDetay() {
        return this.adresDetay;
    }

    public void setAdresDetay(String adresDetay) {
        this.adresDetay = adresDetay;
    }

    public String getPostaKodu() {
        return this.postaKodu;
    }

    public void setPostaKodu(String postaKodu) {
        this.postaKodu = postaKodu;
    }

    public Kullanici getKullanici() {
        return this.kullanici;
    }

    public void setKullanici(Kullanici kullanici) {
        this.kullanici = kullanici;
    }

    public Long getKullaniciId() {
        return this.kullanici != null ? this.kullanici.getId() : null;
    }

    public void setKullaniciId(Long kullaniciId) {
        if (kullaniciId != null) {
            this.kullanici = new Kullanici();
            this.kullanici.setId(kullaniciId);
        }
    }

    public Boolean getVarsayilan() {
        return this.varsayilan;
    }

    public void setVarsayilan(Boolean varsayilan) {
        this.varsayilan = varsayilan;
    }

    public String getTamAdres() {
        return String.format("%s, %s, %s/%s", this.adresDetay, this.mahalle, this.ilce, this.sehir);
    }

    public String toString() {
        return "Adres{id=" + this.id + ", baslik='" + this.baslik + '\'' + ", adSoyad='" + this.adSoyad + '\'' + ", sehir='" + this.sehir + '\'' + ", ilce='" + this.ilce + '\'' + ", varsayilan=" + this.varsayilan + '}';
    }
}

