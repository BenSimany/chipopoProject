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

        // מאזין ללחיצה על כפתור התחברות
        buttonLogin.setOnClickListener(v -> {
            String email = textEmail.getText().toString().trim();
            String password = textPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // התחברות עם Firebase Authentication
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
                            Toast.makeText(getContext(), "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        return view;
    }
}
