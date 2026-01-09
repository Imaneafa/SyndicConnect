package ma.xproce.syndicconnect.dao.repositories;
import ma.xproce.syndicconnect.dao.entities.Probleme;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProblemeRepository extends JpaRepository<Probleme,Integer> {

    long countByStatut(String statut);
}