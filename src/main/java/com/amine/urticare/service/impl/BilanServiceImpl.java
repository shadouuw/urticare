package com.amine.urticare.service.impl;

import com.amine.urticare.model.entities.BilanEntity;
import com.amine.urticare.repository.BilanRepository;
import com.amine.urticare.resources.dto.BilanRequest;
import com.amine.urticare.resources.dto.BilanResponse;
import com.amine.urticare.service.IBilanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class BilanServiceImpl implements IBilanService {

    private final BilanRepository bilanRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public BilanServiceImpl(BilanRepository bilanRepository) {
        this.bilanRepository = bilanRepository;
    }

    @Override
    @Transactional
    public BilanResponse upsert(BilanRequest request) {
        BilanEntity bilan = bilanRepository.findByPatientEmailAndDate(request.getPatientEmail(), request.getDate())
                .orElseGet(BilanEntity::new);

        bilan.setPatientEmail(request.getPatientEmail());
        bilan.setDate(request.getDate());
        bilan.setMood(request.getMood());
        bilan.setIncludeSuivi(request.getIncludeSuivi());
        bilan.setItchScore(request.getItchScore());
        bilan.setPapulesScore(request.getPapulesScore());
        bilan.setBodyPartsJson(writeJson(request.getBodyParts()));
        bilan.setSymptomsJson(writeJson(request.getSymptoms()));
        bilan.setIncludeSelfies(request.getIncludeSelfies());
        bilan.setSelfieCount(request.getSelfieCount());
        bilan.setIncludeQol(request.getIncludeQol());
        bilan.setQolAnswersJson(writeJson(request.getQolAnswers()));

        BilanEntity saved = bilanRepository.save(bilan);
        return toResponse(saved);
    }

    @Override
    public List<BilanResponse> getHistory(String patientEmail) {
        return bilanRepository.findByPatientEmailOrderByDateDesc(patientEmail)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private BilanResponse toResponse(BilanEntity b) {
        BilanResponse r = new BilanResponse();
        r.setId(b.getId());
        r.setPatientEmail(b.getPatientEmail());
        r.setDate(b.getDate());
        r.setMood(b.getMood());
        r.setIncludeSuivi(b.getIncludeSuivi());
        r.setItchScore(b.getItchScore());
        r.setPapulesScore(b.getPapulesScore());
        r.setBodyParts(readJson(b.getBodyPartsJson(), new TypeReference<Map<String, Boolean>>() {}));
        r.setSymptoms(readJson(b.getSymptomsJson(), new TypeReference<Map<String, Integer>>() {}));
        r.setIncludeSelfies(b.getIncludeSelfies());
        r.setSelfieCount(b.getSelfieCount());
        r.setIncludeQol(b.getIncludeQol());
        r.setQolAnswers(readJson(b.getQolAnswersJson(), new TypeReference<List<Integer>>() {}));
        r.setCreatedAt(b.getCreatedAt());
        return r;
    }

    private String writeJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Unable to serialize bilan field to JSON", e);
        }
    }

    private <T> T readJson(String json, TypeReference<T> typeReference) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Unable to deserialize bilan field from JSON", e);
        }
    }
}
