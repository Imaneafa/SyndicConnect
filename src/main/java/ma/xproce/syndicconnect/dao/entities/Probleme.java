package ma.xproce.syndicconnect.dao.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;

@Entity
@Table(name = "probleme")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Probleme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    // "EN_ATTENTE", "EN_COURS", "RESOLU"
    private String statut;

    private LocalDateTime dateCreation;

    // Auteur du signalement
    @ManyToOne
    @JoinColumn(name = "auteur_id")
    @JsonBackReference
    private Utilisateur auteur;

    // Commentaires sur le problème
    @OneToMany(mappedBy = "probleme", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Commentaire> commentaires;
}
