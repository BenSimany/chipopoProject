package com.example.chipopoproject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.chipopoproject.R;
import com.example.chipopoproject.models.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addProductPage extends Fragment {

    private EditText etProductName, etProductQuantity, etProductPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product_page, container, false);

        etProductName = view.findViewById(R.id.etProductName);
        etProductQuantity = view.findViewById(R.id.etProductQuantity);
        etProductPrice = view.findViewById(R.id.etProductPrice);

        Button buttonAddProduct = view.findViewById(R.id.buttonAddProduct);

        buttonAddProduct.setOnClickListener(v -> {
            String productName = etProductName.getText().toString();
            String productQuantity = etProductQuantity.getText().toString();
            String productPrice = etProductPrice.getText().toString();

            if (productName.isEmpty() || productQuantity.isEmpty() || productPrice.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String phone = sharedPreferences.getString("phone", null);

            if (phone != null) {
                DatabaseReference userProductsRef = FirebaseDatabase.getInstance().getReference("users").child(phone).child("Products");
                String productId = userProductsRef.push().getKey();

                if (productId != null) {
                    Product product = new Product(productName, Integer.parseInt(productQuantity), Double.parseDouble(productPrice));
                    userProductsRef.child(productId).setValue(product)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Product added successfully!", Toast.LENGTH_SHORT).show();
                                etProductName.setText("");
                                etProductQuantity.setText("");
                                etProductPrice.setText("");
                                Navigation.findNavController(v).navigate(R.id.action_addPage_to_mainPage);
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(getContext(), "Failed to add product: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                            );
                }
            }
        });

        return view;
    }
}
