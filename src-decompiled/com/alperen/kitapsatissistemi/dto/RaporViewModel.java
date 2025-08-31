/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.dto.KitapSiparisIstatistik
 *  com.alperen.kitapsatissistemi.dto.RaporViewModel
 *  com.alperen.kitapsatissistemi.entity.Kitap
 *  com.alperen.kitapsatissistemi.entity.Siparis
 */
package com.alperen.kitapsatissistemi.dto;

import com.alperen.kitapsatissistemi.dto.KitapSiparisIstatistik;
import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.entity.Siparis;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaporViewModel {
    private int toplamKitap;
    private int toplamKullanici;
    private int toplamKategori;
    private int buAySiparis;
    private List<Siparis> sonSiparisler = new ArrayList();
    private List<Kitap> populerKitaplar = new ArrayList();
    private List<KitapSiparisIstatistik> kitapSiparisIstatistikleri = new ArrayList();
    private Map<String, Integer> kategoriDagilimi = new HashMap();
    private Map<String, Integer> aylikSiparisler = new HashMap();

    public RaporViewModel() {
    }

    public RaporViewModel(int toplamKitap, int toplamKullanici, int toplamKategori, int buAySiparis) {
        this.toplamKitap = toplamKitap;
        this.toplamKullanici = toplamKullanici;
        this.toplamKategori = toplamKategori;
        this.buAySiparis = buAySiparis;
    }

    public int getToplamKitap() {
        return this.toplamKitap;
    }

    public void setToplamKitap(int toplamKitap) {
        this.toplamKitap = toplamKitap;
    }

    public int getToplamKullanici() {
        return this.toplamKullanici;
    }

    public void setToplamKullanici(int toplamKullanici) {
        this.toplamKullanici = toplamKullanici;
    }

    public int getToplamKategori() {
        return this.toplamKategori;
    }

    public void setToplamKategori(int toplamKategori) {
        this.toplamKategori = toplamKategori;
    }

    public int getBuAySiparis() {
        return this.buAySiparis;
    }

    public void setBuAySiparis(int buAySiparis) {
        this.buAySiparis = buAySiparis;
    }

    public List<Siparis> getSonSiparisler() {
        return this.sonSiparisler;
    }

    public void setSonSiparisler(List<Siparis> sonSiparisler) {
        this.sonSiparisler = sonSiparisler;
    }

    public List<Kitap> getPopulerKitaplar() {
        return this.populerKitaplar;
    }

    public void setPopulerKitaplar(List<Kitap> populerKitaplar) {
        this.populerKitaplar = populerKitaplar;
    }

    public List<KitapSiparisIstatistik> getKitapSiparisIstatistikleri() {
        return this.kitapSiparisIstatistikleri;
    }

    public void setKitapSiparisIstatistikleri(List<KitapSiparisIstatistik> kitapSiparisIstatistikleri) {
        this.kitapSiparisIstatistikleri = kitapSiparisIstatistikleri;
    }

    public Map<String, Integer> getKategoriDagilimi() {
        return this.kategoriDagilimi;
    }

    public void setKategoriDagilimi(Map<String, Integer> kategoriDagilimi) {
        this.kategoriDagilimi = kategoriDagilimi;
    }

    public Map<String, Integer> getAylikSiparisler() {
        return this.aylikSiparisler;
    }

    public void setAylikSiparisler(Map<String, Integer> aylikSiparisler) {
        this.aylikSiparisler = aylikSiparisler;
    }
}

