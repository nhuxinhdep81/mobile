package com.example.nhom6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "news_db";
    private static final int DATABASE_VERSION = 1;

    // Bảng Category
    public static final String TABLE_CATEGORY = "category";
    public static final String COL_CATEGORY_ID = "id";
    public static final String COL_CATEGORY_NAME = "name";
    public static final String COL_CATEGORY_DESCRIPTION = "description";
    public static final String COL_CATEGORY_CREATED_DATE = "created_date";

    // Bảng Article
    public static final String TABLE_ARTICLE = "article";
    public static final String COL_ARTICLE_ID = "id";
    public static final String COL_ARTICLE_TITLE = "title";
    public static final String COL_ARTICLE_CONTENT = "content";
    public static final String COL_ARTICLE_IMAGE = "image";
    public static final String COL_ARTICLE_CATEGORY_ID = "category_id";
    public static final String COL_ARTICLE_CREATED_DATE = "created_date";
    public static final String COL_ARTICLE_IS_FEATURED = "is_featured";

    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + " (" +
            COL_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_CATEGORY_NAME + " TEXT, " +
            COL_CATEGORY_DESCRIPTION + " TEXT, " +
            COL_CATEGORY_CREATED_DATE + " TEXT)";

    private static final String CREATE_TABLE_ARTICLE = "CREATE TABLE " + TABLE_ARTICLE + " (" +
            COL_ARTICLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_ARTICLE_TITLE + " TEXT, " +
            COL_ARTICLE_CONTENT + " TEXT, " +
            COL_ARTICLE_IMAGE + " TEXT, " +
            COL_ARTICLE_CATEGORY_ID + " INTEGER, " +
            COL_ARTICLE_CREATED_DATE + " TEXT, " +
            COL_ARTICLE_IS_FEATURED + " INTEGER, " +
            "FOREIGN KEY(" + COL_ARTICLE_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + COL_CATEGORY_ID + "))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_ARTICLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(db);
    }

    public long addCategory(String name, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CATEGORY_NAME, name);
        values.put(COL_CATEGORY_DESCRIPTION, description);
        values.put(COL_CATEGORY_CREATED_DATE, getCurrentDate());
        long result = db.insert(TABLE_CATEGORY, null, values);
        db.close();
        return result;
    }

    public boolean updateCategory(int id, String name, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CATEGORY_NAME, name);
        values.put(COL_CATEGORY_DESCRIPTION, description);
        int rowsAffected = db.update(TABLE_CATEGORY, values, COL_CATEGORY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteCategory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ARTICLE, COL_ARTICLE_CATEGORY_ID + "=?", new String[]{String.valueOf(id)});
        int rowsAffected = db.delete(TABLE_CATEGORY, COL_CATEGORY_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CATEGORY, null);
    }

    public List<Category> getCategoryList() {
        List<Category> categoryList = new ArrayList<>();
        Cursor cursor = getAllCategories();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CATEGORY_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY_DESCRIPTION));
            String createdDate = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY_CREATED_DATE));
            categoryList.add(new Category(id, name, description, createdDate));
        }
        cursor.close();
        return categoryList;
    }

    public long addArticle(String title, String content, String image, int categoryId, boolean isFeatured) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ARTICLE_TITLE, title);
        values.put(COL_ARTICLE_CONTENT, content);
        values.put(COL_ARTICLE_IMAGE, image);
        values.put(COL_ARTICLE_CATEGORY_ID, categoryId);
        values.put(COL_ARTICLE_CREATED_DATE, getCurrentDate());
        values.put(COL_ARTICLE_IS_FEATURED, isFeatured ? 1 : 0);
        long result = db.insert(TABLE_ARTICLE, null, values);
        db.close();
        return result;
    }

    public boolean updateArticle(int id, String title, String content, String image, int categoryId, boolean isFeatured) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ARTICLE_TITLE, title);
        values.put(COL_ARTICLE_CONTENT, content);
        values.put(COL_ARTICLE_IMAGE, image);
        values.put(COL_ARTICLE_CATEGORY_ID, categoryId);
        values.put(COL_ARTICLE_IS_FEATURED, isFeatured ? 1 : 0);
        int rowsAffected = db.update(TABLE_ARTICLE, values, COL_ARTICLE_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteArticle(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_ARTICLE, COL_ARTICLE_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public Cursor getAllArticles() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ARTICLE, null);
    }

    public Cursor getArticlesByCategory(int categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ARTICLE + " WHERE " + COL_ARTICLE_CATEGORY_ID + "=?", new String[]{String.valueOf(categoryId)});
    }

    public int getArticleCountByCategory(int categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_ARTICLE + " WHERE " + COL_ARTICLE_CATEGORY_ID + "=?", new String[]{String.valueOf(categoryId)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    private String getCurrentDate() {
        return "2025-05-21";
    }
}