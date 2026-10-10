package com.amine.urticare.service;

import com.amine.urticare.resources.dto.AntihistaminiqueResponse;

import java.util.List;

public interface IAntihistaminiqueService {

    List<AntihistaminiqueResponse> getAll();
}
