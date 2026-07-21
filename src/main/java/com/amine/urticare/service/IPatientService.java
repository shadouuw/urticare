package com.amine.urticare.service;

import com.amine.urticare.resources.dto.PatientProfileRequest;
import com.amine.urticare.resources.dto.PatientProfileResponse;

public interface IPatientService {

    PatientProfileResponse upsert(PatientProfileRequest request);
}
