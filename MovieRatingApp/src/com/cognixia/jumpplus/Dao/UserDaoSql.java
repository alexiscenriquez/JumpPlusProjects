package com.cognixia.jumpplus.Dao;

import com.cognixia.jumpplus.Connection.ConnectionManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class UserDaoSql implements UserDao{
private Connection conn;

    @Override
    public boolean getFilms() throws SQLException{

        return false;
    }

    @Override
    public void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
      conn= ConnectionManager.getConnection();
    }

    @Override
    public boolean addUser(User user) throws SQLException {
        try (PreparedStatement psmt = conn.prepareStatement("insert into User values ?,?")) {
            psmt.setString(1,user.getEmail());
            psmt.setString(2,user.getPassword());
           int count= psmt.executeUpdate();
           if(count>0) return true;
        }
        return false;
    }

    @Override
    public Optional<User> authenticateUser(String username, String password) throws SQLException {
        try(PreparedStatement pstmt=conn.prepareStatement("select * from User where username=? and password=?")) {
            pstmt.setString(1,username);
            pstmt.setString(2,password);
        }
        return Optional.empty();
    }


    @Override
    public boolean addRating(int userID, int filmID, int rating) throws SQLException {
        try(PreparedStatement pstmt= conn.prepareStatement("insert into User_Film values(?,?,?)")){
        pstmt.setInt(1,userID);
        pstmt.setInt(2,filmID);
        pstmt.setInt(3,rating);
        int count=pstmt.executeUpdate();
        if(count>0){
            return true;
        }
        }
        return false;
    }
}
