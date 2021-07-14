package com.example.myapplication.purchaseManagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;
import com.example.myapplication.model.GameModel;
import com.example.myapplication.utils.GamesPricesAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

public class GamesDetailActivity extends AppCompatActivity {

    private TextView gameName, purchaseTitle;
    private ImageView gameCover;
    private GameModel gameModel;
    private String gameId;
    private RecyclerView recyclerView;

    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_detail);

        gameModel =(GameModel) getIntent().getSerializableExtra("GameObject");
        gameId = (String) getIntent().getStringExtra("gameId");

        gameName = findViewById(R.id.detail_game_name);
        gameCover = findViewById(R.id.detail_cover);
        purchaseTitle = findViewById(R.id.detail_title);
        recyclerView = findViewById(R.id.detail_prices_recycler_view);

        gameName.setText(gameModel.getName());
        purchaseTitle.setText("Buy " + gameModel.getName() + " " + gameModel.getCurrency());
        // https://github.com/bumptech/glide
        Glide.with(this).load(gameModel.getCover())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(gameCover);


        firebaseFirestore.getInstance();

        displayPrices();

    }

    private void displayPrices() {
        GamesPricesAdapter adapter = new GamesPricesAdapter(gameModel.getPrices(), gameModel.getCurrency(), gameModel, this, gameId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    public void exitActivity(View view) {
        finish();
    }
}