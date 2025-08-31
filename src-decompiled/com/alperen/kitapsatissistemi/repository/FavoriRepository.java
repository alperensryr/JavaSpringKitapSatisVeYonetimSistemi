/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.entity.Favori
 *  com.alperen.kitapsatissistemi.repository.FavoriRepository
 *  org.springframework.data.domain.Page
 *  org.springframework.data.domain.Pageable
 *  org.springframework.data.jpa.repository.EntityGraph
 *  org.springframework.data.jpa.repository.JpaRepository
 *  org.springframework.data.jpa.repository.Query
 *  org.springframework.data.repository.query.Param
 *  org.springframework.stereotype.Repository
 */
package com.alperen.kitapsatissistemi.repository;

import com.alperen.kitapsatissistemi.entity.Favori;
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
public interface FavoriRepository
extends JpaRepository<Favori, Long> {
    public List<Favori> findByKullanici_Id(Long var1);

    @EntityGraph(attributePaths={"kitap", "kitap.kategori"})
    public Page<Favori> findByKullanici_Id(Long var1, Pageable var2);

    public List<Favori> findByKitap_Id(Long var1);

    @EntityGraph(attributePaths={"kullanici", "kitap", "kitap.kategori"})
    public Page<Favori> findByKitap_Id(Long var1, Pageable var2);

    public Optional<Favori> findByKullanici_IdAndKitap_Id(Long var1, Long var2);

    public boolean existsByKullanici_IdAndKitap_Id(Long var1, Long var2);

    public long countByKullanici_Id(Long var1);

    public long countByKitap_Id(Long var1);

    @Query(value="SELECT f FROM Favori f LEFT JOIN FETCH f.kitap k LEFT JOIN FETCH k.kategori WHERE f.kullanici.id = :kullanici_Id")
    public List<Favori> findByKullanici_IdWithKitap(@Param(value="kullanici_Id") Long var1);

    @Query(value="SELECT f FROM Favori f LEFT JOIN f.kullanici WHERE f.kitap.id = :kitap_Id")
    public List<Favori> findByKitap_IdWithKullanici(@Param(value="kitap_Id") Long var1);

    @Query(value="SELECT f FROM Favori f LEFT JOIN f.kitap LEFT JOIN f.kullanici")
    public List<Favori> findAllWithKitapAndKullanici();

    @Query(value="SELECT f.kitap.id, COUNT(f) as favoriSayisi FROM Favori f GROUP BY f.kitap.id ORDER BY favoriSayisi DESC")
    public List<Object[]> findMostFavoriteKitaplar();

    @Query(value="SELECT f.kitap.id FROM Favori f WHERE f.kullanici.id = :kullanici_Id")
    public List<Long> findKitapIdsByKullaniciId(@Param(value="kullanici_Id") Long var1);

    public void deleteByKullanici_Id(Long var1);

    public void deleteByKitap_Id(Long var1);

    @Query(value="SELECT f FROM Favori f LEFT JOIN FETCH f.kullanici LEFT JOIN FETCH f.kitap")
    public List<Favori> findAllWithKullaniciAndKitap();

    @Query(value="SELECT f.kitap.id, COUNT(f) as favoriSayisi FROM Favori f GROUP BY f.kitap.id ORDER BY favoriSayisi DESC")
    public List<Object[]> findMostFavoritedBooks();

    @Query(value="SELECT f.kullanici.id, COUNT(f) as favoriSayisi FROM Favori f GROUP BY f.kullanici.id ORDER BY favoriSayisi DESC")
    public List<Object[]> findMostActiveUsers();
}

