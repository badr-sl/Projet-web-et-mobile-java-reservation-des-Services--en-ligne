package com.example.projectservices;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private static final String TAG = "CommentActivity";

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<CommentData> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        recyclerView = findViewById(R.id.recyclerViewComments);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList);
        recyclerView.setAdapter(commentAdapter);

        // Récupération du token et de l'ID du produit depuis l'intent
        String token = getIntent().getStringExtra("token");
        String productIdString = getIntent().getStringExtra("productId");
        long productId = productIdString != null ? Long.parseLong(productIdString) : -1;


        // Vérification des valeurs reçues
        if (token == null || token.isEmpty() || productId == -1) {
            Toast.makeText(this, "Paramètres invalides", Toast.LENGTH_SHORT).show();
            finish(); // Fermer l'activité en cas de paramètres invalides
        } else {
            fetchCommentsFromApi(token, productId);
        }
    }

    private void fetchCommentsFromApi(String token, long productId) {
        // Construction de l'URL avec les paramètres reçus
        String url = "http://10.0.2.2:9085/services/comments?token=" + token + "&id=" + productId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            JSONObject userObject = jsonObject.getJSONObject("user");
                            JSONObject commentObject = jsonObject.getJSONObject("comment");

                            // Récupération des données du commentaire
                            int commentId = jsonObject.getInt("id");
                            String userName = userObject.getString("userName");
                            String commentText = commentObject.getString("comment");

                            // Création de l'objet CommentData
                            CommentData commentData = new CommentData();
                            commentData.setId(commentId);

                            // Création de l'objet User
                            CommentData.User user = new CommentData.User();
                            user.setId(userObject.getInt("id"));
                            user.setUserName(userName);
                            commentData.setUser(user);

                            // Ajout du commentaire à la liste
                            commentList.add(commentData);
                        }
                        // Mise à jour de l'adaptateur
                        commentAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Erreur lors de la récupération des commentaires: " + error.getMessage());
                        Toast.makeText(CommentActivity.this, "Erreur lors de la récupération des commentaires", Toast.LENGTH_SHORT).show();
                    }
                });

        // Ajout de la requête à la file d'attente Volley
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonArrayRequest);
    }
}
