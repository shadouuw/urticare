package com.amine.urticare.service.impl;

import com.amine.urticare.model.entities.AntihistaminiqueEntity;
import com.amine.urticare.repository.AntihistaminiqueRepository;
import com.amine.urticare.resources.dto.AntihistaminiqueResponse;
import com.amine.urticare.service.IAntihistaminiqueService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AntihistaminiqueServiceImpl implements IAntihistaminiqueService {

    private final AntihistaminiqueRepository antihistaminiqueRepository;

    public AntihistaminiqueServiceImpl(AntihistaminiqueRepository antihistaminiqueRepository) {
        this.antihistaminiqueRepository = antihistaminiqueRepository;
    }

    @Override
    public List<AntihistaminiqueResponse> getAll() {
        return antihistaminiqueRepository.findAllByOrderBySortOrderAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private AntihistaminiqueResponse toResponse(AntihistaminiqueEntity e) {
        AntihistaminiqueResponse r = new AntihistaminiqueResponse();
        r.setId(e.getId());
        r.setName(e.getName());
        r.setImageUrl(e.getImageUrl());
        r.setStrengthMg(e.getStrengthMg());
        r.setAlternativeMg(e.getAlternativeMg());
        r.setIsPrincipale(e.getIsPrincipale());
        return r;
    }
}
