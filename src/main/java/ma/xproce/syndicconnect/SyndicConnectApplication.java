package ma.xproce.syndicconnect;

import ma.xproce.syndicconnect.dao.entities.*;
import ma.xproce.syndicconnect.dao.repositories.*;
import ma.xproce.syndicconnect.service.AnnonceService;
import ma.xproce.syndicconnect.service.CommentaireService;
import ma.xproce.syndicconnect.service.ProblemeService;
import ma.xproce.syndicconnect.service.UtilisateurService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@EnableJpaRepositories("ma.xproce.syndicconnect.dao.repositories")
@EntityScan("ma.xproce.syndicconnect.dao.entities")
@SpringBootApplication
public class SyndicConnectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SyndicConnectApplication.class, args);
    }


}