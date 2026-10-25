package com.example.todoapp;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText email, pass; Button login, signupBtn; DBHelper db;
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b); setContentView(R.layout.activity_login);
        email=findViewById(R.id.email); pass=findViewById(R.id.password);
        login=findViewById(R.id.btnLogin); signupBtn=findViewById(R.id.btnGoSignup);
        db=new DBHelper(this);

        // Auto-login if session exists
        SharedPreferences sp = getSharedPreferences("session", MODE_PRIVATE);
        if (sp.getInt("user_id", -1) != -1) { startActivity(new Intent(this, MainActivity.class)); finish(); }

        login.setOnClickListener(v -> {
            if(email.getText().toString().isEmpty() || pass.getText().toString().isEmpty()){
                Toast.makeText(this,"All fields required",Toast.LENGTH_SHORT).show(); return;
            }
            int userId = db.loginUser(email.getText().toString(), pass.getText().toString());
            if(userId != -1){
                getSharedPreferences("session", MODE_PRIVATE).edit().putInt("user_id", userId).apply();
                startActivity(new Intent(this, MainActivity.class)); finish();
            } else Toast.makeText(this,"Invalid credentials",Toast.LENGTH_SHORT).show();
        });
        signupBtn.setOnClickListener(v -> startActivity(new Intent(this, SignupActivity.class)));
    }
}
