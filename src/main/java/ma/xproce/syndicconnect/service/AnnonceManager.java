package ma.xproce.syndicconnect.service;

import ma.xproce.syndicconnect.dao.entities.Annonce;
import ma.xproce.syndicconnect.dao.repositories.AnnonceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnnonceManager implements AnnonceService {

    @Autowired
    private AnnonceRepository annonceRepository;

    @Override
    public List<Annonce> getAllAnnonces() {
        return annonceRepository.findAll();
    }

    @Override
    public Annonce getAnnonceById(Integer id) {
        return annonceRepository.findById(id).orElse(null);
    }

    @Override
    public Annonce saveAnnonce(Annonce annonce) {
        // Vérifie si l’auteur est bien un syndic
        if (annonce.getAuteur() != null && !"SYNDIC".equalsIgnoreCase(annonce.getAuteur().getRole())) {
            throw new RuntimeException("Seul le syndic peut publier une annonce !");
        }
        return annonceRepository.save(annonce);
    }

    @Override
    public void deleteAnnonce(Integer id) {
        annonceRepository.deleteById(id);
    }

    @Override
    public List<Annonce> getAnnoncesByAuteur(Integer auteurId) {
        return annonceRepository.findAll()
                .stream()
                .filter(a -> a.getAuteur() != null && a.getAuteur().getId().equals(auteurId))
                .collect(Collectors.toList());
    }
}
