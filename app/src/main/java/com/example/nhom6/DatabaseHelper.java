package com.example.nhom6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "NewsDB.db";
    private static final int DATABASE_VERSION = 1;

    // Bảng Danh mục
    public static final String TABLE_CATEGORIES = "categories";
    public static final String COL_CATEGORY_ID = "id";
    public static final String COL_CATEGORY_NAME = "name";
    public static final String COL_CATEGORY_DESCRIPTION = "description";
    public static final String COL_CATEGORY_CREATED_DATE = "created_date";

    // Bảng Bài viết
    public static final String TABLE_ARTICLES = "articles";
    public static final String COL_ARTICLE_ID = "id";
    public static final String COL_ARTICLE_TITLE = "title";
    public static final String COL_ARTICLE_CONTENT = "content";
    public static final String COL_ARTICLE_IMAGE = "image";
    public static final String COL_ARTICLE_CATEGORY_ID = "category_id";
    public static final String COL_ARTICLE_CREATED_DATE = "created_date";
    public static final String COL_ARTICLE_IS_FEATURED = "is_featured";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCategoriesTable = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                COL_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CATEGORY_NAME + " TEXT NOT NULL, " +
                COL_CATEGORY_DESCRIPTION + " TEXT, " +
                COL_CATEGORY_CREATED_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createCategoriesTable);

        String createArticlesTable = "CREATE TABLE " + TABLE_ARTICLES + " (" +
                COL_ARTICLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ARTICLE_TITLE + " TEXT NOT NULL, " +
                COL_ARTICLE_CONTENT + " TEXT, " +
                COL_ARTICLE_IMAGE + " TEXT, " +
                COL_ARTICLE_CATEGORY_ID + " INTEGER, " +
                COL_ARTICLE_CREATED_DATE + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
                COL_ARTICLE_IS_FEATURED + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY (" + COL_ARTICLE_CATEGORY_ID + ") REFERENCES " +
                TABLE_CATEGORIES + "(" + COL_CATEGORY_ID + "))";
        db.execSQL(createArticlesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    // CRUD Danh mục
    public long addCategory(String name, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CATEGORY_NAME, name);
        values.put(COL_CATEGORY_DESCRIPTION, description);
        return db.insert(TABLE_CATEGORIES, null, values);
    }

    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CATEGORIES, null, null, null, null, null, COL_CATEGORY_NAME);
    }

    // Lấy danh sách Category để dùng cho Spinner
    public List<Category> getCategoryList() {
        List<Category> categoryList = new ArrayList<>();
        Cursor cursor = getAllCategories();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CATEGORY_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY_NAME));
                categoryList.add(new Category(id, name));
            }
            cursor.close();
        }
        return categoryList;
    }

    public int updateCategory(int id, String name, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CATEGORY_NAME, name);
        values.put(COL_CATEGORY_DESCRIPTION, description);
        return db.update(TABLE_CATEGORIES, values, COL_CATEGORY_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteCategory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ARTICLES, COL_ARTICLE_CATEGORY_ID + "=?", new String[]{String.valueOf(id)});
        db.delete(TABLE_CATEGORIES, COL_CATEGORY_ID + "=?", new String[]{String.valueOf(id)});
    }

    // CRUD Bài viết
    public long addArticle(String title, String content, String image, int categoryId, boolean isFeatured) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ARTICLE_TITLE, title);
        values.put(COL_ARTICLE_CONTENT, content);
        values.put(COL_ARTICLE_IMAGE, image);
        values.put(COL_ARTICLE_CATEGORY_ID, categoryId);
        values.put(COL_ARTICLE_IS_FEATURED, isFeatured ? 1 : 0);
        return db.insert(TABLE_ARTICLES, null, values);
    }

    public Cursor getAllArticles() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ARTICLES, null, null, null, null, null, COL_ARTICLE_CREATED_DATE + " DESC");
    }

    public Cursor getArticlesByCategory(int categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ARTICLES, null, COL_ARTICLE_CATEGORY_ID + "=?", new String[]{String.valueOf(categoryId)}, null, null, COL_ARTICLE_CREATED_DATE + " DESC");
    }

    public int updateArticle(int id, String title, String content, String image, int categoryId, boolean isFeatured) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ARTICLE_TITLE, title);
        values.put(COL_ARTICLE_CONTENT, content);
        values.put(COL_ARTICLE_IMAGE, image);
        values.put(COL_ARTICLE_CATEGORY_ID, categoryId);
        values.put(COL_ARTICLE_IS_FEATURED, isFeatured ? 1 : 0);
        return db.update(TABLE_ARTICLES, values, COL_ARTICLE_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteArticle(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ARTICLES, COL_ARTICLE_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Lớp hỗ trợ Category đơn giản, override toString() để hiển thị tên trong Spinner
    public static class Category {
        private int id;
        private String name;

        public Category(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
