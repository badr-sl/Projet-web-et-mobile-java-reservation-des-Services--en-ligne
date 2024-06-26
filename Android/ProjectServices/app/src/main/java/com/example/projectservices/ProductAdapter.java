package com.example.projectservices;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private List<Product> productList;
    private String token;

    public ProductAdapter(Context context, List<Product> productList, String token) {
        this.context = context;
        this.productList = productList;
        this.token = token;
    }

    // Add a new constructor to include the product ID
    public ProductAdapter(Context context, List<Product> productList, String token, long productId) {
        this.context = context;
        this.productList = productList;
        this.token = token;
        // Store or use the product ID here as needed
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("$%s", product.getPrice()));
        holder.productCategory.setText(product.getCategory());

        // Using Glide to load image from a byte array
        if (product.getImage() != null) {
            Glide.with(context)
                    .load(product.getImage())
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.productImage);
        } else {
            holder.productImage.setImageResource(R.drawable.placeholder_image);
        }

        holder.productImage.setOnClickListener(view -> showProductDetails(product));
        holder.btnReserve.setOnClickListener(view -> {
            if (context instanceof ProductActivity) {
                ((ProductActivity) context).generatePDF(product.getName(), product.getPrice(), token);
            }
        });
        holder.btnCommentaire.setOnClickListener(view -> {
            // Add debug logs here
            Log.d("ProductAdapter", "Comment button clicked for product ID: " + product.getId());
            Log.d("ProductAdapter", "Token: " + token);

            // Create an Intent to start the new CommentActivity
            Intent intent = new Intent(context, CommentActivity.class);
            intent.putExtra("token", token);
            intent.putExtra("productId", product.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName, productPrice, productCategory;
        public Button btnReserve;
        public Button btnCommentaire;
        public ImageView productImage;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productCategory = itemView.findViewById(R.id.productCategory);
            btnReserve = itemView.findViewById(R.id.btnReserve);
            productImage = itemView.findViewById(R.id.productImage);
            btnCommentaire = itemView.findViewById(R.id.btnComment);
        }
    }

    private void showProductDetails(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Product Details");
        builder.setMessage("Name: " + product.getName() +
                "\nDescription: " + product.getDescription() +
                "\nPrice: $" + product.getPrice() +
                "\nAvailability: " + product.getAvailability() +
                "\nCategory: " + product.getCategory());
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
