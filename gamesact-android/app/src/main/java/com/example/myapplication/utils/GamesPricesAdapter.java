package com.example.myapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.GameModel;
import com.example.myapplication.purchaseManagement.PurchaseActivity;

import org.jetbrains.annotations.NotNull;
import java.util.List;

// Android RecyclerView List Example
// https://www.javatpoint.com/android-recyclerview-list-example


public class GamesPricesAdapter extends RecyclerView.Adapter<GamesPricesAdapter.ViewHolder> {

    private List<String> pricesList;
    private String currency;
    private GameModel gameModel;
    private Context context;
    private String gameId;

    public GamesPricesAdapter(List<String> pricesList, String currency, GameModel gameModel, Context context, String gameId) {
        this.pricesList = pricesList;
        this.currency = currency;
        this.gameModel = gameModel;
        this.context = context;
        this.gameId = gameId;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.layout_games_prices, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        String prices = pricesList.get(position);

        // How to split a string in Java
        // https://stackoverflow.com/questions/3481828/how-to-split-a-string-in-java#:~:text=which%20means%20%22any%20character%22%20in,quote(%22.%22))%20.
        String[] comboPrices = prices.split(":");
        String itemValue = comboPrices[0];
        String nepaliPrice = comboPrices[1];

        holder.priceText.setText(itemValue + " " + currency);
        holder.purchaseButton.setText("BUY FOR Rs. " + nepaliPrice);
        holder.purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PurchaseActivity.class);
                intent.putExtra("GameObject",gameModel);
                intent.putExtra("pricePosition", position);
                intent.putExtra("gameId", gameId);

                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return pricesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView priceText;
        public Button purchaseButton;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            this.priceText = itemView.findViewById(R.id.prices_package_text);
            this.purchaseButton = itemView.findViewById(R.id.prices_purchase_button);
        }
    }
}
