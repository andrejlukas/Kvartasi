package com.mojkvart.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Getter
@Setter
public class Trgovina implements UserDetails {

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
    private Integer trgovinaId;

    @Column(nullable = false, length = 100)
    private String trgovinaEmail;

    @Column(nullable = false, length = 100)
    private String trgovinaNaziv;

    @Column(nullable = false, length = 500)
    private String trgovinaOpis;

    @Column(nullable = false, length = 500)
    private String trgovinaKategorija;

    @Column(nullable = false, length = 50)
    private String trgovinaLokacija;

    @Column(nullable = false, length = 200)
    private String trgovinaSlika;

    @Column(nullable = false, length = 5)
    private String trgovinaRadnoVrijemeOd;   //upisuje se u formatu "xx:yy"

    @Column(nullable = false, length = 5)
    private String trgovinaRadnoVrijemeDo;   //upisuje se u formatu "xx:yy"
    
    @Column(nullable = false, length = 200)
    private String trgovinaSifra;

    @OneToMany(mappedBy = "trgovina")
    private Set<Proizvod> trgovinaProizvods;

    @OneToMany(mappedBy = "trgovina")
    private Set<Dogadaj> trgovinaDogadajs;

    @OneToMany(mappedBy = "trgovina")
    private Set<PonudaPopust> trgovinaPonudaPopusts;

    @OneToMany(mappedBy = "trgovina")
    private Set<Recenzija> trgovinaTrgovinaRecenzijas;

    @OneToMany(mappedBy = "trgovina")
    private Set<Racun> trgovinaRacuns;

    @ManyToMany
    @JoinTable(
            name = "ImaAtribute",
            joinColumns = @JoinColumn(name = "trgovinaId"),
            inverseJoinColumns = @JoinColumn(name = "atributId")
    )
    private Set<Atribut> imaAtributeAtributs;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("TRGOVINA"));
    }

    @Override
    public String getPassword() {
        return trgovinaSifra;
    }

    @Override
    public String getUsername() {
        return trgovinaEmail;
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
