package ma.xproce.syndicconnect.dao.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "commentaire")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commentaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String contenu;

    private LocalDateTime date;

    // Qui a écrit le commentaire
    @ManyToOne
    @JoinColumn(name = "auteur_id")
    @JsonBackReference
    private Utilisateur auteur;

    // A quel problème est lié ce commentaire
    @ManyToOne
    @JoinColumn(name = "probleme_id")
    @JsonBackReference
    private Probleme probleme;
}
