package com.example.myapplication.menuFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.R;
import com.example.myapplication.model.GameModel;
import com.example.myapplication.purchaseManagement.GamesDetailActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.home_recycler_view);

        //Query
        Query query = firebaseFirestore.collection("games");

        //Recycler options
        FirestoreRecyclerOptions<GameModel> options = new FirestoreRecyclerOptions.Builder<GameModel>()
                .setQuery(query, GameModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<GameModel, GamesViewHolder>(options) {

            @NotNull
            @Override
            public GamesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_game_view, parent, false);
                return new GamesViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull @NotNull GamesViewHolder holder, int position, @NonNull @NotNull GameModel model) {
                holder.gameTitle.setText(model.getName());

                // https://github.com/bumptech/glide
                Glide.with(view).load(model.getCover())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.gameCover);

                holder.gameCover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Toast.makeText(getContext(), holder.getItemId() + " ", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getContext(), getSnapshots().getSnapshot(position).getId()+ " ", Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(getContext(), GamesDetailActivity.class);

                        // https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
                        intent.putExtra("GameObject",model);

                        // How do I get the Firestore document ID when using FirestoreRecyclerAdapter?
                        // https://newbedev.com/how-do-i-get-the-firestore-document-id-when-using-firestorerecycleradapter
                        intent.putExtra("gameId", getSnapshots().getSnapshot(position).getId());

                        startActivity(intent);
                    }
                });
            }
        };


        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }


    // View holder class
    private class GamesViewHolder extends RecyclerView.ViewHolder {

        private final TextView gameTitle;
        private final ImageView gameCover;
        //private final View view;

        public GamesViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            gameCover = itemView.findViewById(R.id.gameCover);
            gameTitle = itemView.findViewById(R.id.gameTitle);
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

// Firebase recycler view
// https://www.youtube.com/watch?v=cBwaJYocb9I