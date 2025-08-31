/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Adres
 *  com.alperen.kitapsatissistemi.repository.AdresRepository
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.data.jpa.repository.Modifying
 *  org.springframework.data.jpa.repository.Query
 *  org.springframework.data.repository.query.Param
 *  org.springframework.stereotype.Repository
 */
package com.alperen.kitapsatissistemi.repository;

import com.alperen.kitapsatissistemi.entity.Adres;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdresRepository
extends JpaRepository<Adres, Long> {
    public List<Adres> findByKullanici_Id(Long var1);

    public Optional<Adres> findByKullanici_IdAndVarsayilanTrue(Long var1);

    public Optional<Adres> findByKullanici_IdAndBaslik(Long var1, String var2);

    public List<Adres> findBySehir(String var1);

    public List<Adres> findByIlce(String var1);

    public long countByKullanici_Id(Long var1);

    @Modifying
    @Query(value="UPDATE Adres a SET a.varsayilan = false WHERE a.kullanici.id = :kullaniciId")
    public void resetVarsayilanAdres(@Param(value="kullaniciId") Long var1);

    public List<Adres> findByKullanici_IdOrderByVarsayilanDescBaslikAsc(Long var1);
}

