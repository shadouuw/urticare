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
        bilan.setAlertSymptomsJson(writeJson(request.getAlertSymptoms()));
        bilan.setIncludeSelfies(request.getIncludeSelfies());
        bilan.setSelfieCount(request.getSelfieCount());
        bilan.setIncludeQol(request.getIncludeQol());
        bilan.setQolAnswersJson(writeJson(request.getQolAnswers()));
        bilan.setAttachmentCount(request.getAttachmentCount());
        bilan.setIncludeTraitement(request.getIncludeTraitement());
        bilan.setTraitementType(request.getTraitementType());
        bilan.setTraitementRubrique(request.getTraitementRubrique());
        bilan.setTraitementMedicament(request.getTraitementMedicament());
        bilan.setTraitementPrecision(request.getTraitementPrecision());
        bilan.setTraitementFrequence(request.getTraitementFrequence());
        bilan.setTraitementComprimes(request.getTraitementComprimes());
        bilan.setTraitementPuissanceMg(request.getTraitementPuissanceMg());
        bilan.setTraitementDoseValue(request.getTraitementDoseValue());
        bilan.setTraitementDoseUnit(request.getTraitementDoseUnit());
        bilan.setIncludeInjection(request.getIncludeInjection());
        bilan.setInjectionRubrique(request.getInjectionRubrique());
        bilan.setInjectionPrecision(request.getInjectionPrecision());
        bilan.setInjectionFrequence(request.getInjectionFrequence());
        bilan.setInjectionFrequenceSemaines(request.getInjectionFrequenceSemaines());
        bilan.setInjectionDoseValue(request.getInjectionDoseValue());
        bilan.setInjectionDoseUnit(request.getInjectionDoseUnit());

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
        r.setAlertSymptoms(readJson(b.getAlertSymptomsJson(), new TypeReference<List<String>>() {}));
        r.setIncludeSelfies(b.getIncludeSelfies());
        r.setSelfieCount(b.getSelfieCount());
        r.setIncludeQol(b.getIncludeQol());
        r.setQolAnswers(readJson(b.getQolAnswersJson(), new TypeReference<List<Integer>>() {}));
        r.setAttachmentCount(b.getAttachmentCount());
        r.setIncludeTraitement(b.getIncludeTraitement());
        r.setTraitementType(b.getTraitementType());
        r.setTraitementRubrique(b.getTraitementRubrique());
        r.setTraitementMedicament(b.getTraitementMedicament());
        r.setTraitementPrecision(b.getTraitementPrecision());
        r.setTraitementFrequence(b.getTraitementFrequence());
        r.setTraitementComprimes(b.getTraitementComprimes());
        r.setTraitementPuissanceMg(b.getTraitementPuissanceMg());
        r.setTraitementDoseValue(b.getTraitementDoseValue());
        r.setTraitementDoseUnit(b.getTraitementDoseUnit());
        r.setIncludeInjection(b.getIncludeInjection());
        r.setInjectionRubrique(b.getInjectionRubrique());
        r.setInjectionPrecision(b.getInjectionPrecision());
        r.setInjectionFrequence(b.getInjectionFrequence());
        r.setInjectionFrequenceSemaines(b.getInjectionFrequenceSemaines());
        r.setInjectionDoseValue(b.getInjectionDoseValue());
        r.setInjectionDoseUnit(b.getInjectionDoseUnit());
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
