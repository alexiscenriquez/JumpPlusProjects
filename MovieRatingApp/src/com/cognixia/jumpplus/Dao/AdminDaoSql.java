package com.cognixia.jumpplus.Dao;

import com.cognixia.jumpplus.Connection.ConnectionManager;
import com.cognixia.jumpplus.exceptions.ResourceNotFoundException;

import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDaoSql implements AdminDao{
    Connection conn;
    @Override
    public void setConnection() throws  ClassNotFoundException, IOException, SQLException {
        conn=ConnectionManager.getConnection();
    }

    @Override
    public boolean addFilm(Film film) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("insert into Film values(null,?)")) {
            pstmt.setString(1,film.getName());
           int count= pstmt.executeUpdate();
           if(count>0){
               return true;
           }
        }

        return false;
    }

    @Override
    public Film getFilm(int id) throws ResourceNotFoundException, SQLException {
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
    public boolean updateFilm(int id,String name) throws SQLException {
        try(PreparedStatement pstmt=conn.prepareStatement("update Film set name=? where id=?")){
            pstmt.setString(1,name);
            pstmt.setInt(2,id);
           int count= pstmt.executeUpdate();
           if(count>0)return true;
        }
        return false;
    }

    @Override
    public void getFilms() throws SQLException {
        try(PreparedStatement pstmt= conn.prepareStatement("SELECT * FROM FILM")){
            ResultSet rs = pstmt.executeQuery();
            System.out.printf("%10s %10s","ID","Title");
            System.out.println("\n----------------------");
             while(rs.next()){
                 int id=rs.getInt("id");
                 String name=rs.getString("name");
                 System.out.printf("%10d %10s%n",id,name);
             }
        }

    }


}
