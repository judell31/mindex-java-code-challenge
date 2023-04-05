package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String reportingStructureUrl;
    private String compensationUrl;
    private String compensationIdUrl;
    private String employeeId;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        reportingStructureUrl = "http://localhost:" + port + "/get-reporting-structure/{id}";
        compensationUrl = "http://localhost:" + port + "/create-compensation";
        compensationIdUrl = "http://localhost:" + port + "/get-compensation/{id}";
        employeeId = "16a596ae-edd3-4847-99fe-c4518e82c86f";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    @Test
    public void getTotalReportingStructureTest() {
        Employee testEmployee = new Employee();

        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        ReportingStructure expectedReportingStructure = new ReportingStructure();

        expectedReportingStructure.setEmployee(testEmployee);
        expectedReportingStructure.setNumberOfReports(4);

        ReportingStructure actualEmployeeReportingStructure = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, employeeId).getBody();

        assertEquals(expectedReportingStructure.getNumberOfReports(), actualEmployeeReportingStructure.getNumberOfReports());
    }

    @Test
    public void createReadEmployeeCompensationTest() {
        Employee testEmployee = new Employee();

        testEmployee.setEmployeeId(UUID.randomUUID().toString());
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        Compensation testCompensation = new Compensation();

        testCompensation.setCompensationId(UUID.randomUUID().toString());
        testCompensation.setEmployee(testEmployee);
        testCompensation.setSalary(100000);
        testCompensation.setEffectiveDate(Instant.now());

        Compensation createdEmployeeCompensation = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class).getBody();
        Compensation getEmployeeCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, createdEmployeeCompensation.getEmployee().getEmployeeId()).getBody();

        assertNotNull(createdEmployeeCompensation.getEmployee().getEmployeeId());
        assertEquals(createdEmployeeCompensation.getEmployee().getFirstName(), getEmployeeCompensation.getEmployee().getFirstName());
        assertEquals(createdEmployeeCompensation.getEmployee().getLastName(), getEmployeeCompensation.getEmployee().getLastName());
        assertEquals(createdEmployeeCompensation.getEmployee().getDepartment(), getEmployeeCompensation.getEmployee().getDepartment());
        assertEquals(createdEmployeeCompensation.getEmployee().getPosition(), getEmployeeCompensation.getEmployee().getPosition());
        assertEquals(createdEmployeeCompensation.getSalary(), getEmployeeCompensation.getSalary());
        assertEquals(createdEmployeeCompensation.getEffectiveDate().truncatedTo(ChronoUnit.MINUTES), getEmployeeCompensation.getEffectiveDate().truncatedTo(ChronoUnit.MINUTES));
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
