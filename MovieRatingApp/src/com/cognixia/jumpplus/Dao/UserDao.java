package com.cognixia.jumpplus.Dao;

import com.cognixia.jumpplus.exceptions.ResourceNotFoundException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public interface UserDao {
    boolean displayFilms() throws SQLException;
    Film getFilm(int id) throws ResourceNotFoundException, SQLException;
    void setConnection() throws ClassNotFoundException, IOException, SQLException;
     boolean addUser(User user) throws SQLException;
    Optional<User> authenticateUser(String username, String password) throws SQLException;

    boolean addRating(int userId, int filmId, int rating) throws SQLException;
}
