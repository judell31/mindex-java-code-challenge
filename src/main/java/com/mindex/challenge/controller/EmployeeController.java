package com.mindex.challenge.controller;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.info("Received employee create request");

        return employeeService.create(employee);
    }

    @GetMapping("/reporting-structure/{id}")
    public ReportingStructure getReportingStructure(@PathVariable String id) {
        LOG.debug("Received reporting structure get request for employee with id [{}]", id);

        return employeeService.getReportingStructure(id);
    }

//    @PostMapping("/get-compensation/{id}")
//    public ReportingStructure getEmployeeCompensation(@PathVariable String id) {
//        LOG.debug("Received reporting structure get request for employee with id [{}]", id);
//
//        return employeeService.getReportingStructure(id);
//    }
//
//    @GetMapping("/create-compensation")
//    public ReportingStructure createEmployeeCompensation(@RequestBody Compensation compensation) {
//        LOG.debug("Received reporting structure get request for employee with id [{}]", compensation);
//
//        return employeeService.getReportingStructure(compensation);
//    }

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
