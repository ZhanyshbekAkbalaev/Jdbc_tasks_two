package org.example.dao;

import org.example.config.Configuration;
import org.example.model.Job;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class JobDaoImpl implements JobDao{
    Connection connection = Configuration.getGonnection();
    @Override
    public void createJobTable() {
        String sql = """
                create table if not exists job(
                id bigserial primary key ,
                position varchar,
                profession varchar ,
                description varchar,
                experience int)""";
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate(sql);
            System.out.println("create table job...");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addJob(Job job) {
        String jobSql = """
                insert into job(position ,profession ,description ,experience)
                values(?,?,?,?);
                """;
        try (Connection connection1 = Configuration.getGonnection()){
            PreparedStatement preparedStatement = connection1.prepareStatement(jobSql);
            preparedStatement.setString(1,job.getPosition());
            preparedStatement.setString(2,job.getProfession());
            preparedStatement.setString(3,job.getDescription());
            preparedStatement.setInt(4,job.getExperience());
            int i = preparedStatement.executeUpdate();
            if (i > 0){
                System.out.println("Successfully prepared Statement!");
            }
            System.out.println("added job...");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Job getJobById(Long jobId) {
        String sql = """
               select *
               from job
               where id = ?
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1,jobId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()){
                System.out.println("NO!");
            }
            Job job = new Job();
                job.setId(resultSet.getLong(1));
                job.setPosition(resultSet.getString(2));
                job.setProfession(resultSet.getString(3));
                job.setDescription(resultSet.getString(4));
                job.setExperience(resultSet.getInt(5));
                return job;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Job> sortByExperience(String ascOrDesc) {
        List<Job> jobs = new ArrayList<>();
        String asc = """
                select * from job order by experience asc;
                """;
        String desc = """
                select * from job order by experience desc;
                """;
        switch (ascOrDesc) {
            case "asc":
                try (PreparedStatement preparedStatement = connection.prepareStatement(asc)) {
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        Job job = new Job();
                        job.setId(resultSet.getLong(1));
                        job.setPosition(resultSet.getString(2));
                        job.setProfession(resultSet.getString(3));
                        job.setDescription(resultSet.getString(4));
                        job.setExperience(resultSet.getInt(5));
                        jobs.add(job);
                    }
                    resultSet.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "desc":
                try (PreparedStatement preparedStatement = connection.prepareStatement(desc)){
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()){
                        Job job = new Job();
                        job.setId(resultSet.getLong(1));
                        job.setPosition(resultSet.getString(2));
                        job.setProfession(resultSet.getString(3));
                        job.setDescription(resultSet.getString(4));
                        job.setExperience(resultSet.getInt(5));
                        jobs.add(job);
                    }
                    resultSet.close();
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
        }
        return jobs;
    }

    @Override
    public Job getJobByEmployeeId(Long employeeId) {
        Job job = new Job();
        try (PreparedStatement preparedStatement = connection.prepareStatement("" +
                "select * from job join employee  on job.id = job_id where employee.id = ? ")){
            preparedStatement.setLong(1,employeeId);
            preparedStatement.executeQuery();
            ResultSet resultSet  = preparedStatement.getResultSet();
            while (resultSet.next()){
                job.setId(resultSet.getLong(1));
                job.setPosition(resultSet.getString(2));
                job.setProfession(resultSet.getString(3));
                job.setDescription(resultSet.getString(4));
                job.setExperience(resultSet.getInt(5));
            }
            resultSet.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return job;
    }

    @Override
    public void deleteDescriptionColumn() {
        String sql = """
                alter table job drop description;
                """;
        try (Statement statement = connection.createStatement()){
            statement.execute(sql);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
