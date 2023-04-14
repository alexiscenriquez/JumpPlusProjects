package com.cognixia.jumpplus.Dao;

import com.cognixia.jumpplus.exceptions.ResourceNotFoundException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public interface UserDao {
    public boolean displayFilms() throws SQLException;
    Film getFilm(int id) throws ResourceNotFoundException, SQLException;
    public void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException;
     boolean addUser(User user) throws SQLException;
    Optional<User> authenticateUser(String username, String password) throws SQLException;

    boolean addRating(int userId, int filmId, int rating) throws SQLException;
}
