package com.example.nhom6;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom6.adapters.ArticleAdapter;
import com.example.nhom6.adapters.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private DatabaseHelper dbHelper;
    private RecyclerView categoryRecyclerView, articleRecyclerView;
    private CategoryAdapter categoryAdapter;
    private ArticleAdapter articleAdapter;
    private List<Category> categoryList;
    private List<Article> articleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkLocationPermission();

        dbHelper = new DatabaseHelper(this);
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        articleRecyclerView = findViewById(R.id.articleRecyclerView);
        Button addCategoryButton = findViewById(R.id.addCategoryButton);
        Button addArticleButton = findViewById(R.id.addArticleButton);

        categoryList = new ArrayList<>();
        articleList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, categoryList, new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(int categoryId) {
                loadArticlesByCategory(categoryId);
            }

            @Override
            public void onCategoryDelete(int categoryId) {
                boolean success = dbHelper.deleteCategory(categoryId);
                if (success) {
                    loadCategories();
                    loadArticles();
                }
            }
        });
        articleAdapter = new ArticleAdapter(articleList);

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        articleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryRecyclerView.setAdapter(categoryAdapter);
        articleRecyclerView.setAdapter(articleAdapter);

        loadCategories();
        loadArticles();

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CategoryActivity.class));
            }
        });

        addArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ArticleActivity.class));
            }
        });
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadCategories();
                loadArticles();
            }
        }
    }

    private void loadCategories() {
        categoryList.clear();
        Cursor cursor = dbHelper.getAllCategories();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY_DESCRIPTION));
            String createdDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY_CREATED_DATE));
            categoryList.add(new Category(id, name, description, createdDate));
        }
        cursor.close();
        categoryAdapter.notifyDataSetChanged();
    }

    private void loadArticles() {
        articleList.clear();
        Cursor cursor = dbHelper.getAllArticles();
        while (cursor.moveToNext()) {
            articleList.add(new Article(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ARTICLE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ARTICLE_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ARTICLE_CONTENT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ARTICLE_IMAGE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ARTICLE_CATEGORY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ARTICLE_CREATED_DATE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ARTICLE_IS_FEATURED)) == 1
            ));
        }
        cursor.close();
        articleAdapter.notifyDataSetChanged();
    }

    private void loadArticlesByCategory(int categoryId) {
        articleList.clear();
        Cursor cursor = dbHelper.getArticlesByCategory(categoryId);
        while (cursor.moveToNext()) {
            articleList.add(new Article(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ARTICLE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ARTICLE_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ARTICLE_CONTENT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ARTICLE_IMAGE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ARTICLE_CATEGORY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ARTICLE_CREATED_DATE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ARTICLE_IS_FEATURED)) == 1
            ));
        }
        cursor.close();
        articleAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCategories();
        loadArticles();
    }
}