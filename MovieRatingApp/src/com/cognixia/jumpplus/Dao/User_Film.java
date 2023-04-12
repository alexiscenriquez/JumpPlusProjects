package com.cognixia.jumpplus.Dao;

public class User_Film {
private int userID;
private int filmID;
private int rating;

    public User_Film() {
    }

    public User_Film(int userID, int filmID, int rating) {
        this.userID = userID;
        this.filmID = filmID;
        this.rating = rating;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getFilmID() {
        return filmID;
    }

    public void setFilmID(int filmID) {
        this.filmID = filmID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "User_Film{" +
                "userID=" + userID +
                ", filmID=" + filmID +
                ", rating=" + rating +
                '}';
    }
}
