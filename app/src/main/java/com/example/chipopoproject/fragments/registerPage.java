package com.example.chipopoproject.fragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.chipopoproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registerPage extends Fragment {

    private EditText textName, textEmail, textPassword, textConfirmPassword, textPhone;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_page, container, false);

        auth = FirebaseAuth.getInstance();
        textName = view.findViewById(R.id.nameText);
        textEmail = view.findViewById(R.id.textEmailAddress);
        textPassword = view.findViewById(R.id.textPassword);
        textConfirmPassword = view.findViewById(R.id.textPassword2);
        textPhone = view.findViewById(R.id.textPhone);

        Button registerButton = view.findViewById(R.id.buttonReg);

        registerButton.setOnClickListener(v -> {
            String name = textName.getText().toString().trim();
            String email = textEmail.getText().toString().trim();
            String password = textPassword.getText().toString().trim();
            String confirmPassword = textConfirmPassword.getText().toString().trim();
            String phone = textPhone.getText().toString().trim();

            // בדיקת שדות ריקים
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // ולידציה למייל
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
                return;
            }

            // ולידציה לסיסמה
            if (password.length() < 6) {
                Toast.makeText(getContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // ולידציה לטלפון
            if (phone.length() != 10 || !phone.matches("\\d+")) {
                Toast.makeText(getContext(), "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // שמירת נתוני המשתמש ב-Realtime Database
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(phone);
                            userRef.child("name").setValue(name);
                            userRef.child("email").setValue(email);

                            Toast.makeText(getContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(view).navigate(R.id.action_registerPage_to_loginPage);
                        } else {
                            String errorMessage = "Registration failed";
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                errorMessage = "This email is already registered";
                            } else if (task.getException() != null) {
                                errorMessage = task.getException().getMessage();
                            }
                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });

        });

        return view;
    }
}
