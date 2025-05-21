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
import com.example.nhom6.ArticleActivity;
import com.example.nhom6.Article;
import com.example.nhom6.DatabaseHelper;
import com.example.nhom6.R;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private List<Article> articleList;
    private Context context;
    private DatabaseHelper dbHelper;

    public ArticleAdapter(List<Article> articleList) {
        this.articleList = articleList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        context = parent.getContext();
        dbHelper = new DatabaseHelper(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.titleTextView.setText(article.getTitle());
        holder.contentTextView.setText(article.getContent());
        holder.createdDateTextView.setText("Ngày tạo: " + article.getCreatedDate());

        // Sự kiện click nút "Sửa"
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    Article currentArticle = articleList.get(currentPosition);
                    Intent intent = new Intent(context, ArticleActivity.class);
                    intent.putExtra("article_id", currentArticle.getId());
                    intent.putExtra("article_title", currentArticle.getTitle());
                    intent.putExtra("article_content", currentArticle.getContent());
                    intent.putExtra("article_image", currentArticle.getImage());
                    intent.putExtra("article_category_id", currentArticle.getCategoryId());
                    intent.putExtra("article_is_featured", currentArticle.isFeatured());
                    context.startActivity(intent);
                }
            }
        });

        // Sự kiện click nút "Xóa"
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    Article currentArticle = articleList.get(currentPosition);
                    new AlertDialog.Builder(context)
                            .setTitle("Xác nhận xóa")
                            .setMessage("Bạn có chắc chắn muốn xóa bài viết \"" + currentArticle.getTitle() + "\" không?")
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int positionToDelete = holder.getAdapterPosition();
                                    if (positionToDelete != RecyclerView.NO_POSITION) {
                                        Article articleToDelete = articleList.get(positionToDelete);
                                        boolean success = dbHelper.deleteArticle(articleToDelete.getId());
                                        if (success) {
                                            articleList.remove(positionToDelete);
                                            notifyItemRemoved(positionToDelete);
                                            notifyItemRangeChanged(positionToDelete, articleList.size());
                                        }
                                    }
                                }
                            })
                            .setNegativeButton("Không", null)
                            .show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;
        TextView createdDateTextView;
        Button editButton;
        Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            createdDateTextView = itemView.findViewById(R.id.createdDateTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}