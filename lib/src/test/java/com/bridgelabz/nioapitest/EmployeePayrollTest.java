package com.bridgelabz.nioapitest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.bridgelabz.employeepayroll.EmployeeData;
import com.bridgelabz.employeepayroll.EmployeePayrollService;

public class EmployeePayrollTest 
{
	@Test
	public void given3Employees_WhenWrittenToFile_ShouldMatchEmployeeEntries() 
	{
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.addEmployee(new EmployeeData(1,"Jeff Bezos",10000));
		employeePayrollService.addEmployee(new EmployeeData(2,"Bill Gates",20000));
		employeePayrollService.addEmployee(new EmployeeData(3,"Mark Z",30000));
		employeePayrollService.writeEmployeeDataToFile();
		assertEquals(3,employeePayrollService.countEntries());
	}

	@Test
	public void given3Employees_WhenWrittenToFile_ShouldPrintEmployeeEntries() 
	{
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.addEmployee(new EmployeeData(1,"Jeff Bezos",10000));
		employeePayrollService.addEmployee(new EmployeeData(2,"Bill Gates",20000));
		employeePayrollService.addEmployee(new EmployeeData(3,"Mark Z",30000));
		employeePayrollService.writeEmployeeDataToFile();
		employeePayrollService.printData();
		assertEquals(3, employeePayrollService.countEntries());
	}

	@Test
	public void given3Employees_WhenWrittenToFile_ShouldPrintNumberOfEmployeeEntries() 
	{
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.addEmployee(new EmployeeData(1,"Jeff Bezos",10000));
		employeePayrollService.addEmployee(new EmployeeData(2,"Bill Gates",20000));
		employeePayrollService.addEmployee(new EmployeeData(3,"Mark Z",30000));
		employeePayrollService.writeEmployeeDataToFile();
		assertEquals(3, employeePayrollService.countEntries());
	}

	@Test
	public void givenFile_WhenReadingFromFile_ShouldMatchEmployeeCount() 
	{
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		assertEquals(3, employeePayrollService.countEntries());
	}
	
	@Test
	public void givenEmployeePayrollInDB_WhenRetrived_ShouldMatchEmployeeCount()
	{
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		 List<EmployeeData> employeeList = employeePayrollService.readEmployeePayrollFromDB();
		assertEquals(4, employeeList.size());
	}
	
	@Test
	public void givenEmployeePayrollDB_WhenTeresaSalaryUpdated_ShouldUpdateSalary()
	{
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		int rowsChanged  = employeePayrollService.updateSalaryInDB("Teresa",3000000);
		assertEquals(1, rowsChanged);
	}

	@Test
	public void givenEmployeePayrollDB_WhenTeresaSalaryUpdated_ShouldSyncWithDatabase()
	{
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.updateSalaryInDB("Teresa",3000000);
		boolean result = employeePayrollService.checkSyncWithDB("Teresa");
		assertTrue(result);
	}
	
	@Test
	public void givenEmployeePayrollDB_WhenGivendate_ShouldRetunListOfEmployees()
	{
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeeData> employeeList = employeePayrollService.employeeJoinedAfterDate("2019-01-01");
		assertEquals(3, employeeList.size());
	}

}

