/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.alperen.kitapsatissistemi.config.RateLimitingFilter
 *  com.alperen.kitapsatissistemi.config.SecurityConfig
 *  javax.servlet.Filter
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 *  org.springframework.security.authentication.AuthenticationProvider
 *  org.springframework.security.authentication.dao.DaoAuthenticationProvider
 *  org.springframework.security.config.annotation.web.builders.HttpSecurity
 *  org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
 *  org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer$AuthorizedUrl
 *  org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
 *  org.springframework.security.config.annotation.web.configurers.HeadersConfigurer$HstsConfig
 *  org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer$ConcurrencyControlConfigurer
 *  org.springframework.security.core.userdetails.UserDetailsService
 *  org.springframework.security.crypto.password.PasswordEncoder
 *  org.springframework.security.web.SecurityFilterChain
 *  org.springframework.security.web.authentication.AuthenticationSuccessHandler
 *  org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
 *  org.springframework.web.cors.CorsConfiguration
 *  org.springframework.web.cors.CorsConfigurationSource
 *  org.springframework.web.cors.UrlBasedCorsConfigurationSource
 */
package com.alperen.kitapsatissistemi.config;

import com.alperen.kitapsatissistemi.config.RateLimitingFilter;
import java.util.Arrays;
import javax.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private RateLimitingFilter rateLimitingFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) throws Exception {
        ((HttpSecurity)((HttpSecurity)((HttpSecurity)((HttpSecurity)((HttpSecurity)((FormLoginConfigurer)((FormLoginConfigurer)((FormLoginConfigurer)((FormLoginConfigurer)((HttpSecurity)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)((HttpSecurity)((HttpSecurity)http.cors().and()).csrf().ignoringAntMatchers(new String[]{"/api/**", "/h2-console/**"}).and()).authorizeRequests().antMatchers(new String[]{"/", "/home", "/index"})).permitAll().antMatchers(new String[]{"/kitaplar", "/kitaplar/**"})).permitAll().antMatchers(new String[]{"/kategoriler", "/kategoriler/**"})).permitAll().antMatchers(new String[]{"/kategori-listesi"})).permitAll().antMatchers(new String[]{"/about", "/contact", "/privacy"})).permitAll().antMatchers(new String[]{"/kullanici/login", "/kullanici/register"})).permitAll().antMatchers(new String[]{"/css/**", "/js/**", "/images/**", "/fonts/**", "/lib/**", "/lib/fontawesome/webfonts/**"})).permitAll().antMatchers(new String[]{"/favicon.ico"})).permitAll().antMatchers(new String[]{"/api/kullanicilar/register", "/api/kullanicilar/login"})).permitAll().antMatchers(new String[]{"/api/kategoriler", "/api/kategoriler/*/kitaplar"})).permitAll().antMatchers(new String[]{"/api/kitaplar", "/api/kitaplar/*"})).permitAll().antMatchers(new String[]{"/h2-console/**"})).permitAll().antMatchers(new String[]{"/swagger-ui/**"})).permitAll().antMatchers(new String[]{"/v3/api-docs/**"})).permitAll().antMatchers(new String[]{"/swagger-resources/**"})).permitAll().antMatchers(new String[]{"/webjars/**"})).permitAll().antMatchers(new String[]{"/admin/login"})).permitAll().antMatchers(new String[]{"/admin/**"})).hasRole("ADMIN").antMatchers(new String[]{"/kullanici/profil", "/kullanici/logout", "/kullanici/dashboard"})).authenticated().antMatchers(new String[]{"/sepet/count", "/sepet/ekle"})).permitAll().antMatchers(new String[]{"/sepet/**", "/favoriler/**", "/siparisler/**"})).authenticated().antMatchers(new String[]{"/api/favoriler/**", "/api/siparisler/**", "/api/siparis-detaylar/**"})).authenticated().antMatchers(new String[]{"/api/kullanicilar/**"})).authenticated().anyRequest()).authenticated().and()).authenticationProvider((AuthenticationProvider)this.authenticationProvider(userDetailsService, passwordEncoder)).formLogin().loginPage("/kullanici/login").loginProcessingUrl("/login")).usernameParameter("username").passwordParameter("password").successHandler(this.authenticationSuccessHandler())).failureHandler((request, response, exception) -> {
            String referer = request.getHeader("Referer");
            if (referer != null && referer.contains("/admin/login")) {
                response.sendRedirect("/admin/login?error=true");
            } else {
                response.sendRedirect("/kullanici/login?error=true");
            }
        })).permitAll()).and()).logout().logoutUrl("/kullanici/logout").logoutSuccessUrl("/").permitAll().and()).sessionManagement().sessionConcurrency(sessionConcurrency -> {
            SessionManagementConfigurer.ConcurrencyControlConfigurer concurrencyControlConfigurer = sessionConcurrency.maximumSessions(1).maxSessionsPreventsLogin(false);
        }).and()).headers().frameOptions().deny().contentTypeOptions().and().httpStrictTransportSecurity(hstsConfig -> {
            HeadersConfigurer.HstsConfig hstsConfig2 = hstsConfig.maxAgeInSeconds(31536000L).includeSubDomains(true);
        }).and()).headers().frameOptions().sameOrigin().and()).addFilterBefore((Filter)this.rateLimitingFilter, UsernamePasswordAuthenticationFilter.class);
        return (SecurityFilterChain)http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8086", "https://localhost:8086", "http://127.0.0.1:8086", "https://127.0.0.1:8086"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "X-Requested-With", "X-CSRF-TOKEN"));
        configuration.setAllowCredentials(Boolean.valueOf(true));
        configuration.setMaxAge(Long.valueOf(3600L));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}

