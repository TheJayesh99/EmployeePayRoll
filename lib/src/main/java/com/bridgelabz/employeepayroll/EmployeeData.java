package com.bridgelabz.employeepayroll;

public class EmployeeData
{
	private int employeeId;
	private String employeeName;
	private int employeeSalary;
	
	public EmployeeData(int employeeId, String employeeName, int employeeSalary) 
	{
		super();
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.employeeSalary = employeeSalary;
	}

	@Override
	public String toString() {
		return "employeeId=" + employeeId + ", employeeName=" + employeeName + ", employeeSalary="
				+ employeeSalary + "";
	}
	
	
}
