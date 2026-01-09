package ma.xproce.syndicconnect.dao.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "annonce")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Annonce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titre;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    @Column(name = "date_publication")
    private LocalDateTime datePublication;

    // L'auteur (doit être un syndic normalement)
    @ManyToOne
    @JoinColumn(name = "auteur_id")
    @JsonBackReference
    private Utilisateur auteur;
}
