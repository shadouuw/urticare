package com.amine.urticare.service.impl;

import com.amine.urticare.model.entities.TraitementEntity;
import com.amine.urticare.repository.TraitementRepository;
import com.amine.urticare.resources.dto.TraitementRequest;
import com.amine.urticare.resources.dto.TraitementResponse;
import com.amine.urticare.service.ITraitementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TraitementServiceImpl implements ITraitementService {

    private final TraitementRepository traitementRepository;

    public TraitementServiceImpl(TraitementRepository traitementRepository) {
        this.traitementRepository = traitementRepository;
    }

    @Override
    @Transactional
    public TraitementResponse upsert(String userId, TraitementRequest request) {
        TraitementEntity entity = traitementRepository.findByUserIdAndType(userId, request.getType())
                .orElseGet(TraitementEntity::new);

        entity.setUserId(userId);
        entity.setType(request.getType());
        entity.setRubrique(request.getRubrique());
        entity.setMedicament(request.getMedicament());
        entity.setPrecision(request.getPrecision());
        entity.setFrequence(request.getFrequence());
        entity.setFrequenceSemaines(request.getFrequenceSemaines());
        entity.setPuissanceMg(request.getPuissanceMg());
        entity.setNombreComprimes(request.getNombreComprimes());
        entity.setScore(request.getScore() != null ? request.getScore() : 0.0);
        entity.setReminderEnabled(request.getReminderEnabled() != null ? request.getReminderEnabled() : false);
        entity.setReminderHour(request.getReminderHour());
        entity.setReminderMinute(request.getReminderMinute());

        TraitementEntity saved = traitementRepository.save(entity);
        return toResponse(saved);
    }

    @Override
    public List<TraitementResponse> getAll(String userId) {
        return traitementRepository.findAllByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void cancel(String userId, String type) {
        traitementRepository.deleteByUserIdAndType(userId, type);
    }

    private TraitementResponse toResponse(TraitementEntity e) {
        TraitementResponse r = new TraitementResponse();
        r.setId(e.getId());
        r.setType(e.getType());
        r.setRubrique(e.getRubrique());
        r.setMedicament(e.getMedicament());
        r.setPrecision(e.getPrecision());
        r.setFrequence(e.getFrequence());
        r.setFrequenceSemaines(e.getFrequenceSemaines());
        r.setPuissanceMg(e.getPuissanceMg());
        r.setNombreComprimes(e.getNombreComprimes());
        r.setScore(e.getScore());
        r.setReminderEnabled(e.getReminderEnabled());
        r.setReminderHour(e.getReminderHour());
        r.setReminderMinute(e.getReminderMinute());
        r.setCreatedAt(e.getCreatedAt());
        r.setUpdatedAt(e.getUpdatedAt());
        return r;
    }
}
