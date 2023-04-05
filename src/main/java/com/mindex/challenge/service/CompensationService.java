package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.request.CompensationRequest;

public interface CompensationService {
    Compensation getCompensation(String id);
    Compensation createCompensation(CompensationRequest compensationRequest);
}
