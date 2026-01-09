package ma.xproce.syndicconnect.dao.repositories;

import ma.xproce.syndicconnect.dao.entities.Cotisation;
import ma.xproce.syndicconnect.dao.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CotisationRepository extends JpaRepository<Cotisation, Integer> {

    List<Cotisation> findByResident(Utilisateur resident);

    long countByStatut(String statut);
}
