package com.example.nhom6;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tắt tiêu đề (ActionBar)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_category);

        dbHelper = new DatabaseHelper(this);
        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();

                if (name.isEmpty()) {
                    Toast.makeText(CategoryActivity.this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
                    return;
                }

                long result = dbHelper.addCategory(name, description);
                if (result != -1) {
                    Toast.makeText(CategoryActivity.this, "Thêm danh mục thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CategoryActivity.this, "Thêm danh mục thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}