package org.example;

import org.example.model.Employee;
import org.example.model.Job;
import org.example.service.EmployeeService;
import org.example.service.EmployeeServiceImpl;
import org.example.service.JobSerivice;
import org.example.service.JobServiceImpl;

import java.util.Scanner;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        EmployeeService employeeService = new EmployeeServiceImpl();
        JobSerivice jobSerivice = new JobServiceImpl();
        while (true) {
            System.out.println("""
                    1-> methods Employee...
                    2-> methods Job...""");
            System.out.println("Ekoonun biroosun tandanyz.");
            int num = new Scanner(System.in).nextInt();
            switch (num) {
                case 1:
                    while (true) {
                        System.out.println("""
                                1-> create Employee:
                                2-> add Employee:
                                3-> drop table:
                                4-> clean table:
                                5-> update Employee:
                                6-> get all Employees:
                                7-> find by email:
                                8-> get Employee by id:
                                9-> get Employee by position:
                                """);
                        System.out.println("employee methodtorun tandanyz");
                        int emp = new Scanner(System.in).nextInt();
                        switch (emp) {
                            case 1 -> employeeService.createEmployee();
                            case 2 ->
                                    employeeService.addEmployee(new Employee("Adilet", "Ismailov", 22, "adilet@gmail.com", 3));
                            case 3 -> employeeService.dropTable();
                            case 4 -> employeeService.cleanTable();
                            case 5 ->
                                    employeeService.updateEmployee(10L, new Employee("Mukhammed", "Baatyrov", 20, "m@gmail.com", 2));
                            case 6 -> System.out.println(employeeService.getAllEmployees());
                            case 7 -> System.out.println(employeeService.findByEmail("k@gmail.com"));
                            case 8 -> employeeService.getEmployeeById(10L);
                            case 9 -> System.out.println(employeeService.getEmployeeByPosition("Mentor"));
                            default -> System.out.println("no emp methods!");

                        }
                    }
                case 2:
                    while (true) {
                        System.out.println("""
                                1-> create Job table:
                                2-> add Job:
                                3-> get Job by id:
                                4-> sort by Experience: (asc)
                                5-> sort by Experience: (desc)
                                6-> get Job by Employee id:
                                7-> delete Description Column:""");
                        System.out.println("job methodtorun tandanyz.");
                        int job = new Scanner(System.in).nextInt();
                        switch (job) {
                            case 1 -> jobSerivice.createJobTable();
                            case 2 -> jobSerivice.addJob(new Job("Management", "java", "Beckand developer", 3));
                            case 3 -> System.out.println(jobSerivice.getJobById(1L));
                            case 4 -> System.out.println(jobSerivice.sortByExperience("asc"));
                            case 5 -> System.out.println(jobSerivice.sortByExperience("desc"));
                            case 6 -> System.out.println(jobSerivice.getJobByEmployeeId(11L));
                            case 7 -> jobSerivice.deleteDescriptionColumn();
                            default -> System.out.println("not job methods!");
                        }
                    }
            }
        }

    }
}
