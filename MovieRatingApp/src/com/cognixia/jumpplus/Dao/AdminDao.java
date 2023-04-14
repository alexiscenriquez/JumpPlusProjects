package com.cognixia.jumpplus.Dao;

import com.cognixia.jumpplus.exceptions.ResourceNotFoundException;

import java.io.IOException;
import java.sql.SQLException;

public interface AdminDao {
    void setConnection() throws  ClassNotFoundException, IOException, SQLException;
    boolean addFilm(Film film) throws SQLException;
    Film getFilm(int id) throws ResourceNotFoundException, SQLException;
    boolean updateFilm(int id,String name) throws SQLException;
   void getFilms() throws SQLException;
}
