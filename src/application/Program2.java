package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.Scanner;

public class Program2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("=== Test 1: Department insert ===");
        Department department = new Department(null, "Music");
        departmentDao.insert(department);
        System.out.println("Inserted! New id = " + department.getId());

        System.out.println("\n=== Test 2: Department update ===");
        departmentDao.update(new Department(null,"Library"), 15);

        System.out.println("\n=== Test 3: Department findById ===");
        Department department2 = departmentDao.findById(3);
        System.out.println(department2);


    }
}
