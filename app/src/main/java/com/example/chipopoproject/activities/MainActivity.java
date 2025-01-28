package com.example.chipopoproject.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.Navigation;

import com.example.chipopoproject.R;
import com.example.chipopoproject.models.Product;
import com.example.chipopoproject.models.student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

    }

//    public void reg(){
//        String email = ((EditText)findViewById(R.id.textEmailAddress)).getText().toString();
//        String password = ((EditText)findViewById(R.id.textPassword)).getText().toString();
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Toast.makeText(MainActivity.this, "reg success", Toast.LENGTH_SHORT).show();
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Toast.makeText(MainActivity.this, "reg success", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                });
//    }
//    public void login() {
//        String email = ((EditText)findViewById(R.id.TextEmailAddress)).getText().toString();
//        String password = ((EditText)findViewById(R.id.TextPassword)).getText().toString();
//
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // הצלחה
//                            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
//                            Navigation.findNavController(MainActivity.this, R.id.fragmentContainerView).navigate(R.id.action_mainPage_to_calenderPage);
//                        } else {
//                            // שגיאה - מציגים הודעה מתאימה
//                            String errorMessage = "Login failed";
//                            if (task.getException() != null) {
//                                String errorCode = task.getException().getMessage();
//                                if (errorCode.contains("password is invalid")) {
//                                    errorMessage = "Wrong password";
//                                } else if (errorCode.contains("no user record")) {
//                                    errorMessage = "User not found";
//                                } else if (errorCode.contains("badly formatted")) {
//                                    errorMessage = "Invalid email format";
//                                } else if (errorCode.contains("network error")) {
//                                    errorMessage = "Network error, check your connection";
//                                }
//                            }
//                            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

    public void addData ( ) {
        String name = ((EditText)findViewById(R.id.nameText)).getText().toString();
        String phone = ((EditText)findViewById(R.id.textPhone)).getText().toString();
        String email = ((EditText)findViewById(R.id.textEmailAddress)).getText().toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(phone);

        student s = new student(name,email,phone);
        myRef.setValue(s);
    }
}