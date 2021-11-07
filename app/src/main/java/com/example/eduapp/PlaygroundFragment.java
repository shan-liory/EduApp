package com.example.eduapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class PlaygroundFragment extends BaseFragment {

    MaterialCardView card1, card2, card3;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();
    static int point = 0;
    int unlock = 0;

    private class Card {

        MaterialCardView card;
        boolean unlock;
        int required_points;
        String title;
        ImageView lock;

        public Card(MaterialCardView card, int required_points, String title, ImageView lock) {
            this.card = card;
            unlock = false;
            this.required_points = required_points;
            this.title = title;
            this.lock = lock;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playground, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        card1 = view.findViewById(R.id.card);
        card2 = view.findViewById(R.id.card2);
        card3 = view.findViewById(R.id.card3);
        ArrayList<Card> cardlist = new ArrayList<>();


        Card c1 = new Card(
                card1,
                2,
                getResources().getString(R.string.playground_card1),
                view.findViewById(R.id.lock_card1)
                );
        Card c2 = new Card(
                card2,
                3,
                getResources().getString(R.string.playground_card2),
                view.findViewById(R.id.lock_card2)
        );
        Card c3 = new Card(
                card3,
                8,
                getResources().getString(R.string.playground_card3),
                view.findViewById(R.id.lock_card3)
        );

        cardlist.add(c1);
        cardlist.add(c2);
        cardlist.add(c3);

        retrieveData();

        // Check the status, and set the display of play and unlock buttons
        for(Card c: cardlist){
            c.card.setStrokeWidth(0);
            c.card.setClickable(true);
        }

        //
        for(int i =0; i < unlock; i++){
            Card c = cardlist.get(i);
            c.lock.setVisibility(View.INVISIBLE);
            c.unlock = true;
        }

        // for those still be locked
        for(Card c:cardlist){
            if (c.lock.getVisibility() == View.VISIBLE){
                String message = "You need to reach " + c.required_points + " score to unlock " + c.title;
                c.card.setOnClickListener(v -> Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show());
            }
        }



    }


    public void retrieveData (){
        documentReference = db.collection("User").document(currentUserId);
        documentReference.get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().exists()) {
                        if (null != task.getResult().getString("score"))
                            point = Integer.valueOf(task.getResult().getString("score"));
                    } else {
                        Toast.makeText(getContext(), "No data!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}