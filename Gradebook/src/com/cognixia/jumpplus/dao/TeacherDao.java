package com.cognixia.jumpplus.dao;


import com.cognixia.jumpplus.exception.ResourceNotFoundException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TeacherDao {
    void  setConnection() throws SQLException, IOException, ClassNotFoundException;
    Optional<Teacher> authenticateTeacher(String username, String password) throws SQLException;
    boolean addTeacher(Teacher teacher) throws SQLException;
    boolean addStudent(int studentId, int classId,double grade) throws SQLException;
    boolean removeStudent(int studentId, int classId) throws SQLException;
    boolean updateStudentGrade(int studentId, int classId,double grade) throws SQLException;
    List<Student> getStudents(int classId) throws SQLException;
   Optional<Student> getStudent(int studentID) throws ResourceNotFoundException, SQLException;
    List<Student>getAllStudents(int classID) throws SQLException;
    List<Classes> getClasses(int teacherId) throws SQLException;
    Optional<Classes>getClass(int classID,int teacherId) throws SQLException,ResourceNotFoundException;
    void sortStudentsByGrade(int classID) throws SQLException;
    void sortStudentsByName(int classID) throws SQLException;
    boolean findAverage(int classID) throws SQLException;
    boolean findMedian(int classID) throws SQLException;
    boolean addClass(Classes classes) throws SQLException;

}
