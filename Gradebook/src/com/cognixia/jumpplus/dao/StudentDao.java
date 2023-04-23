package com.cognixia.jumpplus.dao;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public interface StudentDao {

    boolean signUp(Student student) throws SQLException;

    Optional<Student> authenticate(String username, String password) throws SQLException;

    boolean getStudentClasses(int studentID) throws SQLException;

    void  setConnection() throws SQLException, IOException, ClassNotFoundException;

}
