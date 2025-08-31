/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.SiparisDetay
 *  com.alperen.kitapsatissistemi.repository.SiparisDetayRepository
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.jpa.repository.EntityGraph
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.data.jpa.repository.Query
 *  org.springframework.data.repository.query.Param
 *  org.springframework.stereotype.Repository
 */
package com.alperen.kitapsatissistemi.repository;

import com.alperen.kitapsatissistemi.entity.SiparisDetay;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SiparisDetayRepository
extends JpaRepository<SiparisDetay, Long> {
    public List<SiparisDetay> findBySiparis_Id(Long var1);

    @EntityGraph(attributePaths={"kitap", "kitap.kategori", "siparis", "siparis.kullanici"})
    public Page<SiparisDetay> findBySiparis_Id(Long var1, Pageable var2);

    public List<SiparisDetay> findByKitap_Id(Long var1);

    @EntityGraph(attributePaths={"kitap", "kitap.kategori", "siparis", "siparis.kullanici"})
    public Page<SiparisDetay> findByKitap_Id(Long var1, Pageable var2);

    public Optional<SiparisDetay> findBySiparis_IdAndKitap_Id(Long var1, Long var2);

    @Query(value="SELECT sd FROM SiparisDetay sd LEFT JOIN FETCH sd.kitap k LEFT JOIN FETCH k.kategori WHERE sd.siparis.id = :siparis_Id")
    public List<SiparisDetay> findBySiparis_IdWithKitap(@Param(value="siparis_Id") Long var1);

    @Query(value="SELECT sd FROM SiparisDetay sd LEFT JOIN FETCH sd.siparis s LEFT JOIN FETCH s.kullanici WHERE sd.kitap.id = :kitap_Id")
    public List<SiparisDetay> findByKitap_IdWithSiparis(@Param(value="kitap_Id") Long var1);

    @Query(value="SELECT COALESCE(SUM(sd.adet), 0) FROM SiparisDetay sd JOIN sd.siparis s WHERE sd.kitap.id = :kitap_Id AND s.durum = 'Onayland\u0131'")
    public Long findToplamSatisAdetiByKitap_Id(@Param(value="kitap_Id") Long var1);

    @Query(value="SELECT COALESCE(SUM(sd.adet), 0) FROM SiparisDetay sd JOIN sd.siparis s WHERE sd.kitap.id = :kitap_Id AND s.durum = 'Onayland\u0131'")
    public Integer getTotalSalesQuantityByKitap_Id(@Param(value="kitap_Id") Long var1);

    @Query(value="SELECT sd FROM SiparisDetay sd JOIN FETCH sd.kitap JOIN FETCH sd.siparis")
    public List<SiparisDetay> findAllWithKitapAndSiparis();

    @Query(value="SELECT COALESCE(SUM(sd.fiyat * sd.adet), 0) FROM SiparisDetay sd JOIN sd.siparis s WHERE sd.kitap.id = :kitap_Id AND s.durum = 'Onayland\u0131'")
    public BigDecimal findToplamSatisTutariByKitap_Id(@Param(value="kitap_Id") Long var1);

    @Query(value="SELECT sd.kitap.id, SUM(sd.adet) as toplamAdet FROM SiparisDetay sd JOIN sd.siparis s WHERE s.durum = 'Onayland\u0131' GROUP BY sd.kitap.id ORDER BY toplamAdet DESC")
    public List<Object[]> findEnCokSatilanKitaplar();

    @Query(value="SELECT sd.kitap.id, SUM(sd.fiyat * sd.adet) as toplamGelir FROM SiparisDetay sd JOIN sd.siparis s WHERE s.durum = 'Onayland\u0131' GROUP BY sd.kitap.id ORDER BY toplamGelir DESC")
    public List<Object[]> findEnCokGelirGetirenKitaplar();

    @Query(value="SELECT COALESCE(SUM(sd.fiyat * sd.adet), 0) FROM SiparisDetay sd WHERE sd.siparis.id = :siparis_Id")
    public BigDecimal calculateToplamTutarBySiparis_Id(@Param(value="siparis_Id") Long var1);

    public List<SiparisDetay> findByAdetGreaterThan(Integer var1);

    public List<SiparisDetay> findByFiyatGreaterThan(BigDecimal var1);

    @Query(value="SELECT k.kategori.ad, SUM(sd.adet), SUM(sd.fiyat * sd.adet) FROM SiparisDetay sd JOIN sd.kitap k JOIN sd.siparis s WHERE s.durum = 'Onayland\u0131' GROUP BY k.kategori.ad ORDER BY SUM(sd.fiyat * sd.adet) DESC")
    public List<Object[]> findKategoriBazindaSatisIstatistikleri();

    @Query(value="SELECT k.yazar, SUM(sd.adet), SUM(sd.fiyat * sd.adet) FROM SiparisDetay sd JOIN sd.kitap k JOIN sd.siparis s WHERE s.durum = 'Onayland\u0131' GROUP BY k.yazar ORDER BY SUM(sd.fiyat * sd.adet) DESC")
    public List<Object[]> findYazarBazindaSatisIstatistikleri();

    @Query(value="SELECT k.kategori.ad, SUM(sd.adet), SUM(sd.fiyat * sd.adet) FROM SiparisDetay sd JOIN sd.kitap k JOIN sd.siparis s WHERE s.durum = 'Onayland\u0131' GROUP BY k.kategori.ad ORDER BY SUM(sd.fiyat * sd.adet) DESC")
    public List<Object[]> getSalesStatsByCategory();

    @Query(value="SELECT k.yazar, SUM(sd.adet), SUM(sd.fiyat * sd.adet) FROM SiparisDetay sd JOIN sd.kitap k JOIN sd.siparis s WHERE s.durum = 'Onayland\u0131' GROUP BY k.yazar ORDER BY SUM(sd.fiyat * sd.adet) DESC")
    public List<Object[]> getSalesStatsByAuthor();

    public long countBySiparis_Id(Long var1);

    public long countByKitap_Id(Long var1);
}

