package com.bridgelabz.employeepayroll;

import java.time.LocalDate;
import java.util.Objects;

public class EmployeeData
{
	public int employeeId;
	public String employeeName;
	public int employeeSalary;
	public LocalDate startDate;
	
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeData other = (EmployeeData) obj;
		return employeeId == other.employeeId && Objects.equals(employeeName, other.employeeName)
				&& employeeSalary == other.employeeSalary && Objects.equals(startDate, other.startDate);
	}
	
	
}
