package com.amine.urticare.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The database/ folder's *.sql files are raw dumps that insert rows with
 * explicit ids, which never advances the underlying Postgres identity
 * sequence. Any table seeded that way then fails its very next real INSERT
 * with a duplicate-key error (id=1 already exists), since the sequence still
 * thinks it hasn't produced anything yet.
 * <p>
 * Runs once per startup and simply advances each sequence to MAX(id)+1 if
 * it's behind — a no-op when everything is already in sync, so it's safe to
 * leave in permanently rather than needing a one-off manual fix.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SequenceResyncRunner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    private static final List<String> TABLES = List.of(
            "public.patient_profiles",
            "public.bilans",
            "public.appointments",
            "public.chat_messages",
            "public.submissions",
            "public.submission_photos",
            "public.suivis"
    );

    @Override
    public void run(String... args) {
        for (String table : TABLES) {
            try {
                jdbcTemplate.execute(String.format(
                        "SELECT setval(pg_get_serial_sequence('%s', 'id'), " +
                                "COALESCE((SELECT MAX(id) FROM %s), 0) + 1, false)",
                        table, table));
            } catch (Exception e) {
                log.warn("Sequence resync skipped for {}: {}", table, e.getMessage());
            }
        }
    }
}
