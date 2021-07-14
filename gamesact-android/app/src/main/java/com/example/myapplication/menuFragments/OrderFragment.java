package com.example.myapplication.menuFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;
import com.example.myapplication.model.GameModel;
import com.example.myapplication.model.PurchaseModel;
import com.example.myapplication.purchaseManagement.GamesDetailActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

public class OrderFragment extends Fragment {

    RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseUser firebaseUser;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        //hooks
        recyclerView = view.findViewById(R.id.order_recycler_view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Query
        // https://stackoverflow.com/questions/50224638/order-firestore-data-by-timestamp-in-ascending-order/50228103
        Query query = firebaseFirestore.collection("orders").whereEqualTo("buyersId", firebaseUser.getUid()).orderBy("timestamp",Query.Direction.DESCENDING);

        //Recycler options
        FirestoreRecyclerOptions<PurchaseModel> options = new FirestoreRecyclerOptions.Builder<PurchaseModel>()
                .setQuery(query, PurchaseModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<PurchaseModel, OrderFragment.OrderViewHolder>(options) {

            @NotNull
            @Override
            public OrderFragment.OrderViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_purchase_history, parent, false);
                return new OrderFragment.OrderViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull @NotNull OrderFragment.OrderViewHolder holder, int position, @NonNull @NotNull PurchaseModel model) {
                holder.productName.setText(model.getProductName());
                holder.userTag.setText(model.getGamerTag());
                holder.price.setText("Rs. " + model.getNepaliPrice());
                if(model.isOrderCompleted()){
                    holder.status.setText("Order: Completed");
                    holder.status.setChipBackgroundColor(AppCompatResources.getColorStateList(getContext(),R.color.green1));
                }
            }
        };

        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        return view;
    }

    // View holder class
    private class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView productName;
        private Chip userTag, price, status;

        public OrderViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.order_title);
            userTag = itemView.findViewById(R.id.chip_user_tag);
            price = itemView.findViewById(R.id.chip_nepali_price);
            status = itemView.findViewById(R.id.chip_order_status);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}