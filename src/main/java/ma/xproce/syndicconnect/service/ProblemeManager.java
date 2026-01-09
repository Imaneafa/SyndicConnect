package ma.xproce.syndicconnect.service;

import ma.xproce.syndicconnect.dao.entities.Probleme;
import ma.xproce.syndicconnect.dao.repositories.ProblemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProblemeManager implements ProblemeService {
    @Autowired
    private ProblemeRepository problemeRepository;
    @Override
    public List<Probleme> getAllProblemes() {
        return problemeRepository.findAll();
    }

    @Override
    public Probleme getProblemeById(Integer id) {
        return problemeRepository.findById(id).orElse(null);
    }

    @Override
    public Probleme saveProbleme(Probleme probleme) {
        if (probleme.getId() != null) {
            Probleme existing = problemeRepository.findById(probleme.getId()).orElse(null);
            if (existing != null && !existing.getStatut().equals(probleme.getStatut())) {
                // Vérifie le rôle du créateur
                if (probleme.getAuteur().getRole().equals("RESIDENT")) {
                    throw new RuntimeException("Seul le syndic peut modifier l'état d'un problème !");
                }
            }
        }
        return problemeRepository.save(probleme);
    }

    @Override
    public void deleteProbleme(Integer id) {
        problemeRepository.deleteById(id);
    }

    @Override
    public List<Probleme> getProblemesByAuteur(Integer auteurId) {
        return problemeRepository.findAll()
                .stream()
                .filter(p -> p.getAuteur() != null && p.getAuteur().getId().equals(auteurId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Probleme> getProblemesByStatut(String statut) {
        return problemeRepository.findAll()
                .stream()
                .filter(p -> p.getStatut().equalsIgnoreCase(statut))
                .collect(Collectors.toList());
    }
    @Override
    public long countByStatut(String statut) {
        return problemeRepository.countByStatut(statut);
    }
}
