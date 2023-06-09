package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    /*
    What do you think about using the Slf4j annotation right under line 12 as opposed to doing this?
     */
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    /*
    Why not use a request object here as the parameter being passed in?
     */
    @Override
    public Employee create(Employee employee) {

        employee.setEmployeeId(UUID.randomUUID().toString());

        LOG.debug("Creating employee with id [{}]", employee.getEmployeeId());

        employeeRepository.insert(employee);

        LOG.info("Employee created");

        return employee;
    }

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
                Employee directReportEmployee = read(directReport.getEmployeeId());

                totalReports += getTotalReports(read(directReportEmployee.getEmployeeId()));
            }
        }

        return totalReports;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Retrieving employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        LOG.info("Employee retrieved");

        return employee;
    }

    // TODO: Solve tomorrow morning
    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }
}
