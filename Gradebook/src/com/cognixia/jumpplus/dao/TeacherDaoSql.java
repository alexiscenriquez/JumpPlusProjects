package com.cognixia.jumpplus.dao;

import com.cognixia.jumpplus.connection.ConnectionManager;
import com.cognixia.jumpplus.exception.ResourceNotFoundException;


import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.cognixia.jumpplus.Main.*;

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
    public boolean addStudent(int studentId, int classId,double grade) throws SQLException {
        PreparedStatement pstmt= conn.prepareStatement("insert into Student_Class values(?,?,?)");
        pstmt.setInt(1,classId);
        pstmt.setInt(2,studentId);
        pstmt.setDouble(3,grade);
       int count= pstmt.executeUpdate();

        return count>0;
    }

    @Override
    public boolean removeStudent(int studentId, int classId) throws SQLException {
        PreparedStatement pstmt=conn.prepareStatement("delete from student_class where student_id=? and class_id=?");
        pstmt.setInt(1,studentId);
        pstmt.setInt(2,classId);
        int count= pstmt.executeUpdate();

        return count>0;
    }

    @Override
    public boolean updateStudentGrade(int studentId, int classId, double grade) throws SQLException{
        PreparedStatement pstmt= conn.prepareStatement("Update Student_Class set grade=? where student_id=? and class_id=?");

        pstmt.setDouble(1,grade);
        pstmt.setInt(2,studentId);
        pstmt.setInt(3,classId);
        int count= pstmt.executeUpdate();

        return count>0;
    }

    @Override
    public List<Student> getStudents(int classId) throws SQLException {
        PreparedStatement pstmt=conn.prepareStatement("SELECT student.id, student.first_name as 'fName',student.last_name as 'lName',grade from student_class join student on student_id=student.id where class_id=?");
       pstmt.setInt(1,classId);
        ResultSet rs= pstmt.executeQuery();
       List<Student>studentList=new ArrayList<>();
       int studentId;
       String fName;
       String lName;
       double grade;
       if(rs.next()) {
           System.out.println(ANSI_Blue+"Students in class");
           System.out.printf("%20s %20s %20s%n", "id", "Name", "Grade");
         do  {
               studentId = rs.getInt("student.id");
               fName = rs.getString("fName");
               lName = rs.getString("lName");
               grade = rs.getDouble("grade");
               Student student = new Student(studentId, fName, lName);
               studentList.add(student);
               System.out.printf("%20s %20s %20s%n", studentId, fName + " " + lName, grade);

           }while (rs.next());
       }
           else { System.out.println(ANSI_Red+"No students in this class"+ANSI_RESET);}

        return studentList;
    }

    @Override
    public Optional<Student> getStudent(int studentID) throws ResourceNotFoundException, SQLException {
        PreparedStatement pstmt = conn.prepareStatement("select * from student where id=?");
        pstmt.setInt(1, studentID);
        ResultSet rs = pstmt.executeQuery();
        Student student;
        if (rs.next()) {
            int studentid = rs.getInt("id");
            String fName = rs.getString("first_name");
            String lName = rs.getString("last_name");
            student = new Student(studentid, fName, lName);
        } else {
            throw new ResourceNotFoundException("Student",studentID);
        }
        return Optional.of(student);

    }

    @Override
    public List<Student> getAllStudents(int classID) throws SQLException {
        PreparedStatement pstmt= conn.prepareStatement("select * from student where id not in(select student_id from student_class where class_id=?);");
       pstmt.setInt(1,classID);
       ResultSet rs=pstmt.executeQuery();
        System.out.printf("%20s %20s%n","id","Name");
        List<Student> studentList=new ArrayList<>();

        while(rs.next()){
            int id=rs.getInt("id");
            String fName=rs.getString("first_name");
            String lName=rs.getString("last_name");
            Student student=new Student(id,fName,lName);
            studentList.add(student);
            System.out.printf("%20s %20s%n",id,fName+" "+lName);
        }
        return null;
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
                System.out.println(ANSI_Red+"You are not assigned to any classses"+ANSI_RESET);
            }
            else {
                System.out.println(ANSI_Blue+"Your Classes");
                System.out.printf("%20s %20s%n","ID","Class");
                for (Classes classes : classList) {
                    System.out.printf("%20s %20s%n", classes.getId(), classes.getName() + " " + classes.getNum());
                }
            }
            return classList;
        }

    @Override
    public Optional<Classes> getClass(int classID, int teacherId) throws SQLException, ResourceNotFoundException {
    PreparedStatement pstmt=conn.prepareStatement("select * from class where id=? and teacher_id=?");
    pstmt.setInt(1,classID);
    pstmt.setInt(2,teacherId);
    ResultSet rs=pstmt.executeQuery();
    Classes classes;
    if(rs.next()){
        int classId=rs.getInt("id");
        int teacherID=rs.getInt("teacher_id");
        String name=rs.getString("name");
        int num=rs.getInt("num");
        classes=new Classes(classID,name,teacherID,num);
    }
    else{
        throw new ResourceNotFoundException("Class",classID);
    }
        return Optional.of(classes);
    }

    @Override
    public void sortStudentsByGrade(int classID) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("SELECT student.id, student.first_name as 'fName',student.last_name as 'lName',grade from student_class join student on student_id=student.id where class_id=? order by grade desc");
        pstmt.setInt(1, classID);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            int studentId = rs.getInt("student.id");
            String fName = rs.getString("fName");
            String lName = rs.getString("lName");
            Double grade = rs.getDouble("grade");
            System.out.printf("%20s %20s %20s%n", studentId, fName + " " + lName, grade);
        }
    }
    @Override
    public void sortStudentsByName(int classID) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("SELECT student.id, student.first_name as 'fName',student.last_name as 'lName',grade from student_class join student on student_id=student.id where class_id=? order by student.first_name,student.last_name");
        pstmt.setInt(1, classID);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            int studentId = rs.getInt("student.id");
            String fName = rs.getString("fName");
            String lName = rs.getString("lName");
            Double grade = rs.getDouble("grade");
            System.out.printf("%20s %20s %20s%n", studentId, fName + " " + lName, grade);
        }

 }
    @Override
    public boolean findAverage(int classID) throws SQLException {
        PreparedStatement pstmt= conn.prepareStatement(" select avg(grade) as avg from student_class where class_id=?");
       pstmt.setInt(1,classID);
        ResultSet rs=pstmt.executeQuery();
        if(rs.next()){
            System.out.println(rs.getDouble("avg"));
            return true;
        }
        return false;
    }

    @Override
    public boolean findMedian(int classID) throws SQLException {
        PreparedStatement pstmt=conn.prepareStatement("SELECT AVG(grade) as median_val\n" +
                "FROM (\n" +
                "SELECT sc.grade, @rownum:=@rownum+1 as `row_number`, @total_rows:=@rownum\n" +
                "  FROM student_class sc, (SELECT @rownum:=0) r\n" +
                "  WHERE sc.class_id=?\n" +
                "  ORDER BY sc.grade\n" +
                ") as med\n" +
                "WHERE med.row_number IN ( FLOOR((@total_rows+1)/2), FLOOR((@total_rows+2)/2) );");

        pstmt.setInt(1,classID);
       ResultSet rs= pstmt.executeQuery();
       if(rs.next()){
           System.out.println( rs.getDouble("median_val"));
           return true;
       }
        return false;
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
