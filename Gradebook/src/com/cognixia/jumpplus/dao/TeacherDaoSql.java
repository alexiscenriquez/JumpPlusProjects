package com.cognixia.jumpplus.dao;

import com.cognixia.jumpplus.connection.ConnectionManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeacherDaoSql implements TeacherDao {
    Connection conn;

    @Override
    public void setConnection() throws SQLException, IOException, ClassNotFoundException {
        conn = ConnectionManager.getConnection();
    }

    @Override
    public Optional<Teacher> authenticateTeacher(String username, String password) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("select * from Teacher where username=? and password=?");
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();
        if (!rs.next()) {
            return Optional.empty();
        }
        int id = rs.getInt("id");
        String fName = rs.getString("first_name");
        String lName = rs.getString("last_name");
        String uname = rs.getString("username");
        String pwd = rs.getString("password");
        Teacher teacher = new Teacher(id, fName, lName, uname, pwd);
        return Optional.of(teacher);

    }


    @Override
    public boolean addTeacher(Teacher teacher) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("insert into Teacher values(null,?,?,?,?)");
        pstmt.setString(1, teacher.getFirstName());
        pstmt.setString(2, teacher.getLastName());
        pstmt.setString(3, teacher.getUsername());
        pstmt.setString(4, teacher.getPassword());
        int count = pstmt.executeUpdate();
        return count > 0;
    }

    @Override
    public boolean addStudent(int studentId, int classId) {
        return false;
    }

    @Override
    public boolean removeStudent(int studentId, int classId) {
        return false;
    }

    @Override
    public boolean updateStudentGrade(int studentId, int classId) {
        return false;
    }

    @Override
    public void getStudents(int classId) {

    }

    @Override
    public List<Classes> getClasses(int teacherId) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("Select * from class where teacher_id=?");
        pstmt.setInt(1, teacherId);
        ResultSet rs = pstmt.executeQuery();
        int id;
        String name;
        int num;
        List<Classes> classList=new ArrayList<>();

            while (rs.next()) {
                id = rs.getInt("id");
                name = rs.getString("name");
                num = rs.getInt("num");
                Classes teacherclass=new Classes(id,name,num,teacherId);
               classList.add(teacherclass);

            }
            if(classList.isEmpty()){
                System.out.println("You are not assigned to any classses");
            }
            else {
                System.out.printf("%20s %20s%n","ID","Class");
                for(int i=0;i< classList.size();i++){
                    System.out.printf("%20s %20s%n", classList.get(i).getId(),classList.get(i).getName()+ " "+classList.get(i).getNum());
                }
            }
            return classList;
        }

    @Override
    public boolean addClass(Classes classes) throws SQLException {
        PreparedStatement pstmt= conn.prepareStatement("insert into class values(null,?,?,?)");
        pstmt.setInt(1,classes.getTeacherId());
        pstmt.setString(2, classes.getName());
        pstmt.setInt(3,classes.getNum());

        int count=pstmt.executeUpdate();
        return count > 0;
    }

}
