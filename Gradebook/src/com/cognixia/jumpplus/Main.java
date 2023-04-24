package com.cognixia.jumpplus;

import com.cognixia.jumpplus.dao.*;
import com.cognixia.jumpplus.exception.ResourceNotFoundException;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static final String ANSI_Green = "\u001B[32m";
    public static final String ANSI_Blue = "\u001B[34m";
    public static final String ANSI_Cyan = "\u001B[36m";
    public static final String ANSI_RESET="\u001B[37m";
    public static final String ANSI_Red="\u001B[31m";
    public static void main(String[] args) {
        System.out.println("Welcome!");
        Scanner scanner = new Scanner(System.in);
        TeacherDaoSql teacherDao = new TeacherDaoSql();
        try {
            teacherDao.setConnection();

            menu(scanner,teacherDao);

        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
public static void menu(Scanner scanner,TeacherDaoSql teacherDao){
    int choice;
    int role;

    StudentDaoSql studentDao=new StudentDaoSql();

    do {
        System.out.println(ANSI_Cyan+"Please select from the menu");
        System.out.println(ANSI_Blue+"1. Create Account \n2. Log in") ;
        choice = scanner.nextInt();
        if (choice == 1) {
            System.out.println(ANSI_Green+"1- Teacher \t2- Student" + ANSI_RESET);
            role = scanner.nextInt();
            if (role == 1)
                signUp(scanner, teacherDao);
            else {
                try {
                    studentDao.setConnection();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                signUp(scanner, studentDao);
            }

        } else if (choice == 2) {
            System.out.println(ANSI_Green+"1- Teacher \t2- Student" + ANSI_RESET);
            role = scanner.nextInt();
            if (role == 1) {
                Teacher teacher = signIn(scanner, teacherDao);
                if (teacher != null) {
                    try {
                        int choice2;
                        do {
                            List<Classes> classList = teacherDao.getClasses(teacher.getId());
                            if (classList.isEmpty()) {
                                System.out.println(ANSI_Green+"1 - Add a class \t 0- Exit"  + ANSI_RESET);
                            } else {
                                System.out.println(ANSI_Green+"1 - Add a class \t 2- View a Class \t 0- Exit"  + ANSI_RESET);
                            }
                            choice2 = scanner.nextInt();
                            if (choice2 == 1) {
                                addClass(scanner, teacher, teacherDao);
                            }
                            if (choice2 == 2) {
                                viewClass(scanner, teacherDao, teacher.getId());
                            }
                        } while (choice2 != 0);
                    } catch (SQLException e) {
                        System.out.println(ANSI_Red+e.getMessage()+ANSI_RESET);
                    }
                }
            }
            else if (role==2){
                try {
                    studentDao.setConnection();
                    Student student=signIn(scanner,studentDao);
                    if(student!=null){
                        studentDao.getStudentClasses(student.getId());
                    }
                } catch (SQLException | IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
    while (choice != 0);
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
            System.out.println(ANSI_Red+"User name must be unique"+ANSI_RESET);
        }
    }
    public static void signUp(Scanner scanner, StudentDaoSql studentDaoSql) {
        System.out.print("First Name:");
        String firstName = scanner.next();
        System.out.print("Last Name: ");
        String lastName = scanner.next();
        System.out.print("Username: ");
        String userName = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();
        Student student = new Student(0, firstName, lastName, userName, password);
        try {
            studentDaoSql.signUp(student);
        } catch (SQLException e) {
            System.out.println(ANSI_Red+"User name must be unique"+ANSI_RESET);
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
                System.out.println(ANSI_Red+"Account not found, please create an account"+ANSI_RESET);
            else {
                teacher = found.get();
                System.out.println(ANSI_Cyan+"Welcome " + teacher.getFirstName() + " " + teacher.getLastName());
            }
        } catch (SQLException e) {
            System.out.println(ANSI_Red+"Sign in failed"+ANSI_RESET);
        }
        return teacher;
    }
    public static Student signIn(Scanner scanner, StudentDaoSql studentDao){
        System.out.print("Username: ");
        String username = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();
        Student student = null;
        try {
            Optional<Student> found = studentDao.authenticate(username, password);

            if (found.isEmpty())
                System.out.println(ANSI_Red+"Account not found, please create an account"+ANSI_RESET);
            else {
                student = found.get();
                System.out.println(ANSI_Cyan+"Welcome " + student.getFirstName() + " " + student.getLastName());
            }
        } catch (SQLException e) {
            System.out.println(ANSI_Red+"Sign in failed"+ANSI_RESET);
        }
        return student;
    }
    public static void addClass(Scanner scanner, Teacher teacher, TeacherDaoSql teacherDao) {

        System.out.print("Class Name:");
        String className = scanner.next();
        System.out.print("Class Num:");
        int num = scanner.nextInt();
        Classes classes = new Classes(0, className, num, teacher.getId());
        try {
            teacherDao.addClass(classes);
        } catch (SQLException e) {
            System.out.println(ANSI_Red+e.getMessage()+ANSI_RESET);
        }
    }

    public static void viewClass(Scanner scanner, TeacherDaoSql teacherDaoSql,int teacherID) {
        System.out.print("Class ID:");
        int classId = scanner.nextInt();
        int choice = 0;

            try {
                Optional<Classes> classes=teacherDaoSql.getClass(classId,teacherID);
                if(classes.isPresent()) {

                    List<Student> studentList = getClass(classId,teacherDaoSql);
                    do {
                        if (!studentList.isEmpty())
                            System.out.println(ANSI_Green+"1- Add Student\t2- Update Student Grade\t3- Remove Student\t4- Sort By Grade\t5- Sort By Name\t0- Exit"+ANSI_RESET);
                        else System.out.println(ANSI_Green+"1- Add Student \t0- Exit"+ANSI_RESET);
                        choice = scanner.nextInt();
                        int studentID;
                        double grade;
                        if (choice == 1) {
                            teacherDaoSql.getAllStudents(classId);
                            System.out.println("Enter id of the student you'd like to add");
                            studentID = scanner.nextInt();
                            System.out.println("Enter the student's grade");
                            grade = scanner.nextDouble();
                            try {
                                teacherDaoSql.addStudent(studentID, classId, grade);
                                System.out.println("Student added");
                                getClass(classId,teacherDaoSql);
                            } catch (SQLException e) {
                                System.out.println("Could not add student");
                            }
                        } else if (choice == 2) {
                            System.out.print("Student ID:");
                            studentID = scanner.nextInt();
                            try {
                                Optional<Student> found = teacherDaoSql.getStudent(studentID);
                                if (found.isPresent()) {
                                    System.out.print("Grade:");
                                    grade = scanner.nextDouble();
                                    teacherDaoSql.updateStudentGrade(studentID, classId, grade);
                                    getClass(classId,teacherDaoSql);
                                }
                            } catch (SQLException | ResourceNotFoundException e) {
                                System.out.println(ANSI_Red+e.getMessage()+ANSI_RESET);
                            }
                        } else if (choice == 3) {
                            System.out.print("Student ID:");
                            studentID = scanner.nextInt();
                            teacherDaoSql.removeStudent(studentID, classId);
                            getClass(classId,teacherDaoSql);
                        } else if (choice == 4) {
                            teacherDaoSql.sortStudentsByGrade(classId);
                        } else if (choice == 5) {
                            teacherDaoSql.sortStudentsByName(classId);
                        }
                    }while (choice!=0);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } catch (ResourceNotFoundException e){
                System.out.println("Access Denied");
            }

    }
     public static List<Student> getClass(int classId,TeacherDaoSql teacherDaoSql){

         try {
             System.out.print(ANSI_Blue+"Class AVG:") ;
             teacherDaoSql.findAverage(classId);
             System.out.print(ANSI_Cyan+"Class Median:"+ANSI_RESET) ;
             teacherDaoSql.findMedian(classId);
             return teacherDaoSql.getStudents(classId);
         } catch (SQLException e) {
             throw new RuntimeException(e);
         }

     }

}