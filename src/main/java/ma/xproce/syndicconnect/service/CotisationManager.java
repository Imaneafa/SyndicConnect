package ma.xproce.syndicconnect.service;

import ma.xproce.syndicconnect.dao.entities.Cotisation;
import ma.xproce.syndicconnect.dao.entities.Utilisateur;
import ma.xproce.syndicconnect.dao.repositories.CotisationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CotisationManager implements CotisationService {

    private final CotisationRepository cotisationRepository;

    public CotisationManager(CotisationRepository cotisationRepository) {
        this.cotisationRepository = cotisationRepository;
    }

    @Override
    public List<Cotisation> getAllCotisations() {
        return cotisationRepository.findAll();
    }

    @Override
    public List<Cotisation> getCotisationsByResident(Utilisateur resident) {
        return cotisationRepository.findByResident(resident);
    }

    @Override
    public Cotisation getCotisationById(Integer id) {
        return cotisationRepository.findById(id).orElse(null);
    }

    @Override
    public Cotisation saveCotisation(Cotisation cotisation) {
        return cotisationRepository.save(cotisation);
    }

    @Override
    public long countNonPayees() {
        return cotisationRepository.countByStatut("NON_PAYE");
    }
}

