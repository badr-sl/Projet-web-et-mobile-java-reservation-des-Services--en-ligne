package com.example.projectservices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context context;
    private List<CommentData> comments;

    public CommentAdapter(Context context, List<CommentData> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Utilisation du layout XML correct pour chaque élément du RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentData comment = comments.get(position);
        holder.txtUserName.setText(comment.getUser().getUserName());
        holder.txtComment.setText(comment.getComment());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    // ViewHolder pour le layout du commentaire
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtUserName;
        public TextView txtComment;

        public ViewHolder(View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtComment = itemView.findViewById(R.id.txtComment);
        }
    }
}
