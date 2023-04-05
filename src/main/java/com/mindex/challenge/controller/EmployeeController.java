package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.request.AddCompensationRequest;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ReportingStructureService reportingStructureService;

    @Autowired
    private CompensationService compensationService;

    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.info("Received employee create request");

        return employeeService.create(employee);
    }

    @GetMapping("/get-reporting-structure/{id}")
    public ReportingStructure getReportingStructure(@PathVariable String id) {
        LOG.debug("Received reporting structure get request for employee with id [{}]", id);

        return reportingStructureService.getReportingStructure(id);
    }

    @GetMapping("/get-compensation/{id}")
    public Compensation getEmployeeCompensation(@PathVariable String id) {
        LOG.debug("Received compensation get request for employee with id [{}]", id);

        return compensationService.getCompensation(id);
    }

    @PostMapping("/create-compensation")
    public Compensation createEmployeeCompensation(@RequestBody AddCompensationRequest addCompensationRequest) {
        LOG.info("Received compensation create request");

        return compensationService.createCompensation(addCompensationRequest);
    }

    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee get request for id [{}]", id);

        return employeeService.read(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee update request for id [{}]", id);

        return employeeService.update(employee);
    }
}
