package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.request.AddCompensationRequest;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class CompensationServiceImpl implements CompensationService {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Compensation getCompensation(String id) {
        LOG.debug("Retrieving employee compensation with employee id [{}]", id);

        Compensation compensation = compensationRepository.findCompensationByEmployeeEmployeeId(id);

        LOG.info("Employee compensation retrieved");

        return compensation;
    }

    @Override
    public Compensation createCompensation(AddCompensationRequest addCompensationRequest) {
        Compensation compensation = new Compensation();

        addCompensationRequest.getEmployee().setEmployeeId(UUID.randomUUID().toString());

        compensation.setCompensationId(UUID.randomUUID().toString());
        compensation.setEmployee(addCompensationRequest.getEmployee());
        compensation.setEffectiveDate(Instant.now());
        compensation.setSalary(addCompensationRequest.getSalary());

        LOG.debug("Creating compensation for employee with id [{}]", compensation.getEmployee().getEmployeeId());

        compensationRepository.insert(compensation);

        LOG.info("Employee compensation created");

        return compensation;
    }
}
