/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Kitap
 *  com.alperen.kitapsatissistemi.repository.KitapRepository
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.jpa.repository.EntityGraph
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.data.jpa.repository.Query
 *  org.springframework.data.repository.query.Param
 *  org.springframework.stereotype.Repository
 */
package com.alperen.kitapsatissistemi.repository;

import com.alperen.kitapsatissistemi.entity.Kitap;
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
public interface KitapRepository
extends JpaRepository<Kitap, Long> {
    public List<Kitap> findByAdContainingIgnoreCase(String var1);

    public List<Kitap> findByYazarContainingIgnoreCase(String var1);

    public List<Kitap> findByKategori_Id(Long var1);

    public List<Kitap> findByFiyatBetween(BigDecimal var1, BigDecimal var2);

    @Query(value="SELECT k FROM Kitap k WHERE (:ad IS NULL OR LOWER(k.ad) LIKE LOWER(CONCAT('%', :ad, '%'))) AND (:yazar IS NULL OR LOWER(k.yazar) LIKE LOWER(CONCAT('%', :yazar, '%')))")
    public List<Kitap> findByAdAndYazar(@Param(value="ad") String var1, @Param(value="yazar") String var2);

    public long countByKategori_Id(Long var1);

    @Query(value="SELECT k FROM Kitap k ORDER BY k.fiyat DESC")
    public List<Kitap> findTopByOrderByFiyatDesc();

    @Query(value="SELECT k FROM Kitap k ORDER BY k.fiyat ASC")
    public List<Kitap> findTopByOrderByFiyatAsc();

    public List<Kitap> findByFiyatGreaterThan(BigDecimal var1);

    public List<Kitap> findByFiyatLessThan(BigDecimal var1);

    @Query(value="SELECT k FROM Kitap k LEFT JOIN k.kategori WHERE k.id = :id")
    public Optional<Kitap> findByIdWithKategori(@Param(value="id") Long var1);

    @Query(value="SELECT k FROM Kitap k LEFT JOIN k.kategori")
    public List<Kitap> findAllWithKategori();

    @Query(value="SELECT k FROM Kitap k WHERE k.resimUrl IS NOT NULL AND k.resimUrl != ''")
    public List<Kitap> findKitaplarWithResim();

    @EntityGraph(attributePaths={"kategori"})
    public Page<Kitap> findAll(Pageable var1);

    @EntityGraph(attributePaths={"kategori"})
    public Page<Kitap> findByAdContainingIgnoreCase(String var1, Pageable var2);

    @EntityGraph(attributePaths={"kategori"})
    public Page<Kitap> findByKategori_Id(Long var1, Pageable var2);

    @EntityGraph(attributePaths={"kategori"})
    public Page<Kitap> findByAdContainingIgnoreCaseAndKategori_Id(String var1, Long var2, Pageable var3);

    public List<Kitap> findByKategori_IdAndIdNot(Long var1, Long var2);

    @Query(value="SELECT k FROM Kitap k ORDER BY k.id DESC")
    public List<Kitap> findTopByOrderByIdDesc(@Param(value="limit") int var1);
}

