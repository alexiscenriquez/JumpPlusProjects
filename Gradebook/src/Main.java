import com.cognixia.jumpplus.dao.Classes;
import com.cognixia.jumpplus.dao.Teacher;
import com.cognixia.jumpplus.dao.TeacherDaoSql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome!");
        Scanner scanner = new Scanner(System.in);
        TeacherDaoSql teacherDao = new TeacherDaoSql();
        try {
            teacherDao.setConnection();
            int choice;
            do {
                System.out.println("Please select from the menu");
                System.out.println("1. Create Account \n2. Log in");
                choice = scanner.nextInt();
                if (choice == 1) {
                    signUp(scanner, teacherDao);
                } else if (choice == 2) {
                    Teacher teacher = signIn(scanner, teacherDao);
                    if (teacher != null) {
                        try {
                            List<Classes> classList = teacherDao.getClasses(teacher.getId());
                            int choice2;
                            do {
                                if (classList.isEmpty()) {
                                    System.out.println("1 - Add a class \t 0- Exit");
                                } else {
                                    System.out.println("1 - Add a class \t 2- View a Class \t 0- Exit");
                                }
                                choice2 = scanner.nextInt();
                                if (choice2 == 1) {
                                addClass(scanner,teacher,teacherDao);
                                }
                                if(choice2==2){

                                }
                            } while (choice2 != 0);
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
            while (choice != 0);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static void signUp(Scanner scanner, TeacherDaoSql teacherDao) {
        System.out.print("First Name:");
        String firstName = scanner.next();
        System.out.print("Last Name: ");
        String lastName = scanner.next();
        System.out.print("Username: ");
        String userName = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();
        Teacher teacher = new Teacher(0, firstName, lastName, userName, password);
        try {
            teacherDao.addTeacher(teacher);
        } catch (SQLException e) {
            System.out.println("User name must be unique");
        }
    }

    public static Teacher signIn(Scanner scanner, TeacherDaoSql teacherDao) {
        System.out.print("Username: ");
        String username = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();
        Teacher teacher = null;
        try {
            Optional<Teacher> found = teacherDao.authenticateTeacher(username, password);

            if (found.isEmpty())
                System.out.println("Account not found, please create an account");
            else {
                teacher = found.get();
                System.out.println("Welcome " + teacher.getFirstName() + " " + teacher.getLastName());
            }
        } catch (SQLException e) {
            System.out.println("Sign in failed");
        }
        return teacher;
    }

    public static void addClass(Scanner scanner, Teacher teacher,TeacherDaoSql teacherDao){

        System.out.print("Class Name:");
        String className = scanner.next();
        System.out.print("Class Num:");
        int num = scanner.nextInt();
        Classes classes = new Classes(0, className, num, teacher.getId());
        try {
            teacherDao.addClass(classes);
            teacherDao.getClasses(teacher.getId());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void viewClass(Scanner scanner){
        System.out.println("Class ID:");
        int classId= scanner.nextInt();

    }
}