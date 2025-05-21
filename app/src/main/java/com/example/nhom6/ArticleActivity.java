package com.example.nhom6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ArticleActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText titleEditText, contentEditText, imageEditText;
    private Spinner categorySpinner;
    private CheckBox featuredCheckBox;
    private Button saveButton;
    private int articleId = -1; // -1 nghĩa là thêm mới, >0 nghĩa là chỉnh sửa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_article);

        dbHelper = new DatabaseHelper(this);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        imageEditText = findViewById(R.id.imageEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        featuredCheckBox = findViewById(R.id.featuredCheckBox);
        saveButton = findViewById(R.id.saveButton);

        loadCategoriesToSpinner();

        // Kiểm tra chế độ chỉnh sửa
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("article_id")) {
            articleId = intent.getIntExtra("article_id", -1);
            String title = intent.getStringExtra("article_title");
            String content = intent.getStringExtra("article_content");
            String image = intent.getStringExtra("article_image");
            int categoryId = intent.getIntExtra("article_category_id", -1);
            boolean isFeatured = intent.getBooleanExtra("article_is_featured", false);

            titleEditText.setText(title);
            contentEditText.setText(content);
            imageEditText.setText(image);
            featuredCheckBox.setChecked(isFeatured);
            saveButton.setText("Cập nhật");

            // Đặt danh mục đã chọn trong Spinner
            List<Category> categoryList = dbHelper.getCategoryList();
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).getId() == categoryId) {
                    categorySpinner.setSelection(i);
                    break;
                }
            }
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString().trim();
                String content = contentEditText.getText().toString().trim();
                String image = imageEditText.getText().toString().trim();

                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(ArticleActivity.this, "Vui lòng nhập tiêu đề và nội dung", Toast.LENGTH_SHORT).show();
                    return;
                }

                Category selectedCategory = (Category) categorySpinner.getSelectedItem();
                if (selectedCategory == null) {
                    Toast.makeText(ArticleActivity.this, "Vui lòng chọn danh mục", Toast.LENGTH_SHORT).show();
                    return;
                }

                int categoryId = selectedCategory.getId();
                boolean isFeatured = featuredCheckBox.isChecked();

                if (articleId == -1) {
                    // Thêm mới bài viết
                    long result = dbHelper.addArticle(title, content, image, categoryId, isFeatured);
                    if (result != -1) {
                        Toast.makeText(ArticleActivity.this, "Thêm bài viết thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ArticleActivity.this, "Thêm bài viết thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Cập nhật bài viết
                    boolean success = dbHelper.updateArticle(articleId, title, content, image, categoryId, isFeatured);
                    if (success) {
                        Toast.makeText(ArticleActivity.this, "Cập nhật bài viết thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ArticleActivity.this, "Cập nhật bài viết thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void loadCategoriesToSpinner() {
        List<Category> categoryList = dbHelper.getCategoryList();
        if (categoryList.isEmpty()) {
            Toast.makeText(this, "Không có danh mục nào. Vui lòng thêm danh mục trước.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            ArrayAdapter<Category> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, categoryList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(adapter);
        }
    }
}