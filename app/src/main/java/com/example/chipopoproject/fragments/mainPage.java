package com.example.chipopoproject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chipopoproject.R;
import com.example.chipopoproject.models.CustomeAdapter;
import com.example.chipopoproject.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class mainPage extends Fragment {

    private RecyclerView recyclerView;
    private CustomeAdapter adapter;
    private ArrayList<Product> productList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, container, false);

        TextView nameText = view.findViewById(R.id.nameText); // השם שמופיע מעל הרשימה
        recyclerView = view.findViewById(R.id.resView);

        // אתחול רשימת המוצרים
        productList = new ArrayList<>();
        adapter = new CustomeAdapter(productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // שליפת שם המשתמש המחובר
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail(); // קבלת האימייל של המשתמש המחובר
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

            // חיפוש המשתמש לפי אימייל
            usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String userName = snapshot.child("name").getValue(String.class);
                            if (userName != null) {
                                nameText.setText(userName + "'s list");
                            }
                        }
                    } else {
                        nameText.setText("User's list");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Failed to load user name: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            nameText.setText("User's list");
        }

        // שליפת מוצרים
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", null);
        if (phone != null) {
            DatabaseReference userProductsRef = FirebaseDatabase.getInstance().getReference("users").child(phone).child("Products");
            userProductsRef.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Product product = snapshot.getValue(Product.class);
                    if (product != null) {
                        productList.add(product);
                        adapter.notifyItemInserted(productList.size() - 1);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        TextView logoutIcon = view.findViewById(R.id.buttonLogout);
        logoutIcon.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_mainPage_to_loginPage));

        TextView addIcon = view.findViewById(R.id.buttonAdd);
        addIcon.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_mainPage_to_addProductPage));

        return view;
    }
}
