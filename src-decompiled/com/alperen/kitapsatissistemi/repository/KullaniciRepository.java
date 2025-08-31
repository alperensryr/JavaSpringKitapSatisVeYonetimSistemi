/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Kullanici
 *  com.alperen.kitapsatissistemi.repository.KullaniciRepository
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.data.jpa.repository.Query
 *  org.springframework.data.repository.query.Param
 *  org.springframework.stereotype.Repository
 */
package com.alperen.kitapsatissistemi.repository;

import com.alperen.kitapsatissistemi.entity.Kullanici;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface KullaniciRepository
extends JpaRepository<Kullanici, Long> {
    public Optional<Kullanici> findByEmail(String var1);

    public boolean existsByEmail(String var1);

    @Query(value="SELECT COUNT(k) > 0 FROM Kullanici k WHERE k.email = :email AND k.id != :id")
    public boolean existsByEmailAndIdNot(@Param(value="email") String var1, @Param(value="id") Long var2);

    public List<Kullanici> findByRol(String var1);

    @Query(value="SELECT k FROM Kullanici k WHERE k.rol = 'Admin'")
    public List<Kullanici> findAdminKullanicilar();

    @Query(value="SELECT k FROM Kullanici k WHERE k.rol = 'User'")
    public List<Kullanici> findNormalKullanicilar();

    public List<Kullanici> findByAdSoyadContainingIgnoreCase(String var1);

    public List<Kullanici> findByKayitTarihiAfter(LocalDateTime var1);

    public List<Kullanici> findByKayitTarihiBefore(LocalDateTime var1);

    public List<Kullanici> findByKayitTarihiBetween(LocalDateTime var1, LocalDateTime var2);

    public long countByRol(String var1);

    @Query(value="SELECT k FROM Kullanici k ORDER BY k.kayitTarihi DESC")
    public List<Kullanici> findLatestKullanicilar();

    @Query(value="SELECT k FROM Kullanici k ORDER BY k.kayitTarihi DESC")
    public List<Kullanici> findTopByOrderByKayitTarihiDesc(int var1);

    public Optional<Kullanici> findByEmailAndRol(String var1, String var2);

    public Page<Kullanici> findByAdSoyadContainingIgnoreCase(String var1, Pageable var2);

    public Page<Kullanici> findByAdSoyadContainingIgnoreCaseOrEmailContainingIgnoreCase(String var1, String var2, Pageable var3);
}

