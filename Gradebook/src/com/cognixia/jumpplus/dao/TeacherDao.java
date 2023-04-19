package com.cognixia.jumpplus.dao;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TeacherDao {
    void  setConnection() throws SQLException, IOException, ClassNotFoundException;
    Optional<Teacher> authenticateTeacher(String username, String password) throws SQLException;
    boolean addTeacher(Teacher teacher) throws SQLException;
    boolean addStudent(int studentId, int classId);
    boolean removeStudent(int studentId, int classId);
    boolean updateStudentGrade(int studentId, int classId);
    void getStudents(int classId);
    List<Classes> getClasses(int teacherId) throws SQLException;
    boolean addClass(Classes classes) throws SQLException;

}
