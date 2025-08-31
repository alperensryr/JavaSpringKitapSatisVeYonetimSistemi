/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Kategori
 *  com.alperen.kitapsatissistemi.repository.KategoriRepository
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.data.jpa.repository.Query
 *  org.springframework.data.repository.query.Param
 *  org.springframework.stereotype.Repository
 */
package com.alperen.kitapsatissistemi.repository;

import com.alperen.kitapsatissistemi.entity.Kategori;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface KategoriRepository
extends JpaRepository<Kategori, Long> {
    public Optional<Kategori> findByAd(String var1);

    public List<Kategori> findByAdContainingIgnoreCase(String var1);

    @Query(value="SELECT k FROM Kategori k WHERE k.aciklama IS NOT NULL AND k.aciklama != ''")
    public List<Kategori> findKategorilerWithAciklama();

    @Query(value="SELECT COUNT(k) > 0 FROM Kategori k WHERE k.ad = :ad AND k.id != :id")
    public boolean existsByAdAndIdNot(@Param(value="ad") String var1, @Param(value="id") Long var2);

    public boolean existsByAd(String var1);

    public Page<Kategori> findByAdContainingIgnoreCase(String var1, Pageable var2);
}

