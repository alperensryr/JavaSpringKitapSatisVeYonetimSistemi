/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Kullanici
 *  com.alperen.kitapsatissistemi.entity.Siparis
 *  com.alperen.kitapsatissistemi.entity.SiparisDetay
 *  javax.persistence.CascadeType
 *  javax.persistence.Column
 *  javax.persistence.Entity
 *  javax.persistence.FetchType
 *  javax.persistence.GeneratedValue
 *  javax.persistence.GenerationType
 *  javax.persistence.Id
 *  javax.persistence.JoinColumn
 *  javax.persistence.ManyToOne
 *  javax.persistence.OneToMany
 *  javax.persistence.PrePersist
 *  javax.persistence.Table
 *  javax.validation.constraints.NotBlank
 *  javax.validation.constraints.NotNull
 *  javax.validation.constraints.Size
 */
package com.alperen.kitapsatissistemi.entity;

import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.entity.SiparisDetay;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="siparisler")
public class Siparis {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="siparis_tarihi", nullable=false)
    private LocalDateTime siparisTarihi;
    @NotNull(message="Toplam tutar zorunludur.")
    @Column(name="toplam_tutar", nullable=false, precision=18, scale=2)
    private @NotNull(message="Toplam tutar zorunludur.") BigDecimal toplamTutar;
    @NotBlank(message="Durum zorunludur.")
    @Size(max=50, message="Durum en fazla 50 karakter olabilir.")
    @Column(name="durum", length=50)
    private @NotBlank(message="Durum zorunludur.") @Size(max=50, message="Durum en fazla 50 karakter olabilir.") String durum = "Beklemede";
    @NotNull(message="Kullan\u0131c\u0131 zorunludur.")
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="kullanici_id", nullable=false)
    private @NotNull(message="Kullan\u0131c\u0131 zorunludur.") Kullanici kullanici;
    @OneToMany(mappedBy="siparis", cascade={CascadeType.ALL}, fetch=FetchType.LAZY, orphanRemoval=true)
    private List<SiparisDetay> siparisDetaylari = new ArrayList();
    @Column(name="teslimat_ad_soyad", length=100)
    private String teslimatAdSoyad;
    @Column(name="teslimat_telefon", length=20)
    private String teslimatTelefon;
    @Column(name="teslimat_sehir", length=50)
    private String teslimatSehir;
    @Column(name="teslimat_ilce", length=50)
    private String teslimatIlce;
    @Column(name="teslimat_mahalle", length=100)
    private String teslimatMahalle;
    @Column(name="teslimat_adres_detay", length=500)
    private String teslimatAdresDetay;
    @Column(name="teslimat_posta_kodu", length=10)
    private String teslimatPostaKodu;
    @Column(name="odeme_yontemi", length=50)
    private String odemeYontemi;
    @Column(name="kart_sahibi_adi", length=100)
    private String kartSahibiAdi;
    @Column(name="kart_numarasi_son4", length=4)
    private String kartNumarasiSon4;
    @Column(name="odeme_durumu", length=50)
    private String odemeDurumu = "Beklemede";
    @Column(name="kargo_takip_no", length=100)
    private String kargoTakipNo;
    @Column(name="kargo_firmasi", length=100)
    private String kargoFirmasi;
    @Column(name="tahmini_teslimat_tarihi")
    private LocalDateTime tahminiTeslimatTarihi;
    @Column(name="gercek_teslimat_tarihi")
    private LocalDateTime gercekTeslimatTarihi;

    public Siparis() {
        this.siparisTarihi = LocalDateTime.now();
    }

    public Siparis(Kullanici kullanici, BigDecimal toplamTutar) {
        this();
        this.kullanici = kullanici;
        this.toplamTutar = toplamTutar;
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

    public LocalDateTime getSiparisTarihi() {
        return this.siparisTarihi;
    }

    public void setSiparisTarihi(LocalDateTime siparisTarihi) {
        this.siparisTarihi = siparisTarihi;
    }

    public BigDecimal getToplamTutar() {
        return this.toplamTutar;
    }

    public void setToplamTutar(BigDecimal toplamTutar) {
        this.toplamTutar = toplamTutar;
    }

    public String getDurum() {
        return this.durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }

    public Kullanici getKullanici() {
        return this.kullanici;
    }

    public void setKullanici(Kullanici kullanici) {
        this.kullanici = kullanici;
    }

    public List<SiparisDetay> getSiparisDetaylari() {
        return this.siparisDetaylari;
    }

    public void setSiparisDetaylari(List<SiparisDetay> siparisDetaylari) {
        this.siparisDetaylari = siparisDetaylari;
    }

    public void addSiparisDetay(SiparisDetay siparisDetay) {
        this.siparisDetaylari.add(siparisDetay);
        siparisDetay.setSiparis(this);
    }

    public void removeSiparisDetay(SiparisDetay siparisDetay) {
        this.siparisDetaylari.remove(siparisDetay);
        siparisDetay.setSiparis(null);
    }

    public boolean isBeklemede() {
        return "Beklemede".equals(this.durum);
    }

    public boolean isOnaylandi() {
        return "Onayland\u0131".equals(this.durum);
    }

    public boolean isIptalEdildi() {
        return "\u0130ptal Edildi".equals(this.durum);
    }

    public String getTeslimatAdSoyad() {
        return this.teslimatAdSoyad;
    }

    public void setTeslimatAdSoyad(String teslimatAdSoyad) {
        this.teslimatAdSoyad = teslimatAdSoyad;
    }

    public String getTeslimatTelefon() {
        return this.teslimatTelefon;
    }

    public void setTeslimatTelefon(String teslimatTelefon) {
        this.teslimatTelefon = teslimatTelefon;
    }

    public String getTeslimatSehir() {
        return this.teslimatSehir;
    }

    public void setTeslimatSehir(String teslimatSehir) {
        this.teslimatSehir = teslimatSehir;
    }

    public String getTeslimatIlce() {
        return this.teslimatIlce;
    }

    public void setTeslimatIlce(String teslimatIlce) {
        this.teslimatIlce = teslimatIlce;
    }

    public String getTeslimatMahalle() {
        return this.teslimatMahalle;
    }

    public void setTeslimatMahalle(String teslimatMahalle) {
        this.teslimatMahalle = teslimatMahalle;
    }

    public String getTeslimatAdresDetay() {
        return this.teslimatAdresDetay;
    }

    public void setTeslimatAdresDetay(String teslimatAdresDetay) {
        this.teslimatAdresDetay = teslimatAdresDetay;
    }

    public String getTeslimatPostaKodu() {
        return this.teslimatPostaKodu;
    }

    public void setTeslimatPostaKodu(String teslimatPostaKodu) {
        this.teslimatPostaKodu = teslimatPostaKodu;
    }

    public String getOdemeYontemi() {
        return this.odemeYontemi;
    }

    public void setOdemeYontemi(String odemeYontemi) {
        this.odemeYontemi = odemeYontemi;
    }

    public String getKartSahibiAdi() {
        return this.kartSahibiAdi;
    }

    public void setKartSahibiAdi(String kartSahibiAdi) {
        this.kartSahibiAdi = kartSahibiAdi;
    }

    public String getKartNumarasiSon4() {
        return this.kartNumarasiSon4;
    }

    public void setKartNumarasiSon4(String kartNumarasiSon4) {
        this.kartNumarasiSon4 = kartNumarasiSon4;
    }

    public String getOdemeDurumu() {
        return this.odemeDurumu;
    }

    public void setOdemeDurumu(String odemeDurumu) {
        this.odemeDurumu = odemeDurumu;
    }

    public String getKargoTakipNo() {
        return this.kargoTakipNo;
    }

    public void setKargoTakipNo(String kargoTakipNo) {
        this.kargoTakipNo = kargoTakipNo;
    }

    public String getKargoFirmasi() {
        return this.kargoFirmasi;
    }

    public void setKargoFirmasi(String kargoFirmasi) {
        this.kargoFirmasi = kargoFirmasi;
    }

    public LocalDateTime getTahminiTeslimatTarihi() {
        return this.tahminiTeslimatTarihi;
    }

    public void setTahminiTeslimatTarihi(LocalDateTime tahminiTeslimatTarihi) {
        this.tahminiTeslimatTarihi = tahminiTeslimatTarihi;
    }

    public LocalDateTime getGercekTeslimatTarihi() {
        return this.gercekTeslimatTarihi;
    }

    public void setGercekTeslimatTarihi(LocalDateTime gercekTeslimatTarihi) {
        this.gercekTeslimatTarihi = gercekTeslimatTarihi;
    }

    public String getTamTeslimatAdresi() {
        if (this.teslimatAdresDetay == null) {
            return null;
        }
        return String.format("%s, %s, %s/%s", this.teslimatAdresDetay, this.teslimatMahalle, this.teslimatIlce, this.teslimatSehir);
    }

    public boolean isOdemeOnaylandi() {
        return "Onayland\u0131".equals(this.odemeDurumu);
    }

    public boolean isKargoya() {
        return this.kargoTakipNo != null && !this.kargoTakipNo.trim().isEmpty();
    }

    public boolean isTeslimEdildi() {
        return this.gercekTeslimatTarihi != null;
    }

    @PrePersist
    protected void onCreate() {
        if (this.siparisTarihi == null) {
            this.siparisTarihi = LocalDateTime.now();
        }
        if (this.durum == null || this.durum.trim().isEmpty()) {
            this.durum = "Beklemede";
        }
    }

    public String toString() {
        return "Siparis{id=" + this.id + ", kullaniciId=" + this.getKullaniciId() + ", siparisTarihi=" + this.siparisTarihi + ", toplamTutar=" + this.toplamTutar + ", durum='" + this.durum + '\'' + '}';
    }
}

