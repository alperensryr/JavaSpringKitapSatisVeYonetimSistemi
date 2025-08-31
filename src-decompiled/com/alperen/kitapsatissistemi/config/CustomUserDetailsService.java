/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.config.CustomUserDetailsService
 *  com.alperen.kitapsatissistemi.entity.Kullanici
 *  com.alperen.kitapsatissistemi.service.KullaniciService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.context.annotation.Lazy
 *  org.springframework.context.annotation.Primary
 *  org.springframework.security.core.authority.SimpleGrantedAuthority
 *  org.springframework.security.core.userdetails.User
 *  org.springframework.security.core.userdetails.UserDetails
 *  org.springframework.security.core.userdetails.UserDetailsService
 *  org.springframework.security.core.userdetails.UsernameNotFoundException
 *  org.springframework.stereotype.Service
 */
package com.alperen.kitapsatissistemi.config;

import com.alperen.kitapsatissistemi.entity.Kullanici;
import com.alperen.kitapsatissistemi.service.KullaniciService;
import java.util.Collections;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class CustomUserDetailsService
implements UserDetailsService {
    @Autowired
    @Lazy
    private KullaniciService kullaniciService;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional kullaniciOpt = this.kullaniciService.getKullaniciByEmail(email);
        if (kullaniciOpt.isPresent()) {
            Kullanici kullanici = (Kullanici)kullaniciOpt.get();
            String originalRole = kullanici.getRol() != null ? kullanici.getRol() : "User";
            String role = "Admin".equalsIgnoreCase(originalRole) ? "ADMIN" : "USER";
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
            return User.builder().username(kullanici.getEmail()).password(kullanici.getSifreHash()).authorities(Collections.singletonList(authority)).accountExpired(false).accountLocked(false).credentialsExpired(false).disabled(false).build();
        }
        System.out.println("CustomUserDetailsService - User not found: " + email);
        throw new UsernameNotFoundException("Kullan\u0131c\u0131 bulunamad\u0131: " + email);
    }
}

