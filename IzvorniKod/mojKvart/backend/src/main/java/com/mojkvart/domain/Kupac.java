package com.mojkvart.domain;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Kupac implements UserDetails {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Integer kupacId;

    @Column(unique = true, nullable = false, length = 50)
    private String kupacEmail;

    @Column(nullable = false, length = 20)
    private String kupacIme;

    @Column(nullable = false, length = 20)
    private String kupacPrezime;

    @Column(nullable = false, length = 50)
    private String kupacAdresa;

    @Column(nullable = false, length = 200)
    private String kupacSifra;

    @OneToMany(mappedBy = "kupac")
    private Set<KupacDogadaj> kupacKupacDogadajs;

    @OneToMany(mappedBy = "kupac")
    private Set<Recenzija> kupacKupacRecenzijas;

    @OneToMany(mappedBy = "kupac")
    private Set<OcjenaProizvodKupac> kupacOcjenaProizvodKupacs;

    @OneToMany(mappedBy = "kupac")
    private Set<KupacPonudaPopust> kupacKupacPonudaPopusts;

    @OneToMany(mappedBy = "kupac")
    private Set<KupacProizvod> kupacKupacProizvods;

    @OneToMany(mappedBy = "kupac")
    private Set<Racun> kupacRacuns;

    @Column(length = 50)
    private String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return kupacEmail;
    }

    @Override
    public String getPassword() {
        return kupacSifra;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}