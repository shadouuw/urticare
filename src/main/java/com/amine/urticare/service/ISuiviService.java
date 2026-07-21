package com.amine.urticare.service;

import com.amine.urticare.resources.dto.SuiviRequest;
import com.amine.urticare.resources.dto.SuiviResponse;

import java.util.List;

public interface ISuiviService {

    SuiviResponse create(String userId, SuiviRequest request);

    SuiviResponse getActive(String userId);

    boolean hasActive(String userId);

    List<SuiviResponse> getHistory(String userId);

    SuiviResponse getById(String userId, Long id);

    void close(String userId, Long id);
}