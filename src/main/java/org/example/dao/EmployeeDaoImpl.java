package org.example.dao;

import org.example.config.Configuration;
import org.example.model.Employee;
import org.example.model.Job;

import java.awt.print.PrinterJob;
import java.sql.*;
import java.util.*;

public class EmployeeDaoImpl implements EmployeeDao {
    Connection connection = Configuration.getGonnection();
    @Override
    public void createEmployee() {
        try (Statement statement = connection.createStatement()){
            statement.execute("""
create table if not exists employee(
id bigserial primary key ,
first_name varchar,
last_name varchar,
age int ,
email varchar unique ,
job_id int REFERENCES job(id));  
""");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addEmployee(Employee employee) {
        String sql = """
                insert into employee(first_name ,last_name ,age,email,job_id) 
                values(?,?,?,?,?);
                """;
        try (Connection connection = Configuration.getGonnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setInt(3, employee.getAge());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setLong(5, employee.getJobId());
            int i = preparedStatement.executeUpdate();
            if (i > 0){
                System.out.println("Successfully prepared Statement!");
            }
            System.out.println("added employee..");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void dropTable() {
        String sql = """
                drop table employee""";
        try (Statement statement  = connection.createStatement()){
            int i = statement.executeUpdate(sql);
            if (i > 0){
                System.out.println("Delete table...");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void cleanTable() {
        String clean = """
                delete from employee;
                """;
        try (Statement statement = connection.createStatement()){
            boolean execute = statement.execute(clean);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEmployee(Long id, Employee employee) {
        String update = """
                update employee set first_name= ? ,last_name = ? ,age = ? ,email = ? ,job_id = ?
                where id = ?;
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(update)){
            preparedStatement.setString(1,employee.getFirstName());
            preparedStatement.setString(2,employee.getLastName());
            preparedStatement.setInt(3,employee.getAge());
            preparedStatement.setString(4,employee.getEmail());
            preparedStatement.setLong(5,employee.getJobId());
            preparedStatement.setLong(6,id);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String getAll = """
                select * from employee;
                """;
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(getAll);
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getLong(1));
                employee.setFirstName(resultSet.getString(2));
                employee.setLastName(resultSet.getString(3));
                employee.setAge(resultSet.getInt(4));
                employee.setEmail(resultSet.getString(5));
                employee.setJobId(resultSet.getInt(6));
                employees.add(employee);
            }resultSet.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return employees;
    }

    @Override
    public Employee findByEmail(String email) {
        String emailF = """
                select * from employee where email = ?;
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(emailF)){
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()){
                System.out.println("no result set!");
            }
            Employee employee = new Employee();
            employee.setId(resultSet.getLong(1));
            employee.setFirstName(resultSet.getString(2));
            employee.setLastName(resultSet.getString(3));
            employee.setAge(resultSet.getInt(4));
            employee.setEmail(resultSet.getString(5));
            employee.setJobId(resultSet.getInt(6));
            resultSet.close();
            return employee;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<Employee, Job> getEmployeeById(Long employeeId) {
        Map<Employee ,Job> map = new LinkedHashMap<>();
        String sql = """
                select first_name,last_name,age,email,job_id from employee join job j on employee.job_id = j.id where employee.id = ?;
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1,employeeId);
            ResultSet resultSet  = preparedStatement.executeQuery();
            if (!resultSet.next()){
                System.out.println("no by id !");
            }
            Employee employee = new Employee();
            employee.setId(resultSet.getLong(1));
            employee.setFirstName(resultSet.getString(2));
            employee.setLastName(resultSet.getString(3));
            employee.setAge(resultSet.getInt(4));
            employee.setEmail(resultSet.getString(5));
            employee.setJobId(resultSet.getInt(6));

            Job job = new Job();
            job.setId(resultSet.getLong(1));
            job.setPosition(resultSet.getString(2));
            job.setProfession(resultSet.getString(3));
            job.setDescription(resultSet.getString(4));
            job.setExperience(resultSet.getInt(5));
            map.put(employee,job);
            resultSet.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return map;
    }

    @Override
    public List<Employee> getEmployeeByPosition(String position) {
        List<Employee> employees = new ArrayList<>();
        String sq = """
                select * from employee join job j on employee.job_id = j.id where position = ?;
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sq)){
            preparedStatement.setString(1,position);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()){
                System.out.println("no job!");
            }
            Employee employee = new Employee();
            employee.setId(resultSet.getLong(1));
            employee.setFirstName(resultSet.getString(2));
            employee.setLastName(resultSet.getString(3));
            employee.setAge(resultSet.getInt(4));
            employee.setEmail(resultSet.getString(5));
            employee.setJobId(resultSet.getInt(6));
            employees.add(employee);
            resultSet.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return employees;
    }
}
