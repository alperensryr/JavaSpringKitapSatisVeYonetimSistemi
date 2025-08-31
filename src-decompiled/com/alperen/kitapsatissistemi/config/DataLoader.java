/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.config.DataLoader
 *  com.alperen.kitapsatissistemi.entity.Kategori
 *  com.alperen.kitapsatissistemi.entity.Kitap
 *  com.alperen.kitapsatissistemi.entity.Kullanici
 *  com.alperen.kitapsatissistemi.repository.KategoriRepository
 *  com.alperen.kitapsatissistemi.repository.KitapRepository
 *  com.alperen.kitapsatissistemi.repository.KullaniciRepository
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.boot.CommandLineRunner
 *  org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
 *  org.springframework.stereotype.Component
 */
package com.alperen.kitapsatissistemi.config;

import com.alperen.kitapsatissistemi.entity.Kategori;
import com.alperen.kitapsatissistemi.entity.Kitap;
import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.repository.KategoriRepository;
import com.alperen.kitapsatissistemi.repository.KitapRepository;
import com.alperen.kitapsatissistemi.repository.KullaniciRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader
implements CommandLineRunner {
    private final KategoriRepository kategoriRepository;
    private final KitapRepository kitapRepository;
    private final KullaniciRepository kullaniciRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(KategoriRepository kategoriRepository, KitapRepository kitapRepository, KullaniciRepository kullaniciRepository) {
        this.kategoriRepository = kategoriRepository;
        this.kitapRepository = kitapRepository;
        this.kullaniciRepository = kullaniciRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public void run(String ... args) throws Exception {
        System.out.println("DataLoader run metodu \u00e7a\u011fr\u0131ld\u0131");
        this.loadData();
    }

    private void loadData() {
        this.createSampleUsers();
        if (this.kategoriRepository.count() > 0L) {
            return;
        }
        List<Kategori> kategoriler = Arrays.asList(new Kategori("Roman", "Roman kategorisi"), new Kategori("Bilim Kurgu", "Bilim kurgu kitaplar\u0131"), new Kategori("Tarih", "Tarih kitaplar\u0131"), new Kategori("Felsefe", "Felsefe kitaplar\u0131"), new Kategori("\u00c7ocuk", "\u00c7ocuk kitaplar\u0131"), new Kategori("Edebiyat", "Edebiyat kitaplar\u0131"), new Kategori("Bilim", "Bilim kitaplar\u0131"), new Kategori("Sanat", "Sanat kitaplar\u0131"));
        List savedKategoriler = this.kategoriRepository.saveAll(kategoriler);
        List<Kitap> kitaplar = Arrays.asList(new Kitap("Su\u00e7 ve Ceza", "Fyodor Dostoyevski", new BigDecimal("25.50"), "Klasik Rus edebiyat\u0131n\u0131n ba\u015fyap\u0131t\u0131", (Kategori)savedKategoriler.get(0), "/images/books/suc-ve-ceza.jpg"), new Kitap("Bilin\u00e7alt\u0131n\u0131n G\u00fcc\u00fc", "Joseph Murphy", new BigDecimal("30.00"), "Ki\u015fisel geli\u015fim kitab\u0131", (Kategori)savedKategoriler.get(3), "/images/books/bilincaltinin-gucu.jpg"), new Kitap("Bilinmeyen Bir Kad\u0131n\u0131n Mektubu", "Stefan Zweig", new BigDecimal("15.75"), "Duygusal bir a\u015fk hikayesi", (Kategori)savedKategoriler.get(0), "/images/books/bilinmeyen-kadin-mektubu.jpg"), new Kitap("1984", "George Orwell", new BigDecimal("22.00"), "Distopik roman", (Kategori)savedKategoriler.get(1), "/images/books/1984.jpg"), new Kitap("Hayvan \u00c7iftli\u011fi", "George Orwell", new BigDecimal("18.50"), "Alegorik roman", (Kategori)savedKategoriler.get(0), "/images/books/hayvan-ciftligi.jpg"), new Kitap("Dune", "Frank Herbert", new BigDecimal("35.00"), "Bilim kurgu klasi\u011fi", (Kategori)savedKategoriler.get(1), "/images/books/dune.jpg"), new Kitap("Sapiens", "Yuval Noah Harari", new BigDecimal("28.75"), "\u0130nsanl\u0131k tarihi", (Kategori)savedKategoriler.get(2), "/images/books/sapiens.jpg"), new Kitap("Homo Deus", "Yuval Noah Harari", new BigDecimal("32.00"), "Gelece\u011fin tarihi", (Kategori)savedKategoriler.get(2), "/images/books/homo-deus.jpg"), new Kitap("K\u00fc\u00e7\u00fck Prens", "Antoine de Saint-Exup\u00e9ry", new BigDecimal("12.50"), "\u00c7ocuklar i\u00e7in klasik", (Kategori)savedKategoriler.get(4), "/images/books/kucuk-prens.jpg"), new Kitap("Simyac\u0131", "Paulo Coelho", new BigDecimal("20.00"), "Felsefi roman", (Kategori)savedKategoriler.get(3), "/images/books/simyaci.jpg"), new Kitap("K\u00fcrk Mantolu Madonna", "Sabahattin Ali", new BigDecimal("16.25"), "T\u00fcrk edebiyat\u0131 klasi\u011fi", (Kategori)savedKategoriler.get(5), "/images/books/kurk-mantolu-madonna.jpg"), new Kitap("Harry Potter ve Felsefe Ta\u015f\u0131", "J.K. Rowling", new BigDecimal("24.00"), "Fantastik roman", (Kategori)savedKategoriler.get(4), "/images/books/harry-potter-felsefe-tasi.jpg"), new Kitap("Y\u00fcz\u00fcklerin Efendisi", "J.R.R. Tolkien", new BigDecimal("45.00"), "Fantastik epik", (Kategori)savedKategoriler.get(1), "/images/books/yuzuklerin-efendisi.jpg"), new Kitap("Kuyucakl\u0131 Yusuf", "Sabahattin Ali", new BigDecimal("23.25"), "Anadolu'nun hikayesi", (Kategori)savedKategoriler.get(0), "/images/books/kuyucakli-yusuf.jpg"), new Kitap("Masumiyet M\u00fczesi", "Orhan Pamuk", new BigDecimal("29.00"), "Nobel \u00f6d\u00fcll\u00fc yazar\u0131n a\u015fk roman\u0131", (Kategori)savedKategoriler.get(0), "/images/books/masumiyet-muzesi.jpg"), new Kitap("Fahrenheit 451", "Ray Bradbury", new BigDecimal("20.75"), "Distopik bilim kurgu", (Kategori)savedKategoriler.get(1), "/images/books/fahrenheit-451.jpg"), new Kitap("Brave New World", "Aldous Huxley", new BigDecimal("22.50"), "Distopik roman", (Kategori)savedKategoriler.get(1), "/images/books/brave-new-world.jpg"), new Kitap("Zaman\u0131n K\u0131sa Tarihi", "Stephen Hawking", new BigDecimal("31.00"), "Fizik ve kozmoloji", (Kategori)savedKategoriler.get(6), "/images/books/zamanin-kisa-tarihi.jpg"), new Kitap("Sanat Tarihi", "Ernst Gombrich", new BigDecimal("42.00"), "Sanat tarihi rehberi", (Kategori)savedKategoriler.get(7), "/images/books/sanat-tarihi.jpg"), new Kitap("Meditations", "Marcus Aurelius", new BigDecimal("18.00"), "Stoik felsefe", (Kategori)savedKategoriler.get(3), "/images/books/meditations.jpg"), new Kitap("Nicomachean Ethics", "Aristotle", new BigDecimal("25.00"), "Etik felsefesi", (Kategori)savedKategoriler.get(3), "/images/books/nicomachean-ethics.jpg"), new Kitap("The Republic", "Plato", new BigDecimal("27.50"), "Politik felsefe", (Kategori)savedKategoriler.get(3), "/images/books/the-republic.jpg"), new Kitap("\u0130nsan Haklar\u0131 Evrensel Beyannamesi", "Birle\u015fmi\u015f Milletler", new BigDecimal("8.00"), "\u0130nsan haklar\u0131 metni", (Kategori)savedKategoriler.get(2), "/images/books/insan-haklari-beyannamesi.jpg"), new Kitap("Osmanl\u0131 Tarihi", "\u0130lber Ortayl\u0131", new BigDecimal("35.50"), "Osmanl\u0131 \u0130mparatorlu\u011fu tarihi", (Kategori)savedKategoriler.get(2), "/images/books/osmanli-tarihi.jpg"), new Kitap("Nutuk", "Mustafa Kemal Atat\u00fcrk", new BigDecimal("15.00"), "Kurtulu\u015f Sava\u015f\u0131 an\u0131lar\u0131", (Kategori)savedKategoriler.get(2), "/images/books/nutuk.jpg"), new Kitap("Alice Harikalar Diyar\u0131nda", "Lewis Carroll", new BigDecimal("13.75"), "\u00c7ocuk klasi\u011fi", (Kategori)savedKategoriler.get(4), "/images/books/alice-harikalar-diyarinda.jpg"), new Kitap("Pinokyo", "Carlo Collodi", new BigDecimal("11.50"), "\u00c7ocuk masal\u0131", (Kategori)savedKategoriler.get(4), "/images/books/pinokyo.jpg"));
        this.kitapRepository.saveAll(kitaplar);
        System.out.println("\u00d6rnek veriler ba\u015far\u0131yla y\u00fcklendi: " + kategoriler.size() + " kategori, " + kitaplar.size() + " kitap");
    }

    private void createSampleUsers() {
        System.out.println("createSampleUsers metodu \u00e7a\u011fr\u0131ld\u0131");
        try {
            boolean adminExists = this.kullaniciRepository.existsByEmail("admin@kitap.com");
            System.out.println("Admin kullan\u0131c\u0131s\u0131 var m\u0131: " + adminExists);
            if (!adminExists) {
                Kullanici admin = new Kullanici();
                admin.setAdSoyad("Admin Kullan\u0131c\u0131");
                admin.setEmail("admin@kitap.com");
                String adminPassword = System.getenv("ADMIN_PASSWORD");
                if (adminPassword == null || adminPassword.trim().isEmpty()) {
                    adminPassword = "Admin123!";
                }
                admin.setSifreHash(this.passwordEncoder.encode((CharSequence)adminPassword));
                admin.setRol("Admin");
                admin.setKayitTarihi(LocalDateTime.now());
                this.kullaniciRepository.save((Object)admin);
                System.out.println("Admin kullan\u0131c\u0131s\u0131 olu\u015fturuldu: admin@kitap.com / Admin123!");
            } else {
                System.out.println("Admin kullan\u0131c\u0131s\u0131 zaten mevcut: admin@kitap.com");
            }
        }
        catch (Exception e) {
            System.err.println("Admin kullan\u0131c\u0131s\u0131 olu\u015fturulurken hata: " + e.getMessage());
        }
        try {
            boolean testUserExists = this.kullaniciRepository.existsByEmail("test@kitap.com");
            if (!testUserExists) {
                Kullanici testUser = new Kullanici();
                testUser.setAdSoyad("Test Kullan\u0131c\u0131");
                testUser.setEmail("test@kitap.com");
                testUser.setSifreHash(this.passwordEncoder.encode((CharSequence)"test123"));
                testUser.setRol("User");
                testUser.setKayitTarihi(LocalDateTime.now());
                this.kullaniciRepository.save((Object)testUser);
                System.out.println("Test kullan\u0131c\u0131s\u0131 olu\u015fturuldu: test@kitap.com / test123");
            } else {
                System.out.println("Test kullan\u0131c\u0131s\u0131 zaten mevcut: test@kitap.com");
            }
        }
        catch (Exception e) {
            System.err.println("Test kullan\u0131c\u0131s\u0131 olu\u015fturulurken hata: " + e.getMessage());
        }
        try {
            boolean demoUserExists = this.kullaniciRepository.existsByEmail("demo@kitap.com");
            if (!demoUserExists) {
                Kullanici demoUser = new Kullanici();
                demoUser.setAdSoyad("Demo Kullan\u0131c\u0131");
                demoUser.setEmail("demo@kitap.com");
                demoUser.setSifreHash(this.passwordEncoder.encode((CharSequence)"demo123"));
                demoUser.setRol("User");
                demoUser.setKayitTarihi(LocalDateTime.now());
                this.kullaniciRepository.save((Object)demoUser);
                System.out.println("Demo kullan\u0131c\u0131s\u0131 olu\u015fturuldu: demo@kitap.com / demo123");
            } else {
                System.out.println("Demo kullan\u0131c\u0131s\u0131 zaten mevcut: demo@kitap.com");
            }
        }
        catch (Exception e) {
            System.err.println("Demo kullan\u0131c\u0131s\u0131 olu\u015fturulurken hata: " + e.getMessage());
        }
        try {
            boolean extraAdminExists = this.kullaniciRepository.existsByEmail("a@a.com");
            if (!extraAdminExists) {
                Kullanici extraAdmin = new Kullanici();
                extraAdmin.setAdSoyad("Ek Admin");
                extraAdmin.setEmail("a@a.com");
                extraAdmin.setSifreHash(this.passwordEncoder.encode((CharSequence)"aaaaaa"));
                extraAdmin.setRol("Admin");
                extraAdmin.setKayitTarihi(LocalDateTime.now());
                this.kullaniciRepository.save((Object)extraAdmin);
                System.out.println("Ek admin kullan\u0131c\u0131s\u0131 olu\u015fturuldu: a@a.com / aaaaaa");
            } else {
                System.out.println("Ek admin kullan\u0131c\u0131s\u0131 zaten mevcut: a@a.com");
            }
        }
        catch (Exception e) {
            System.err.println("Ek admin kullan\u0131c\u0131s\u0131 olu\u015fturulurken hata: " + e.getMessage());
        }
    }
}

