package com.bridgelabz.employeepayroll;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService 
{
	private List<EmployeeData> employePayrollList = new ArrayList<EmployeeData>();
	Scanner scanner = new Scanner(System.in);
	private static final String FILE_PATH = "c://Users//malij//OneDrive//Desktop//payroll-file.txt";

	public void readEmployeeDataFromConsole() 
	{
		System.out.println("Enter Employee Id");
		int id = scanner.nextInt();
		System.out.println("Enter Employee Name");
		String Name = scanner.next();
		System.out.println("Enter the salary");
		int salary = scanner.nextInt();
		employePayrollList.add(new EmployeeData(id, Name, salary));
	}

	public void writeEmployeeDataInConsole() 
	{
		System.out.println("Writing Employee Pay Roll Data \n"+employePayrollList);
	}

	public void addEmployee(EmployeeData employee)
	{
		employePayrollList.add(employee);
	}

	public void writeEmployeeDataToFile()  
	{
		checkFile();
		StringBuffer empBuffer = new StringBuffer();
		employePayrollList.forEach(employee ->{
			String employeeDataString = employee.toString().concat("\n");
			empBuffer.append(employeeDataString);
		});
		try {
			Files.write(Paths.get(FILE_PATH), empBuffer.toString().getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//method to create file if file doesn't exist
	private void checkFile() 
	{
		File file = new File(FILE_PATH);
		try 
		{
			//checking file already exists
			if (!file.exists()) 
			{
				//if not creating a new file
				file.createNewFile();
				System.out.println("Created a file at "+FILE_PATH);
			} 		
		}
		catch (IOException e1) 
		{			
			System.err.println("Problem encountered while creating a file");
		}
	}

	//method to count entries
	public long countEntries()
	{
		long entries = 0;
		try 
		{
			entries = Files.lines(new File(FILE_PATH).toPath()).count();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return entries;
	}
	
	//method to print content of file
	public void printData() 
	{
		try 
		{
			Files.lines(Paths.get(FILE_PATH)).forEach(System.out::println);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
