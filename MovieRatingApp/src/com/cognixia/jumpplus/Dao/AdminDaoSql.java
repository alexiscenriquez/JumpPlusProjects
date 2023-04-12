package com.cognixia.jumpplus.Dao;

import com.cognixia.jumpplus.Connection.ConnectionManager;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminDaoSql implements AdminDao{
    Connection conn;
    @Override
    public void setConnection() throws FileNotFoundException, ClassNotFoundException, IOException, SQLException {
        conn=ConnectionManager.getConnection();
    }

    @Override
    public boolean addFilm(Film film) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("insert into Film values(null,?)")) {
            pstmt.setString(1,film.getName());
        }

        return false;
    }

    @Override
    public boolean updateFilm(int id,String name) throws SQLException {
        try(PreparedStatement pstmt=conn.prepareStatement("update Film set name=? where id=?")){
            pstmt.setString(1,name);
            pstmt.setInt(2,id);
           int count= pstmt.executeUpdate();
           if(count>0)return true;
        }
        return false;
    }



















}
