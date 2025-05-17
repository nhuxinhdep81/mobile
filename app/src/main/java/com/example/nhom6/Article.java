package com.example.nhom6;

public class Article {
    private int id;
    private String title;
    private String content;
    private String image;
    private int categoryId;
    private String createdDate;
    private boolean isFeatured;

    public Article(int id, String title, String content, String image, int categoryId, String createdDate, boolean isFeatured) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.categoryId = categoryId;
        this.createdDate = createdDate;
        this.isFeatured = isFeatured;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public boolean isFeatured() {
        return isFeatured;
    }
}