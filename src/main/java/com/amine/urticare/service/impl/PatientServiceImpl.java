package com.amine.urticare.service.impl;

import com.amine.urticare.model.entities.PatientProfile;
import com.amine.urticare.repository.PatientProfileRepository;
import com.amine.urticare.resources.dto.PatientProfileRequest;
import com.amine.urticare.resources.dto.PatientProfileResponse;
import com.amine.urticare.service.IPatientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientServiceImpl implements IPatientService {

    private final PatientProfileRepository patientProfileRepository;

    public PatientServiceImpl(PatientProfileRepository patientProfileRepository) {
        this.patientProfileRepository = patientProfileRepository;
    }

    @Override
    @Transactional
    public PatientProfileResponse upsert(PatientProfileRequest request) {
        PatientProfile profile = patientProfileRepository.findByEmail(request.getEmail())
                .orElseGet(PatientProfile::new);

        profile.setEmail(request.getEmail());
        profile.setFirstname(request.getFirstname());
        profile.setLastname(request.getLastname());
        profile.setPhone(request.getPhone());
        profile.setRegion(request.getRegion());
        profile.setAge(request.getAge());
        profile.setWeight(request.getWeight());
        profile.setGouvernorat(request.getGouvernorat());
        profile.setCountry(request.getCountry());
        profile.setSuiviType(request.getSuiviType());
        profile.setHospital(request.getHospital());
        profile.setDoctorName(request.getDoctorName());
        profile.setDoctorSpecialty(request.getDoctorSpecialty());

        PatientProfile saved = patientProfileRepository.save(profile);
        return toResponse(saved);
    }

    private PatientProfileResponse toResponse(PatientProfile p) {
        PatientProfileResponse r = new PatientProfileResponse();
        r.setId(p.getId());
        r.setEmail(p.getEmail());
        r.setFirstname(p.getFirstname());
        r.setLastname(p.getLastname());
        r.setPhone(p.getPhone());
        r.setRegion(p.getRegion());
        r.setAge(p.getAge());
        r.setWeight(p.getWeight());
        r.setGouvernorat(p.getGouvernorat());
        r.setCountry(p.getCountry());
        r.setSuiviType(p.getSuiviType());
        r.setHospital(p.getHospital());
        r.setDoctorName(p.getDoctorName());
        r.setDoctorSpecialty(p.getDoctorSpecialty());
        r.setCreatedAt(p.getCreatedAt());
        r.setUpdatedAt(p.getUpdatedAt());
        return r;
    }
}
