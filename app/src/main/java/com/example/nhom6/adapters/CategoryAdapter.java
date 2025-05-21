package com.example.nhom6.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nhom6.Category;
import com.example.nhom6.CategoryActivity;
import com.example.nhom6.DatabaseHelper;
import com.example.nhom6.R;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> categoryList;
    private OnCategoryClickListener listener;
    private Context context;
    private DatabaseHelper dbHelper;

    public interface OnCategoryClickListener {
        void onCategoryClick(int categoryId);
        void onCategoryDelete(int categoryId);
    }

    public CategoryAdapter(Context context, List<Category> categoryList, OnCategoryClickListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
        this.dbHelper = new DatabaseHelper(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.nameTextView.setText(category.getName());
        holder.descriptionTextView.setText(category.getDescription());
        holder.createdDateTextView.setText("Ngày tạo: " + category.getCreatedDate());

        // Hiển thị số lượng tin tức
        int articleCount = dbHelper.getArticleCountByCategory(category.getId());
        holder.articleCountTextView.setText("(" + articleCount + " bài)");

        // Sự kiện click vào toàn bộ mục để xem bài viết
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCategoryClick(category.getId());
            }
        });

        // Sự kiện click nút "Sửa"
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("category_id", category.getId());
                intent.putExtra("category_name", category.getName());
                intent.putExtra("category_description", category.getDescription());
                context.startActivity(intent);
            }
        });

        // Sự kiện click nút "Xóa"
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa danh mục \"" + category.getName() + "\" không?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onCategoryDelete(category.getId());
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        TextView createdDateTextView;
        TextView articleCountTextView;
        Button editButton;
        Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            createdDateTextView = itemView.findViewById(R.id.createdDateTextView);
            articleCountTextView = itemView.findViewById(R.id.articleCountTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}