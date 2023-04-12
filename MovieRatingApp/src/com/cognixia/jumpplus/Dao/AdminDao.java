package com.cognixia.jumpplus.Dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public interface AdminDao {
    void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException;
    boolean addFilm(Film film) throws SQLException;
    boolean updateFilm(int id,String name) throws SQLException;
}
