package com.bridgelabz.employeepayroll;

import java.time.LocalDate;

public class EmployeeData
{
	private int employeeId;
	private String employeeName;
	private int employeeSalary;
	private LocalDate startDate;
	
	public EmployeeData(int employeeId, String employeeName, int employeeSalary) 
	{
		super();
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.employeeSalary = employeeSalary;
	}
	

	public EmployeeData(int employeeId, String employeeName, int employeeSalary, LocalDate startDate) 
	{
		super();
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.employeeSalary = employeeSalary;
		this.startDate = startDate;
	}



	@Override
	public String toString() 
	{
		return "EmployeeData [employeeId=" + employeeId + ", employeeName=" + employeeName + ", employeeSalary="
				+ employeeSalary + ", startDate=" + startDate + "]";
	}
	
	
}
