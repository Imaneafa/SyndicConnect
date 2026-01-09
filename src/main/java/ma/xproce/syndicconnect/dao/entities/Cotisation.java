package ma.xproce.syndicconnect.dao.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cotisation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cotisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // ex: "Janvier 2026"
    private String mois;

    private Double montant;

    // PAYE / NON_PAYE
    private String statut;

    @ManyToOne
    @JoinColumn(name = "resident_id")
    private Utilisateur resident;
}
