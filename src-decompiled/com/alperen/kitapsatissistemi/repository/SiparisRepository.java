/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Siparis
 *  com.alperen.kitapsatissistemi.repository.SiparisRepository
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.jpa.repository.EntityGraph
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.data.jpa.repository.Query
 *  org.springframework.data.repository.query.Param
 *  org.springframework.stereotype.Repository
 */
package com.alperen.kitapsatissistemi.repository;

import com.alperen.kitapsatissistemi.entity.Siparis;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public interface SiparisRepository
extends JpaRepository<Siparis, Long> {
    public List<Siparis> findByKullanici_Id(Long var1);

    public List<Siparis> findByDurum(String var1);

    public List<Siparis> findByKullanici_IdAndDurum(Long var1, String var2);

    public List<Siparis> findBySiparisTarihiAfter(LocalDateTime var1);

    public List<Siparis> findBySiparisTarihiBefore(LocalDateTime var1);

    public List<Siparis> findBySiparisTarihiBetween(LocalDateTime var1, LocalDateTime var2);

    public List<Siparis> findByToplamTutarBetween(BigDecimal var1, BigDecimal var2);

    @Query(value="SELECT s FROM Siparis s LEFT JOIN FETCH s.siparisDetaylari WHERE s.kullanici.id = :kullanici_Id")
    public List<Siparis> findByKullanici_IdWithDetaylar(@Param(value="kullanici_Id") Long var1);

    @Query(value="SELECT s FROM Siparis s LEFT JOIN FETCH s.siparisDetaylari sd LEFT JOIN FETCH sd.kitap WHERE s.id = :id")
    public Optional<Siparis> findByIdWithDetaylar(@Param(value="id") Long var1);

    public long countByKullanici_Id(Long var1);

    public long countByDurum(String var1);

    @Query(value="SELECT COALESCE(SUM(s.toplamTutar), 0) FROM Siparis s WHERE s.kullanici.id = :kullanici_Id AND s.durum = 'Onayland\u0131'")
    public BigDecimal findToplamHarcamaByKullanici_Id(@Param(value="kullanici_Id") Long var1);

    @Query(value="SELECT s FROM Siparis s ORDER BY s.siparisTarihi DESC")
    public List<Siparis> findLatestSiparisler();

    @Query(value="SELECT s FROM Siparis s ORDER BY s.toplamTutar DESC")
    public List<Siparis> findTopSiparisByTutar();

    @Query(value="SELECT s FROM Siparis s WHERE s.durum = 'Beklemede' ORDER BY s.siparisTarihi ASC")
    public List<Siparis> findBeklemedekiSiparisler();

    @Query(value="SELECT s FROM Siparis s WHERE s.durum = 'Onayland\u0131' ORDER BY s.siparisTarihi DESC")
    public List<Siparis> findOnaylanmisSiparisler();

    @Query(value="SELECT s FROM Siparis s WHERE s.durum = '\u0130ptal Edildi' ORDER BY s.siparisTarihi DESC")
    public List<Siparis> findIptalEdilmisSiparisler();

    @EntityGraph(attributePaths={"kullanici", "siparisDetaylari", "siparisDetaylari.kitap"})
    public Page<Siparis> findByDurum(String var1, Pageable var2);

    @Query(value="SELECT CAST(s.siparisTarihi AS date) as tarih, COUNT(s) as adet, SUM(s.toplamTutar) as toplam FROM Siparis s WHERE s.durum = 'Onayland\u0131' GROUP BY CAST(s.siparisTarihi AS date) ORDER BY CAST(s.siparisTarihi AS date) DESC")
    public List<Object[]> findGunlukSatisIstatistikleri();

    @Query(value="SELECT YEAR(s.siparisTarihi) as yil, MONTH(s.siparisTarihi) as ay, COUNT(s) as adet, SUM(s.toplamTutar) as toplam FROM Siparis s WHERE s.durum = 'Onayland\u0131' GROUP BY YEAR(s.siparisTarihi), MONTH(s.siparisTarihi) ORDER BY YEAR(s.siparisTarihi) DESC, MONTH(s.siparisTarihi) DESC")
    public List<Object[]> getMonthlySalesStats();

    @Query(value="SELECT s.kullanici.id, COUNT(s) as siparisAdet, SUM(s.toplamTutar) as toplamHarcama FROM Siparis s WHERE s.durum = 'Onayland\u0131' GROUP BY s.kullanici.id ORDER BY toplamHarcama DESC")
    public List<Object[]> getTopCustomers();

    @Query(value="SELECT s FROM Siparis s LEFT JOIN FETCH s.siparisDetaylari")
    public List<Siparis> findAllWithDetails();

    @Query(value="SELECT s FROM Siparis s LEFT JOIN FETCH s.siparisDetaylari WHERE s.id = :id")
    public Optional<Siparis> findByIdWithDetails(@Param(value="id") Long var1);

    @Query(value="SELECT s FROM Siparis s LEFT JOIN FETCH s.siparisDetaylari WHERE s.kullanici.id = :kullanici_Id")
    public List<Siparis> findByKullanici_IdWithDetails(@Param(value="kullanici_Id") Long var1);

    @Query(value="SELECT s FROM Siparis s ORDER BY s.id DESC")
    public List<Siparis> findTopByOrderByIdDesc(@Param(value="limit") int var1);
}

