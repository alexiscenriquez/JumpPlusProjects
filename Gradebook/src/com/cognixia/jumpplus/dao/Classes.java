package com.cognixia.jumpplus.dao;

public class Classes {
    private int id;
    private String name;
    private int num;
    private int teacherId;

    public Classes() {
    }

    public Classes(int id, String name, int num, int teacherId) {
        this.id = id;
        this.name = name;
        this.num = num;
        this.teacherId = teacherId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public String toString() {
        return "Class{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", num=" + num +
                ", teacherId=" + teacherId +
                '}';
    }
}
