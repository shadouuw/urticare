package com.amine.urticare.service;

import com.amine.urticare.resources.dto.RecoveryConfirmRequest;
import com.amine.urticare.resources.dto.RecoveryRequest;
import com.amine.urticare.resources.dto.RecoveryResponse;
import com.amine.urticare.resources.dto.RecoveryStartResponse;

public interface IRecoveryService {

    RecoveryStartResponse startRecovery(RecoveryRequest request);

    RecoveryResponse confirmRecovery(RecoveryConfirmRequest request);
}
