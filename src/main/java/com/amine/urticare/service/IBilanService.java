package com.amine.urticare.service;

import com.amine.urticare.resources.dto.BilanRequest;
import com.amine.urticare.resources.dto.BilanResponse;

import java.util.List;

public interface IBilanService {

    BilanResponse upsert(BilanRequest request);

    List<BilanResponse> getHistory(String patientEmail);
}
