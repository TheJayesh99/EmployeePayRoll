package com.bridgelabz.employeepayroll;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class EmployeePayrollService 
{
	private List<EmployeeData> employePayrollListFile = new ArrayList<EmployeeData>();
	Scanner scanner = new Scanner(System.in);
	private static final String FILE_PATH = "c://Users//malij//OneDrive//Desktop//payroll-file.txt";
	List<EmployeeData> employeePayrollListDB = new ArrayList<>();

	//to create prepared statement
	private PreparedStatement employeePayrollStatement;
	private static EmployeePayrollService employeePayrollService;
	public  EmployeePayrollService getInstance()
	{
		if (employeePayrollService == null)
		{
			employeePayrollService = new EmployeePayrollService();
		}
		return employeePayrollService;
	}

	public void readEmployeeDataFromConsole() 
	{
		System.out.println("Enter Employee Id");
		int id = scanner.nextInt();
		System.out.println("Enter Employee Name");
		String Name = scanner.next();
		System.out.println("Enter the salary");
		int salary = scanner.nextInt();
		employePayrollListFile.add(new EmployeeData(id, Name, salary));
	}

	public void writeEmployeeDataInConsole() 
	{
		System.out.println("Writing Employee Pay Roll Data \n"+employePayrollListFile);
	}

	public void addEmployee(EmployeeData employee)
	{
		employePayrollListFile.add(employee);
	}

	public void writeEmployeeDataToFile()  
	{
		checkFile();
		StringBuffer empBuffer = new StringBuffer();
		employePayrollListFile.forEach(employee ->{
			String employeeDataString = employee.toString().concat("\n");
			empBuffer.append(employeeDataString);
		});

		try {
			Files.write(Paths.get(FILE_PATH), empBuffer.toString().getBytes());
		} 
		catch (IOException e) 
		{
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

	public long readDataFromFile()
	{
		try 
		{
			String data = Files.readString(Paths.get(FILE_PATH));
			System.out.println(data);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return countEntries();
	}
	
	//method to read data from database
	public List<EmployeeData> readEmployeePayrollFromDB() 
	{
		String sql = " select * from employee";
		return getQueryResult(sql);
	}

	//gets the query result and store them into object;
	private List<EmployeeData> getQueryResult(String sql) {
		try(Connection connection = this.getConnection())
		{
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			employeePayrollListDB = this.getEmployeeDataFromDB(resultSet);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return employeePayrollListDB;
	}

	//creating connection
	private Connection getConnection() throws SQLException
	{
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
		String username = "root";
		String password = "Jayesh@13";
		System.out.println("Conecting to database"+jdbcURL);
		Connection connection = null;
		connection = DriverManager.getConnection(jdbcURL,username,password);
		System.out.println("Connection sucessful" + connection);

		return connection;
	}

	public int updateSalaryInDB(String name, int salary)
	{
		int result = updateSalary(name, salary); //updating salary
		if (result != 0) 
		{
			EmployeeData employeeData = this.getEmployeeData(name); 
			if (employeeData != null)
			{
				employeeData.employeeSalary = salary;  //changing value in list
			}
		}
		return result;
	}

	private EmployeeData getEmployeeData(String name)  //Searching employee by name 
	{
		return employePayrollListFile.stream()
				.filter(employee -> employee.employeeName.equals(name))
				.findFirst()
				.orElse(null); 
	}

	private int updateSalary(String name, int salary) 
	{
		try(Connection connection = this.getConnection())
		{
			String sql = "update employee set basicPay = "+salary+" where name = '"+name+"'";
			Statement statement = connection.createStatement();
			int rowChanged = statement.executeUpdate(sql);
			return rowChanged;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean checkSyncWithDB(String name)  //checking sync with DB
	{
		List<EmployeeData> employeeDataFromDBList = new EmployeePayrollService().getEmployeeDataFromDB(name);
		return employeeDataFromDBList.get(0).equals(getEmployeeDataFromDB(name).get(0));
	}
	
	//setting the values
	private List<EmployeeData> getEmployeeDataFromDB(ResultSet resultSet)
	{
		List<EmployeeData> employeePayrollData = new ArrayList<>();
		try 
		{
			while(resultSet.next())
			{
				int id = resultSet.getInt("emp_id");
				String name = resultSet.getString("name");
				Integer salary = resultSet.getInt("basicPay");
				LocalDate startdate = resultSet.getDate("start").toLocalDate();
				employeePayrollData.add(new EmployeeData(id, name, salary, startdate));
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return employeePayrollData;
	}

	private List<EmployeeData> getEmployeeDataFromDB(String name) //Getting data from database
	{
		List<EmployeeData> employeePayrollList = new ArrayList<>();
		if (this.employeePayrollStatement == null) //checking for existing prepare statement
		{
			this.prepareStatementForEmployee();
		}
		try 
		{
			employeePayrollStatement.setString(1, name);
			ResultSet resultSet = employeePayrollStatement.executeQuery();
			employeePayrollList = this.getEmployeeDataFromDB(resultSet);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	//created a prepared statement
	private void prepareStatementForEmployee() 
	{
		try 
		{
			Connection connection = this.getConnection();
			String sql = "Select * from employee where name = ? ";
			employeePayrollStatement = connection.prepareStatement(sql);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	//Retrieving data after joining date
	public List<EmployeeData> employeeJoinedAfterDate(String date) 
	{
		String sql = " Select  * from employee  Where start Between cast('"+date+"' as date) and date(now());";
		return getQueryResult(sql);
	}

	public double calculateOfSalaryByGender(String operation,char gender)
	{
		try(Connection connection = this.getConnection())
		{
			String sql = " select "+operation+"(basicPay) from employee where gender='"+gender+"' group by gender;";
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			resultSet.next();
			return resultSet.getDouble(operation+"(basicPay)");				
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return 0.0;
	}


	public void addDataInDB(String name, char gender, int basicPay, String startDate)
	{
		try(Connection connection = this.getConnection())
		{
			String sql = "insert into employee (name,gender,basicpay,start) values "
					+ "('"+name+"','"+gender+"',"+basicPay+",'"+startDate+"');";
			Statement statement = connection.createStatement();
			int rowsChanged = statement.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
			if (rowsChanged == 1) 
			{
				ResultSet resultSet = statement.getGeneratedKeys();
				if(resultSet.next())
				{
					int employeeId = resultSet.getInt(1);
					employeePayrollListDB.add(new EmployeeData(employeeId, name,basicPay,LocalDate.parse(startDate)));
				}
			}
			System.out.println(employeePayrollListDB.toString());
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}



}
