package com.example.chipopoproject.utils;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chipopoproject.R;
import com.example.chipopoproject.models.CustomeAdapter;
import com.example.chipopoproject.models.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fireStore extends Fragment {
    private FirebaseFirestore db;
    private RecyclerView resView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        resView = view.findViewById(R.id.resView);
        resView.setLayoutManager(new LinearLayoutManager(getContext()));

        // לדוגמה: קריאה לפונקציה שמביאה מוצרים
        fetchProducts();
    }

    private void addProduct(String name, int quantity, double price) {
        Map<String, Object> product = new HashMap<>();
        product.put("name", name);
        product.put("quantity", quantity);
        product.put("price", price);

        db.collection("products")
                .add(product)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "Product added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w("Firestore", "Error adding product", e);
                });
    }

    private void fetchProducts() {
        db.collection("products")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Product> productList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Product product = document.toObject(Product.class);
                        productList.add(product);
                    }
                    updateRecyclerView((ArrayList<Product>) productList);
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Error fetching products", e));
    }

    private void updateRecyclerView(ArrayList<Product> productList) {
        CustomeAdapter adapter = new CustomeAdapter(productList);
        resView.setAdapter(adapter);
    }

}