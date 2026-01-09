package ma.xproce.syndicconnect.service;

import ma.xproce.syndicconnect.dao.entities.Cotisation;
import ma.xproce.syndicconnect.dao.entities.Utilisateur;

import java.util.List;

public interface CotisationService {

    List<Cotisation> getAllCotisations();

    List<Cotisation> getCotisationsByResident(Utilisateur resident);

    Cotisation getCotisationById(Integer id);

    Cotisation saveCotisation(Cotisation cotisation);

    long countNonPayees();
}

