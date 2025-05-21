package com.example.nhom6;

public class Category {
    private int id;
    private String name;
    private String description;
    private String createdDate;

    public Category(int id, String name, String description, String createdDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    @Override
    public String toString() {
        return name; // Hiển thị tên danh mục trong Spinner
    }
}