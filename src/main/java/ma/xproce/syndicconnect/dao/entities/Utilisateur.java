package ma.xproce.syndicconnect.dao.entities;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "utilisateur")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nom;

    @Column(unique = true, nullable = false)
    private String email;

    private String motDePasse;

    // RESIDENT / SYNDIC / ADMIN
    private String role;

    @Transient
    private long nbCotisationsNonPayees;

    // Numéro d'appartement (uniquement pour RESIDENT)
    @Column(nullable = true)
    private String numeroAppartement;

    // Un utilisateur peut signaler plusieurs problèmes
    @OneToMany(mappedBy = "auteur", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Probleme> problemes;

    // Un utilisateur peut écrire plusieurs commentaires
    @OneToMany(mappedBy = "auteur", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Commentaire> commentaires;

    // Un utilisateur (syndic) peut publier plusieurs annonces
    @OneToMany(mappedBy = "auteur", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Annonce> annonces;

    // ======= SPRING SECURITY =======

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return motDePasse;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public long getNbCotisationsNonPayees() {
        return nbCotisationsNonPayees;
    }

    public void setNbCotisationsNonPayees(long nb) {
        this.nbCotisationsNonPayees = nb;
    }
}

