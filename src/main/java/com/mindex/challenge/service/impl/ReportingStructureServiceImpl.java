package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeServiceImpl employeeService;

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Override
    public ReportingStructure getReportingStructure(String id) {
        Employee employee = employeeRepository.findByEmployeeId(id);

        ReportingStructure reportingStructure = new ReportingStructure();

        reportingStructure.setEmployee(employee);
        reportingStructure.setNumberOfReports(getTotalReports(employee));

        LOG.info("Retrieved reporting structure");

        return reportingStructure;
    }

    private int getTotalReports(Employee employee) {
        int totalReports = 0;

        if (employee.getDirectReports() != null) {
            totalReports = employee.getDirectReports().size();

            for (Employee directReport : employee.getDirectReports()) {
                Employee directReportEmployee = employeeService.read(directReport.getEmployeeId());

                totalReports += getTotalReports(employeeService.read(directReportEmployee.getEmployeeId()));
            }
        }

        return totalReports;
    }
}
