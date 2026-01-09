package ma.xproce.syndicconnect.service;

import ma.xproce.syndicconnect.dao.entities.Utilisateur;
import java.util.List;

public interface UtilisateurService {
    long countResidents();
    public List<Utilisateur> getAllUtilisateurs();
    public Utilisateur getUtilisateurById(Integer id);
    public Utilisateur getUtilisateurByEmail(String email);
    public Utilisateur saveUtilisateur(Utilisateur utilisateur);
    public void deleteUtilisateur(Integer id);
    boolean existsByEmail(String email);
    List<Utilisateur> getAllResidents();
}
