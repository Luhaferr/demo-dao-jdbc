package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.List;
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
        System.out.println("Update completed");

        System.out.println("\n=== Test 3: Department findById ===");
        Department department2 = departmentDao.findById(3);
        System.out.println(department2);

        System.out.println("\n=== Test 4: Department deleteById ===");
        System.out.println("Enter id for delete test: ");
        int id = scanner.nextInt();
        departmentDao.deleteById(id);
        System.out.println("Delete completed");

        System.out.println("\n=== Test 5: Department findAll ===");
        List<Department> list = departmentDao.findAll();
        for (Department obj : list) {
            System.out.println(obj);
        }
    }
}
