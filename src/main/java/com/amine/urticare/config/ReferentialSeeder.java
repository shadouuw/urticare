package com.amine.urticare.config;

import com.amine.urticare.model.entities.GouvernoratEntity;
import com.amine.urticare.model.entities.HospitalEntity;
import com.amine.urticare.model.entities.SpecialiteEntity;
import com.amine.urticare.repository.GouvernoratRepository;
import com.amine.urticare.repository.HospitalRepository;
import com.amine.urticare.repository.SpecialiteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

// Seeds/updates the profile referential lists (gouvernorats, hospitals,
// specialties) on every boot, upserting by name so it's safe to re-run and
// safe to extend — just add entries to the lists below.
//
// Gouvernorats: Tunisia's 24 official governorates (public, standard data).
// Hospitals/specialties: a starter list only — extend/correct these once
// the official lists are provided (e.g. by Dr. Meriem Zidi).
@Component
public class ReferentialSeeder implements CommandLineRunner {

    private final GouvernoratRepository gouvernoratRepository;
    private final HospitalRepository hospitalRepository;
    private final SpecialiteRepository specialiteRepository;

    public ReferentialSeeder(
            GouvernoratRepository gouvernoratRepository,
            HospitalRepository hospitalRepository,
            SpecialiteRepository specialiteRepository) {
        this.gouvernoratRepository = gouvernoratRepository;
        this.hospitalRepository = hospitalRepository;
        this.specialiteRepository = specialiteRepository;
    }

    private static final List<String> GOUVERNORATS = List.of(
            "Ariana", "Béja", "Ben Arous", "Bizerte", "Gabès", "Gafsa", "Jendouba",
            "Kairouan", "Kasserine", "Kébili", "Le Kef", "Mahdia", "Manouba",
            "Médenine", "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana",
            "Sousse", "Tataouine", "Tozeur", "Tunis", "Zaghouan"
    );

    private record HospitalSeed(String name, String type) {
    }

    private static final List<HospitalSeed> HOSPITALS = List.of(
            new HospitalSeed("Hôpital Charles Nicolle (Tunis)", "public"),
            new HospitalSeed("Hôpital La Rabta (Tunis)", "public"),
            new HospitalSeed("Hôpital Habib Thameur (Tunis)", "public"),
            new HospitalSeed("Hôpital Mongi Slim (La Marsa)", "public"),
            new HospitalSeed("Hôpital Farhat Hached (Sousse)", "public"),
            new HospitalSeed("Hôpital Sahloul (Sousse)", "public"),
            new HospitalSeed("Hôpital Fattouma Bourguiba (Monastir)", "public"),
            new HospitalSeed("Hôpital Hédi Chaker (Sfax)", "public"),
            new HospitalSeed("Hôpital Habib Bourguiba (Sfax)", "public"),
            new HospitalSeed("Hôpital Ibn El Jazzar (Kairouan)", "public")
    );

    private static final List<String> SPECIALITES = List.of(
            "Dermatologie",
            "Allergologie",
            "Médecine générale",
            "Pédiatrie",
            "ORL",
            "Immunologie",
            "Médecine interne",
            "Pneumologie"
    );

    @Override
    public void run(String... args) {
        int order = 0;
        for (String name : GOUVERNORATS) {
            GouvernoratEntity entity = gouvernoratRepository.findByName(name).orElseGet(GouvernoratEntity::new);
            entity.setName(name);
            entity.setSortOrder(order++);
            gouvernoratRepository.save(entity);
        }

        order = 0;
        for (HospitalSeed seed : HOSPITALS) {
            HospitalEntity entity = hospitalRepository.findByName(seed.name()).orElseGet(HospitalEntity::new);
            entity.setName(seed.name());
            entity.setType(seed.type());
            entity.setSortOrder(order++);
            hospitalRepository.save(entity);
        }

        order = 0;
        for (String name : SPECIALITES) {
            SpecialiteEntity entity = specialiteRepository.findByName(name).orElseGet(SpecialiteEntity::new);
            entity.setName(name);
            entity.setSortOrder(order++);
            specialiteRepository.save(entity);
        }
    }
}
