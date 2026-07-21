package com.amine.urticare.service.impl;

import com.amine.urticare.model.entities.Suivi;
import com.amine.urticare.repository.SuiviRepository;
import com.amine.urticare.resources.dto.SuiviRequest;
import com.amine.urticare.resources.dto.SuiviResponse;
import com.amine.urticare.service.ISuiviService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class SuiviServiceImpl implements ISuiviService {

    private final SuiviRepository suiviRepository;

    public SuiviServiceImpl(SuiviRepository suiviRepository) {
        this.suiviRepository = suiviRepository;
    }

    @Override
    @Transactional
    public SuiviResponse create(String userId, SuiviRequest request) {
        if (suiviRepository.existsByUserIdAndIsActiveTrue(userId)) {
            throw new IllegalStateException("An active suivi already exists. Close it before creating a new one.");
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date.");
        }

        Suivi suivi = new Suivi();
        suivi.setUserId(userId);
        suivi.setTreatmentName(request.getTreatmentName());
        suivi.setDosage(request.getDosage());
        suivi.setPillsPerDay(request.getPillsPerDay());
        suivi.setStartDate(request.getStartDate());
        suivi.setEndDate(request.getEndDate());
        suivi.setDoctorName(request.getDoctorName());
        suivi.setNotes(request.getNotes());
        suivi.setIsActive(true);

        Suivi saved = suiviRepository.save(suivi);
        return toResponse(saved);
    }

    @Override
    public SuiviResponse getActive(String userId) {
        Suivi suivi = suiviRepository.findByUserIdAndIsActiveTrue(userId)
                .orElse(null);

        if (suivi == null) return null;

        // Auto-close if expired
        if (suivi.isExpired()) {
            suivi.setIsActive(false);
            suiviRepository.save(suivi);
            return null;
        }

        return toResponse(suivi);
    }

    @Override
    public boolean hasActive(String userId) {
        return getActive(userId) != null;
    }

    @Override
    public List<SuiviResponse> getHistory(String userId) {
        return suiviRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public SuiviResponse getById(String userId, Long id) {
        Suivi suivi = suiviRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Suivi not found"));

        if (!suivi.getUserId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        return toResponse(suivi);
    }

    @Override
    @Transactional
    public void close(String userId, Long id) {
        Suivi suivi = suiviRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Suivi not found"));

        if (!suivi.getUserId().equals(userId)) {
            throw new RuntimeException("Access denied");
        }

        suivi.setIsActive(false);
        suiviRepository.save(suivi);
    }

    private SuiviResponse toResponse(Suivi s) {
        SuiviResponse r = new SuiviResponse();
        r.setId(s.getId());
        r.setTreatmentName(s.getTreatmentName());
        r.setDosage(s.getDosage());
        r.setPillsPerDay(s.getPillsPerDay());
        r.setStartDate(s.getStartDate());
        r.setEndDate(s.getEndDate());
        r.setDoctorName(s.getDoctorName());
        r.setNotes(s.getNotes());
        r.setIsActive(s.getIsActive());
        r.setIsExpired(s.isExpired());
        r.setCreatedAt(s.getCreatedAt());

        LocalDate now = LocalDate.now();
        long total = ChronoUnit.DAYS.between(s.getStartDate(), s.getEndDate());
        long elapsed = ChronoUnit.DAYS.between(s.getStartDate(), now);
        long remaining = ChronoUnit.DAYS.between(now, s.getEndDate());

        r.setTotalDays((int) total);
        r.setDaysElapsed((int) Math.max(0, Math.min(elapsed, total)));
        r.setDaysRemaining((int) Math.max(0, remaining));

        return r;
    }
}