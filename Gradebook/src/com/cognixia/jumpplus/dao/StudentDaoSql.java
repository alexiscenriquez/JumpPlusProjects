package com.cognixia.jumpplus.dao;

import com.cognixia.jumpplus.connection.ConnectionManager;
import com.cognixia.jumpplus.exception.ResourceNotFoundException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class StudentDaoSql implements StudentDao{

Connection conn;
    @Override
    public boolean signUp(Student student) throws SQLException {
        PreparedStatement pstmt= conn.prepareStatement("insert into Student values (null,?,?,?,?)");
        pstmt.setString(1,student.getFirstName());
        pstmt.setString(2,student.getLastName());
        pstmt.setString(3,student.getUsername());
        pstmt.setString(4,student.getPassword());

        int count=pstmt.executeUpdate();

        return count > 0;
    }

    @Override
    public Optional<Student> authenticate(String username, String password) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("select * from Student where username=? and password=?");
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();
        if (!rs.next()) {
            return Optional.empty();
        }
        int id = rs.getInt("id");
        String fName = rs.getString("first_name");
        String lName = rs.getString("last_name");
        Student student = new Student(id, fName, lName);
return Optional.of(student);

    }

    @Override
    public boolean getStudentClasses( int studentID) throws SQLException {
        PreparedStatement pstmt=conn.prepareStatement("SELECT class.name,grade,concat(teacher.first_name ,\" \",teacher.last_name) as tName from student_class join class \n" +
                "on student_class.class_id=class.id join teacher on teacher.id=class.teacher_id\n" +
                "where student_id=?;");
        pstmt.setInt(1,studentID);
        ResultSet rs=pstmt.executeQuery();
        System.out.printf("%20s %20s %20s%n","Class","Teacher","Grade");
        String name;
        String teacher;
        double grade;
        while(rs.next()){
            name=rs.getString("class.name");
            teacher=rs.getString("tName");
            grade=rs.getDouble("grade");
            System.out.printf("%20s %20s %20s%n",name,teacher,grade);
        }
        return false;
    }

    @Override
    public void setConnection() throws SQLException, IOException, ClassNotFoundException {
        conn = ConnectionManager.getConnection();
    }

}
