package com.example.chipopoproject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chipopoproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginPage extends Fragment {

    private FirebaseAuth mAuth;
    private EditText textEmail, textPassword;

    public loginPage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance(); // אתחול Firebase Authentication
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_page, container, false);

        // אתחול תצוגות
        textEmail = view.findViewById(R.id.TextEmailAddress);
        textPassword = view.findViewById(R.id.TextPassword);
        Button buttonRegister = view.findViewById(R.id.buttonRegister);
        Button buttonLogin = view.findViewById(R.id.buttonLogin);

        // מאזין ללחיצה על כפתור רישום
        buttonRegister.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_mainPage_to_registerPage);
        });

        buttonLogin.setOnClickListener(v -> {
            String email = textEmail.getText().toString().trim();
            String password = textPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // שמירת נתוני המשתמש ב-SharedPreferences
                                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email", email);
                                editor.apply();

                                Toast.makeText(getContext(), "Login successful!", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.action_mainPage_to_calenderPage);
                            }
                        } else {
                            String errorMessage = "Login failed";
                            if (task.getException() != null) {
                                String errorCode = task.getException().getMessage();
                                if (errorCode.contains("password is invalid")) {
                                    errorMessage = "Wrong password";
                                } else if (errorCode.contains("no user record")) {
                                    errorMessage = "User not found";
                                } else if (errorCode.contains("badly formatted")) {
                                    errorMessage = "Invalid email format";
                                } else if (errorCode.contains("network error")) {
                                    errorMessage = "Network error, check your connection";
                                }
                            }
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
        });


        return view;
    }
}
