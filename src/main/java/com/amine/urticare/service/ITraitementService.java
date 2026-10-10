package com.amine.urticare.service;

import com.amine.urticare.resources.dto.TraitementRequest;
import com.amine.urticare.resources.dto.TraitementResponse;

import java.util.List;

public interface ITraitementService {

    // Upserts by (userId, type) — a patient's oral and injectable
    // prescriptions are independent, not mutually exclusive.
    TraitementResponse upsert(String userId, TraitementRequest request);

    // Returns every active prescription for the user (0, 1, or 2: at most
    // one oral and one injectable).
    List<TraitementResponse> getAll(String userId);

    // Cancels (deletes) the user's prescription of the given type. History
    // (bilans, suivi logs) is untouched — only the "active prescription"
    // record itself is removed.
    void cancel(String userId, String type);
}
