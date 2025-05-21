package com.example.nhom6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CategoryActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText nameEditText, descriptionEditText;
    private Button saveButton;
    private int categoryId = -1; // -1 nghĩa là thêm mới, >0 nghĩa là chỉnh sửa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_category);

        dbHelper = new DatabaseHelper(this);
        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        saveButton = findViewById(R.id.saveButton);

        // Kiểm tra chế độ chỉnh sửa
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("category_id")) {
            categoryId = intent.getIntExtra("category_id", -1);
            String name = intent.getStringExtra("category_name");
            String description = intent.getStringExtra("category_description");
            nameEditText.setText(name);
            descriptionEditText.setText(description);
            saveButton.setText("Cập nhật");
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();

                if (name.isEmpty()) {
                    Toast.makeText(CategoryActivity.this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (categoryId == -1) {
                    // Thêm mới danh mục
                    long result = dbHelper.addCategory(name, description);
                    if (result != -1) {
                        Toast.makeText(CategoryActivity.this, "Thêm danh mục thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CategoryActivity.this, "Thêm danh mục thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Cập nhật danh mục
                    boolean success = dbHelper.updateCategory(categoryId, name, description);
                    if (success) {
                        Toast.makeText(CategoryActivity.this, "Cập nhật danh mục thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CategoryActivity.this, "Cập nhật danh mục thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}