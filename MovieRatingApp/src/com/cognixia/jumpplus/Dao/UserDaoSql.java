package com.cognixia.jumpplus.Dao;

import com.cognixia.jumpplus.Connection.ConnectionManager;
import com.cognixia.jumpplus.exceptions.ResourceNotFoundException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDaoSql implements UserDao {
    private Connection conn;

    @Override
    public boolean displayFilms() throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("""
                Select name,id,coalesce(avg(rating) ,'N/A')As 'Avg', count(rating) AS count from user_film
                right join film on user_film.film_id=film.id
                group by film.id;""")) {
            ResultSet rs = pstmt.executeQuery();

            System.out.printf("%20s %20s %20s %20s", "#", "Film", "Avg Rating", "# of Ratings");
            System.out.println("\n--------------------------------------------------------------------------");
            while (rs.next()) {
                String name = rs.getString("name");
                String avg = rs.getString("Avg");
                int count = rs.getInt("count");
                int id = rs.getInt("id");
                System.out.printf("%20s %20s %20s %20s%n", id, name, avg, count);
            }


        }
        return false;
    }

    @Override
    public Film getFilm(int id) throws SQLException, ResourceNotFoundException {
        Film film;
        try (PreparedStatement pstmt = conn.prepareStatement("select * from film where id =?")) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next())
                throw new ResourceNotFoundException(id);

            int filmid = rs.getInt("id");
            String name = rs.getString("name");


            film = new Film(filmid, name);
            return film;

        }

    }


    @Override
    public void setConnection() throws  ClassNotFoundException, IOException, SQLException {
        conn = ConnectionManager.getConnection();
    }

    @Override
    public boolean addUser(User user) throws SQLException {
        try (PreparedStatement psmt = conn.prepareStatement("insert into User values (null,?,?,?)")) {
            psmt.setString(1, user.getEmail());
            psmt.setString(2, user.getPassword());
            psmt.setInt(3, user.getRole());
            int count = psmt.executeUpdate();
            if (count > 0) return true;
        }
        return false;
    }

    @Override
    public Optional<User> authenticateUser(String emaila, String password) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("select * from User where email=? and password=?")) {
            pstmt.setString(1, emaila);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }
            System.out.println(rs.getInt("id") + rs.getString("email"));


            int id = rs.getInt("id");
            String email = rs.getString("email");
            String pwd = rs.getString("password");
            int role = rs.getInt("role");
            User user = new User(id, email, pwd, role);
            return Optional.of(user);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }


    @Override
    public boolean addRating(int userID, int filmID, int rating) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("insert into User_Film values(?,?,?)")) {
            pstmt.setInt(1, userID);
            pstmt.setInt(2, filmID);
            pstmt.setInt(3, rating);
            int count = pstmt.executeUpdate();
            if (count > 0) {
                return true;
            }
        }
        return false;
    }
}
