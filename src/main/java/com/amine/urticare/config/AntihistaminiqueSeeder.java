package com.amine.urticare.config;

import com.amine.urticare.model.entities.AntihistaminiqueEntity;
import com.amine.urticare.repository.AntihistaminiqueRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

// Seeds/updates the antihistaminique referential (Annexe 8 — dénomination
// commune internationale des antihistaminiques) on every boot — this is
// shared reference data, not something a user creates through the API.
// imageUrl is stored as a path RELATIVE to the backend (e.g.
// "/uploads/referentiel/cetirizine.png"), not an absolute URL: the client
// resolves it against whatever base URL it uses to reach this backend
// (e.g. 10.0.2.2 from the Android emulator vs. localhost from a browser),
// which an absolute URL baked in here could never account for.
@Component
public class AntihistaminiqueSeeder implements CommandLineRunner {

    private final AntihistaminiqueRepository antihistaminiqueRepository;

    public AntihistaminiqueSeeder(AntihistaminiqueRepository antihistaminiqueRepository) {
        this.antihistaminiqueRepository = antihistaminiqueRepository;
    }

    private record Seed(String name, double strengthMg, Double alternativeMg) {
    }

    // strengthMg is the typical/principal adult tablet strength, used as the
    // default "puissance" in the prescription's total-dose computation.
    // alternativeMg is a commonly available secondary strength for the same
    // molecule, offered as a second choice in the strength picker (null when
    // no other adult strength is commonly available). Both are indicative —
    // always overridden by the doctor's actual prescription.
    private static final List<Seed> SEEDS = List.of(
            new Seed("Bropheniramine", 4, null),
            new Seed("Cyproheptadine", 4, 2.0),
            new Seed("Dexchlorpheniramine", 2, null),
            new Seed("Hydroxyzine", 25, 10.0),
            new Seed("Mequitazine", 5, null),
            new Seed("Promethazine", 25, 10.0),
            new Seed("Bilastine", 20, 10.0),
            new Seed("Cetirizine", 10, 5.0),
            new Seed("Desloratadine", 5, 2.5),
            new Seed("Ebastine", 10, 20.0),
            new Seed("Fexofenadine", 120, 180.0),
            new Seed("Levocetirizine", 5, 2.5),
            new Seed("Loratadine", 10, 5.0),
            new Seed("Mizolastine", 10, null),
            new Seed("Rupatadine", 10, null)
    );

    @Override
    public void run(String... args) {
        int order = 0;
        for (Seed seed : SEEDS) {
            var existing = antihistaminiqueRepository.findByName(seed.name());
            AntihistaminiqueEntity entity = existing.orElseGet(AntihistaminiqueEntity::new);
            entity.setName(seed.name());
            entity.setImageUrl("/uploads/referentiel/" + slug(seed.name()) + ".png");
            entity.setSortOrder(order++);
            // strengthMg/alternativeMg/isPrincipale are only seeded for
            // brand-new rows — an existing row may have been hand-tuned
            // directly in the database, and a restart shouldn't clobber that.
            if (existing.isEmpty()) {
                entity.setStrengthMg(seed.strengthMg());
                entity.setAlternativeMg(seed.alternativeMg());
                entity.setIsPrincipale(true);
            }
            antihistaminiqueRepository.save(entity);
        }
    }

    private String slug(String name) {
        return name.toLowerCase().replaceAll("[^a-z0-9]+", "-");
    }
}
